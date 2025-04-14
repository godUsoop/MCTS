package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

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
        // TODO: return a deep copy of this board and score
        return null;
    }

    public boolean move(Game2048Move direction) {
        // TODO: apply the move (UP/DOWN/LEFT/RIGHT), perform merging, and return true if board changes
        return false;
    }

    private int[] compress(int[] row) {
        // TODO: shift all non-zero numbers to the left (removing zeros in between)
        return null;
    }

    private int[] merge(int[] row) {
        // TODO: merge adjacent equal tiles and update score
        return null;
    }

    private void rotateTo(Game2048Move direction) {
        // TODO: rotate board so that move can always be processed as LEFT
    }

    private void rotateBack(Game2048Move direction) {
        // TODO: rotate board back to original orientation
    }

    private void rotateLeft() {
        // TODO: rotate board 90° counter-clockwise
    }

    private void rotateRight() {
        // TODO: rotate board 90° clockwise
    }

    private void rotate180() {
        // TODO: rotate board 180°
    }

    private void copyBoard(int[][] from) {
        // TODO: copy values from another board into this board
    }

    public boolean addRandomTile(Random random) {
        // TODO: choose a random empty cell and assign it 2 (90%) or 4 (10%)
        return false;
    }

    public boolean canMove() {
        // TODO: return true if any valid move is possible
        return false;
    }

    public int getScore() {
        return score;
    }

    public int[][] getBoard() {
        return board;
    }

    public String render() {
        // TODO: return a formatted string of the board for printing
        return null;
    }
}