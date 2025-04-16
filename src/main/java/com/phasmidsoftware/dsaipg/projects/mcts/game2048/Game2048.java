package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class Game2048 implements Game<Game2048> {

    private final RandomState random = new RandomState(4);

    @Override
    public State<Game2048> start() {
        Game2048Board board = new Game2048Board();
        board.addRandomTile(random.random);
        board.addRandomTile(random.random);
        return new Game2048State(this, board, random, 0);
    }

    @Override
    public int opener() {
        return 0;
    }
}