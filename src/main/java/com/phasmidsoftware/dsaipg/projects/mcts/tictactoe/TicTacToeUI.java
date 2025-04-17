package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import java.util.Scanner;

public class TicTacToeUI extends TicTacToe {


    public void runGame(int player, int version) {
        Scanner scanner = new Scanner(System.in);


        State<TicTacToe> state = start();
        int currentPlayer = opener(); // default is 1

        if (version == 1) {
            System.out.println("MCTS version!!!!!! Hard mode!!!!");
        }

        // game loop
        while (!state.isTerminal()) {
            System.out.println("Current grid：\n" + ((TicTacToeState) state).position().render());
            System.out.println("Next player: " + currentPlayer + (currentPlayer == 1 ? " (X)" : " (O)"));

            if (currentPlayer == player) {
                boolean validMove = false;
                while (!validMove) {
                    System.out.print("Enter the row (0–2) where you want to make your move: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter the column (0–2) where you want to make your move:");
                    int col = scanner.nextInt();

                    try {
                        TicTacToeMove move = new TicTacToeMove(currentPlayer, row, col);
                        state = state.next(move);
                        validMove = true;
                    } catch (RuntimeException e) {

                        System.out.println("Invalid move：" + e.getMessage() + ", do it again!");
                    }
                }
            } else {
                // computer choose next move
                if (version == 0) {
                    Move<TicTacToe> computerMove = state.chooseMove(currentPlayer);
                    state = state.next(computerMove);
                }
                else if (version == 1){
                    TicTacToeNode root = new TicTacToeNode(state);
                    MCTS mcts = new MCTS(root);

                    Node<TicTacToe> bestNode = mcts.runSearch(1000);

                    state = bestNode.state();


                }

                System.out.println("Computer (" + currentPlayer + ") moved");
            }


            currentPlayer = 1 - currentPlayer;
        }

        System.out.println("Final result：\n" + ((TicTacToeState) state).position().render());
        if (state.winner().isPresent()) {
            int w = state.winner().get();
            if (w == player) {
                System.out.println("Congratulation you win!");
            } else {
                System.out.println("Computer Win!");
            }
        } else {
            System.out.println("Draw!");
        }

        scanner.close();
    }

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

    public int chooseVersion(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose a version: 0 for original competitor; 1 for MCTS version competitor: ");
        int version = scanner.nextInt();
        while (version != 0 && version != 1) {
            System.out.print("Invalid input，insert value again: 0 or 1: ");
            version = scanner.nextInt();
        }
        return version;
    }



    public static void main(String[] args) {
        TicTacToeUI game = new TicTacToeUI();
        int player = game.getPlayer();
        int version = game.chooseVersion();
        game.runGame(player, version);
    }

}
