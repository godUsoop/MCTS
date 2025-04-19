package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.MCTS;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class MCTSBenchmark {
    public static void main(String[] args) {
        int totalGames = 10;
        int simulation = 100;

        int totalScore = 0;
        int totalMoves = 0;
        int globalMaxTile = 0;
        long totalTime = 0;

        for (int i = 1; i <= totalGames; i++) {
            Game2048 game = new Game2048();
            State<Game2048> state = game.start();
            int moves = 0;
            long start = System.currentTimeMillis();

            while (!state.isTerminal()) {
                Node<Game2048> root = new Game2048Node(state);
                MCTS<Game2048> mcts = new Game2048MCTS(root);
                Node<Game2048> best = mcts.runSearch(simulation);
                state = best.state();
                moves++;
            }

            long end = System.currentTimeMillis();
            int score = ((Game2048State) state).getScore();
            int maxTile = getMaxTile(((Game2048State) state).getBoard().getBoard());

            totalScore += score;
            totalMoves += moves;
            totalTime += (end - start);
            globalMaxTile = Math.max(globalMaxTile, maxTile);

            System.out.printf("Game %3d | Score: %5d | Max Tile: %4d | Moves: %2d\n", i, score, maxTile, moves);
        }

        System.out.println("\n=== Benchmark Summary ===");
        System.out.println("Total Games          : " + totalGames);
        System.out.println("Number of Simulations: " + simulation);
        System.out.println("Average Score        : " + (totalScore / totalGames));
        System.out.println("Average Moves        : " + (totalMoves / totalGames));
        System.out.println("Max Tile Achieved    : " + globalMaxTile);
        System.out.printf("Avg Time per Game    : %.2f ms\n", totalTime / (double) totalGames);
        System.out.printf("Avg Time per Move    : %.2f ms\n", totalTime / (double) totalMoves);
    }

    private static int getMaxTile(int[][] board) {
        int max = 0;
        for (int[] row : board)
            for (int val : row)
                max = Math.max(max, val);
        return max;
    }
}
