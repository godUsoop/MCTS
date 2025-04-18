/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class TicTacToeMCTS implements MCTS<TicTacToe> {

    private final Node<TicTacToe> root;
    private final Random random = new Random();

    public TicTacToeMCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    @Override
    public Node<TicTacToe> runSearch(int iterations) {
        if (root.isLeaf()) return root;
        for (int i = 0; i < iterations; i++) {
            Node<TicTacToe> selected = select(root);
            Node<TicTacToe> expanded = expand(selected);
            int winner = simulate(expanded.state());
            backpropagate(expanded, winner);
        }
        return bestChild(root);
    }

    @Override
    public Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestUCTChild(node);
        }
        return node;
    }

    @Override
    public Node<TicTacToe> expand(Node<TicTacToe> node) {
        node.explore();
        List<Node<TicTacToe>> children = new ArrayList<>(node.children());
        return children.isEmpty() ? node : children.get(random.nextInt(children.size()));
    }

    @Override
    public int simulate(State<TicTacToe> state) {
        while (!state.isTerminal()) {
            Move<TicTacToe> move = state.chooseMove(state.player());
            state = state.next(move);
        }
        return state.winner().orElse(-1);
    }

    @Override
    public void backpropagate(Node<TicTacToe> node, int winner) {
        TicTacToeNode current = (TicTacToeNode) node;
        while (current != null) {
            current.playouts++;
            if (current.state().winner().isPresent() && current.state().winner().get() == winner)
                current.wins += 2;
            else if (!current.state().winner().isPresent())
                current.wins += 1;
            current = current.parent;
        }
    }

    private Node<TicTacToe> bestUCTChild(Node<TicTacToe> node) {
        double logParentVisits = Math.log(node.playouts());
        return node.children().stream()
                .max(Comparator.comparingDouble(child -> uctValue(child, logParentVisits)))
                .orElseThrow();
    }

    private double uctValue(Node<TicTacToe> child, double logParentVisits) {
        if (child.playouts() == 0) return Double.POSITIVE_INFINITY;
        double winRate = (double) child.wins() / child.playouts();
        return winRate + Math.sqrt(2 * logParentVisits / child.playouts());
    }

    private Node<TicTacToe> bestChild(Node<TicTacToe> node) {
        return node.children().stream()
                .max(Comparator.comparingInt(Node::playouts))
                .orElseThrow();
    }
}