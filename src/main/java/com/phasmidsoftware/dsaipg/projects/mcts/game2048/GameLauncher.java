package com.phasmidsoftware.dsaipg.projects.mcts.game2048;
import java.util.Scanner;

public class GameLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game2048 game = new Game2048();
        Game2048State state = (Game2048State) game.start();

        System.out.println("Welcome to 2048!");
        printState(state);

        while (!state.isTerminal()) {
            System.out.print("Enter move (w/a/s/d): ");
            String input = scanner.nextLine().trim().toLowerCase();

            Game2048Move move = switch (input) {
                case "w" -> Game2048Move.UP;
                case "s" -> Game2048Move.DOWN;
                case "a" -> Game2048Move.LEFT;
                case "d" -> Game2048Move.RIGHT;
                default -> null;
            };

            if (move == null) {
                System.out.println("Invalid input. Use w/a/s/d.");
                continue;
            }

            Game2048Board before = state.getBoard().copy();
            Game2048State next = (Game2048State) state.next(move);

            if (sameBoard(before.getBoard(), next.getBoard().getBoard())) {
                System.out.println("No tiles moved. Try another direction.");
            } else {
                state = next;
                printState(state);
            }
        }

        System.out.println("Game Over! Final Score: " + state.getScore());
        System.out.println(state.render());
    }

    private static void printState(Game2048State state) {
        System.out.println(state.render());
        System.out.println("Score: " + state.getScore());
    }

    private static boolean sameBoard(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                if (a[i][j] != b[i][j]) return false;
        return true;
    }
}
