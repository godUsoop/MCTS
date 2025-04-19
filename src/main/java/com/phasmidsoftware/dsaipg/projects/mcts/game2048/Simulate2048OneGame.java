package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class Simulate2048OneGame {
    public static void main(String[] args) {
        int simulation = 100;
        Game2048 game = new Game2048();
        State<Game2048> state = game.start();

        int moveNumber = 1;
        while (!state.isTerminal()) {
            System.out.println("=== Move " + moveNumber + " ===");
            System.out.println(((Game2048State) state).render());
            System.out.println("Score: " + ((Game2048State) state).getScore());
            System.out.println();

            Node<Game2048> root = new Game2048Node(state);
            MCTS<Game2048> mcts = new Game2048MCTS(root);
            Node<Game2048> best = mcts.runSearch(simulation);

            state = best.state();
            moveNumber++;
        }

        System.out.println("=== Game Over ===");
        System.out.println(((Game2048State) state).render());
        System.out.println("Final Score: " + ((Game2048State) state).getScore());
        System.out.println("Max Tile: " + getMaxTile(((Game2048State) state).getBoard().getBoard()));
    }

    private static int getMaxTile(int[][] board) {
        int max = 0;
        for (int[] row : board)
            for (int val : row)
                max = Math.max(max, val);
        return max;
    }
}
