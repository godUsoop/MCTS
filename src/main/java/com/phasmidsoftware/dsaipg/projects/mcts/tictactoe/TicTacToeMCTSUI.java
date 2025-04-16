package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Scanner;


public class TicTacToeMCTSUI extends TicTacToe {


    public void runGame(int player) {
        Scanner scanner = new Scanner(System.in);


        State<TicTacToe> state = start();
        int currentPlayer = opener();
        System.out.println("MCTS version!!!!");

        while (!state.isTerminal()) {
            System.out.println("Current grid：\n" + ((TicTacToeState) state).position().render());
            System.out.println("Next player: " + currentPlayer + (currentPlayer == 1 ? " (X)" : " (O)"));

            
            if (currentPlayer == player) {
                boolean validMove = false;
                while (!validMove) {
                    System.out.print("Enter the row (0–2) where you want to make your move：");
                    int row = scanner.nextInt();
                    System.out.print("Enter the column (0–2) where you want to make your move：");
                    int col = scanner.nextInt();
                    try {

                        TicTacToeMove move = new TicTacToeMove(currentPlayer, row, col);
                        state = state.next(move);
                        validMove = true;
                    } catch (RuntimeException e) {
                        System.out.println("Invalid move：" + e.getMessage() + "do it again!");
                    }
                }
            }

            // computer's turn
            else {

                TicTacToeNode root = new TicTacToeNode(state);
                MCTS mcts = new MCTS(root);

                Node<TicTacToe> bestNode = mcts.runSearch(1000);

                state = bestNode.state();
                System.out.println("Computer(" + currentPlayer + ") moved");
            }


            currentPlayer = 1 - currentPlayer;
        }


        System.out.println("Final result：\n" + ((TicTacToeState) state).position().render());
        if (state.winner().isPresent()) {
            int w = state.winner().get();
            if (w == player) {
                System.out.println("Congratulation you win! (Player " + w + ")");
            } else {
                System.out.println("Computer Win! (Player \" + w + \")");
            }
        } else {
            System.out.println("Draw！");
        }

        scanner.close();
    }

    /**
     * Allow player to choose role (0=O or 1=X)。
     * @return player's role
     */
    public int getPlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose a role you want to play，0=O or 1=X: ");
        int userPlayer = scanner.nextInt();
        while (userPlayer != 0 && userPlayer != 1) {
            System.out.print("Invalid input，insert value again: 0 or 1: ");
            userPlayer = scanner.nextInt();
        }
        return userPlayer;
    }


    public static void main(String[] args) {
        TicTacToeMCTSUI game = new TicTacToeMCTSUI();
        int player = game.getPlayer();
        game.runGame(player);
    }
}
