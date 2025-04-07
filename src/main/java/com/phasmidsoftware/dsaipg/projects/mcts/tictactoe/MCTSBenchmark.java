package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Optional;

public class MCTSBenchmark {
    public static void main(String[] args) {
        System.out.println("=== Test 1: Decision Time vs Simulations ===");
        testSimulationTimes();

        System.out.println("\n=== Test 2: MCTS vs Random Player ===");
        playMCTSvsRandom(100, 1000);

        System.out.println("\n=== Test 2: MCTS vs Random Player ===");
        playMCTSvsRandom(500, 1000);


        System.out.println("\n=== Test 3: Full Game Decision Times ===");
        runFullGameWithTiming(1000);
    }

    public static void testSimulationTimes() {
        int[] iterations = {10, 100, 1000, 5000};
        State<TicTacToe> state = new TicTacToe().start();

        for (int iter : iterations) {
            Node<TicTacToe> root = new TicTacToeNode(state);
            MCTS mcts = new MCTS(root);

            long start = System.currentTimeMillis();
            Node<TicTacToe> best = mcts.runSearch(iter);
            long end = System.currentTimeMillis();

            System.out.printf("Simulations: %-5d | Time: %d ms\n", iter, (end - start));
        }
    }

    public static void playMCTSvsRandom(int games, int iterations) {
        int mctsWins = 0, randomWins = 0, draws = 0;

        for (int g = 0; g < games; g++) {
            State<TicTacToe> state = new TicTacToe().start();
            int currentPlayer = TicTacToe.X;

            while (!state.isTerminal()) {
                if (currentPlayer == TicTacToe.X) {
                    MCTS mcts = new MCTS(new TicTacToeNode(state));
                    state = mcts.runSearch(iterations).state();
                } else {
                    Move<TicTacToe> move = state.chooseMove(currentPlayer);
                    state = state.next(move);
                }
                currentPlayer = 1 - currentPlayer;
            }

            Optional<Integer> winner = state.winner();
            if (winner.isEmpty()) draws++;
            else if (winner.get() == TicTacToe.X) mctsWins++;
            else randomWins++;
        }

        System.out.printf("After %d games:\n", games);
        System.out.printf("MCTS Wins   : %d\n", mctsWins);
        System.out.printf("Random Wins : %d\n", randomWins);
        System.out.printf("Draws       : %d\n", draws);
    }

    public static void runFullGameWithTiming(int iterationsPerMove) {
        State<TicTacToe> state = new TicTacToe().start();
        int player = TicTacToe.X;
        int moves = 0;
        long totalTime = 0;

        while (!state.isTerminal()) {
            long start = System.currentTimeMillis();
            MCTS mcts = new MCTS(new TicTacToeNode(state));
            Node<TicTacToe> best = mcts.runSearch(iterationsPerMove);
            long end = System.currentTimeMillis();

            long time = end - start;
            totalTime += time;
            moves++;

            System.out.printf("Move %d by player %d took %d ms\n", moves, player, time);
            state = best.state();
            player = 1 - player;
        }

        System.out.println("Game over: " + (state.winner().isPresent() ? "Winner is " + state.winner().get() : "Draw"));
        System.out.printf("Total moves: %d | Total time: %d ms | Avg time per move: %.2f ms\n",
                moves, totalTime, totalTime / (double) moves);
    }
}
