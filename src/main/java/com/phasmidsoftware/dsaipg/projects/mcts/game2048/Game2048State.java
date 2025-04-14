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
    public Game2048 game() {
        return game;
    }

    @Override
    public int player() {
        return player;
    }

    @Override
    public Optional<Integer> winner() {
        // TODO: return Optional.of(player) if any tile >= 2048 is present, else Optional.empty()
        return null;
    }

    @Override
    public Random random() {
        return random.random;
    }

    @Override
    public boolean isTerminal() {
        // TODO: return true if the game is over (i.e., no valid moves are left)
        return false;
    }

    @Override
    public Collection<Move<Game2048>> moves(int player) {
        // TODO: return list of valid Game2048Move directions that change the board state
        return null;
    }

    @Override
    public State<Game2048> next(Move<Game2048> move) {
        // TODO:
        // 1. Make a copy of the board
        // 2. Apply the move (left, right, up, down)
        // 3. If the move results in a change, add a random tile
        // 4. Return a new Game2048State with the updated board and randomState.next()
        return null;
    }

    public Game2048Board getBoard() {
        return board;
    }

    public int getScore() {
        return board.getScore();
    }

    public String render() {
        return board.render();
    }
}