package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Game2048MCTS implements MCTS<Game2048> {

    private final Node<Game2048> root;
    private final Random random = new Random();

    public Game2048MCTS(Node<Game2048> root) {
        this.root = root;
    }

    @Override
    public Node<Game2048> runSearch(int iterations) {
        if (root.isLeaf()) return root;
        for (int i = 0; i < iterations; i++) {
            Node<Game2048> selected = select(root);
            Node<Game2048> expanded = expand(selected);
            int score = simulate(expanded.state());
            backpropagate(expanded, score);
        }
        return bestChild(root);
    }

    @Override
    public Node<Game2048> select(Node<Game2048> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestUCTChild(node);
        }
        return node;
    }

    @Override
    public Node<Game2048> expand(Node<Game2048> node) {
        if (!node.isLeaf() && node.children().isEmpty()) {
            node.explore();
        }
        List<Node<Game2048>> children = new ArrayList<>(node.children());
        return children.isEmpty() ? node : children.get(random.nextInt(children.size()));
    }

    @Override
    public int simulate(State<Game2048> state) {
        while (!state.isTerminal()) {
            Move<Game2048> move = state.chooseMove(state.player());
            state = state.next(move);
        }
        return ((Game2048State) state).getScore();
    }

    @Override
    public void backpropagate(Node<Game2048> node, int score) {
        Game2048Node current = (Game2048Node) node;
        while (current != null) {
            current.playouts++;
            current.wins += score;
            current = current.parent;
        }
    }

    private Node<Game2048> bestUCTChild(Node<Game2048> node) {
        double logParentVisits = Math.log(node.playouts());
        return node.children().stream()
                .max(Comparator.comparingDouble(child -> uctValue(child, logParentVisits)))
                .orElseThrow();
    }

    private double uctValue(Node<Game2048> child, double logParentVisits) {
        if (child.playouts() == 0) return Double.POSITIVE_INFINITY;
        double avgScore = (double) child.wins() / child.playouts();
        return avgScore + Math.sqrt(2 * logParentVisits / child.playouts());
    }

    private Node<Game2048> bestChild(Node<Game2048> node) {
        return node.children().stream()
                .max(Comparator.comparingInt(Node::playouts))
                .orElseThrow();
    }
}
