package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

public enum Game2048Move implements Move<Game2048> {
    UP, DOWN, LEFT, RIGHT;

    @Override
    public int player() {
        return 0;
    }
}
