/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

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
public class MCTS {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        int player = game.opener();

        while (!state.isTerminal()) {
            TicTacToeNode root = new TicTacToeNode(state);
            MCTS mcts = new MCTS(root);
            Node<TicTacToe> bestMove = mcts.runSearch(1000);
            state = bestMove.state();
            System.out.println("Player " + player);
            System.out.println("Current grid：\n" + ((TicTacToe.TicTacToeState) state).position().render());
            player = 1 - player;
        }

        state.winner().ifPresentOrElse(
                w -> System.out.println("Winner is player " + w),
                () -> System.out.println("It's a draw!")
        );
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;
    private final Random random = new Random();

    public Node<TicTacToe> runSearch(int iterations) {
        if (root.isLeaf()) return root;
        for (int i = 0; i < iterations; i++) {
            Node<TicTacToe> selected = select(root);
            Node<TicTacToe> expanded = expand(selected);
            int winner = simulate(expanded.state());
            backpropagate((TicTacToeNode) expanded, winner);
        }
        return bestChild(root);
    }

    private Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestUCTChild(node);
        }
        return node;
    }

    private Node<TicTacToe> expand(Node<TicTacToe> node) {
        if (!node.isLeaf() && node.children().isEmpty()) {
            node.explore();
        }

        List<Node<TicTacToe>> children = new ArrayList<>(node.children());

        if (children.isEmpty()) {
            return node;
        }

        return children.get(random.nextInt(children.size()));
    }

    private int simulate(State<TicTacToe> state) {
        while (!state.isTerminal()) {
            int player = state.player();
            Move<TicTacToe> move = state.chooseMove(player);
            state = state.next(move);
        }
        return state.winner().orElse(-1);
    }

    private void backpropagate(TicTacToeNode node, int winner) {
        TicTacToeNode current = node;
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