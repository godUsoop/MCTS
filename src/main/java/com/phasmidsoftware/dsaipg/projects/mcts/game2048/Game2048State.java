package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class Game2048State implements State<Game2048> {
    private final Game2048 game;
    private final Game2048Board board;
    private final RandomState random;
    private final int player;

    public Game2048State(Game2048 game, Game2048Board board, RandomState random, int player) {
        this.game = game;
        this.board = board;
        this.random = random;
        this.player = player;
    }

    @Override
    public Game2048 game() { return game; }

    @Override
    public int player() { return player; }

    @Override
    public Optional<Integer> winner() {
        for (int[] row : board.getBoard())
            for (int value : row)
                if (value >= 2048) return Optional.of(player);
        return Optional.empty();
    }

    @Override
    public Random random() { return random.random; }

    @Override
    public boolean isTerminal() { return !board.canMove(); }

    @Override
    public Collection<Move<Game2048>> moves(int player) {
        List<Move<Game2048>> result = new ArrayList<>();
        for (Game2048Move m : Game2048Move.values()) {
            Game2048Board copy = board.copy();
            if (copy.move(m)) result.add(m);
        }
        return result;
    }

    @Override
    public State<Game2048> next(Move<Game2048> move) {
        Game2048Board nextBoard = board.copy();
        boolean moved = nextBoard.move((Game2048Move) move);
        if (moved) nextBoard.addRandomTile(random());
        return new Game2048State(game, nextBoard, random, player);
    }

    public boolean sameBoard(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                if (a[i][j] != b[i][j]) return false;
        return true;
    }

    public Game2048Board getBoard() { return board; }
    public int getScore() { return board.getScore(); }
    public String render() { return board.render(); }
}