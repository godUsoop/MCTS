package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game2048Board {
    private static final int SIZE = 4;
    private final int[][] board;
    private int score;

    public Game2048Board() {
        this.board = new int[SIZE][SIZE];
        this.score = 0;
    }

    public Game2048Board(int[][] board, int score) {
        this.board = board;
        this.score = score;
    }

    public Game2048Board copy() {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
        return new Game2048Board(newBoard, score);
    }

    public boolean move(Game2048Move direction) {
        boolean moved = false;

        rotateTo(direction);
        for (int i = 0; i < SIZE; i++) {
            int[] compressed = compress(board[i]);
            int[] merged = merge(compressed);
            if (!Arrays.equals(board[i], merged)) {
                moved = true;
                board[i] = merged;
            }
        }
        rotateBack(direction);
        return moved;
    }

    private int[] compress(int[] row) {
        return Arrays.stream(row).filter(x -> x != 0).toArray();
    }

    private int[] merge(int[] row) {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        while (i < row.length) {
            if (i + 1 < row.length && row[i] == row[i + 1]) {
                result.add(row[i] * 2);
                score += row[i] * 2;
                i += 2;
            } else {
                result.add(row[i]);
                i++;
            }
        }
        while (result.size() < SIZE) result.add(0);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private void rotateTo(Game2048Move direction) {
        switch (direction) {
            case UP -> rotateLeft();
            case DOWN -> rotateRight();
            case RIGHT -> rotate180();
        }
    }

    private void rotateBack(Game2048Move direction) {
        switch (direction) {
            case UP -> rotateRight();
            case DOWN -> rotateLeft();
            case RIGHT -> rotate180();
        }
    }

    private void rotateLeft() {
        int[][] temp = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                temp[SIZE - j - 1][i] = board[i][j];
        copyBoard(temp);
    }

    private void rotateRight() {
        int[][] temp = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                temp[j][SIZE - i - 1] = board[i][j];
        copyBoard(temp);
    }

    private void rotate180() {
        rotateRight();
        rotateRight();
    }

    private void copyBoard(int[][] from) {
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(from[i], 0, board[i], 0, SIZE);
    }

    public boolean addRandomTile(Random random) {
        List<int[]> empty = new ArrayList<>();
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == 0)
                    empty.add(new int[]{i, j});

        if (empty.isEmpty()) return false;

        int[] cell = empty.get(random.nextInt(empty.size()));
        board[cell[0]][cell[1]] = random.nextDouble() < 0.9 ? 2 : 4;
        return true;
    }

    public boolean canMove() {
        for (Game2048Move dir : Game2048Move.values()) {
            Game2048Board copy = this.copy();
            if (copy.move(dir)) return true;
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public int[][] getBoard() {
        return board;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int val : row)
                sb.append(String.format("%4s", val == 0 ? "." : val)).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }
}