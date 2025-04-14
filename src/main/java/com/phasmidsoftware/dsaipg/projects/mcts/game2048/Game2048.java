package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class Game2048 implements Game<Game2048> {

    @Override
    public State<Game2048> start() {
        // TODO: create initial Game2048Board, add 2 random tiles, and return new Game2048State
        return null;
    }

    // 2048 is a single-player game
    @Override
    public int opener() {
        return 0;
    }
}