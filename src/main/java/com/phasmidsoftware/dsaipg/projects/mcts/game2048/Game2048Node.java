package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Game2048Node implements Node<Game2048> {

    private final State<Game2048> state;
    private final List<Node<Game2048>> children = new ArrayList<>();
    public final Game2048Node parent;
    public int wins = 0;
    public int playouts = 0;

    public Game2048Node(State<Game2048> state) {
        this(state, null);
    }

    public Game2048Node(State<Game2048> state, Game2048Node parent) {
        this.state = state;
        this.parent = parent;
        if (state.isTerminal()) {
            this.playouts = 1;
            this.wins = ((Game2048State) state).getScore();
        }
    }

    @Override
    public boolean isLeaf() {
        return state.isTerminal();
    }

    @Override
    public State<Game2048> state() {
        return state;
    }

    @Override
    public boolean white() {
        return true;
    }

    @Override
    public Collection<Node<Game2048>> children() {
        return children;
    }

    @Override
    public void addChild(State<Game2048> childState) {
        children.add(new Game2048Node(childState, this));
    }

    @Override
    public void explore() {
        for (Move<Game2048> move : state.moves(state.player())) {
            State<Game2048> nextState = state.next(move);
            addChild(nextState);
        }
    }

    @Override
    public void backPropagate() {
        wins = 0;
        playouts = 0;
        for (Node<Game2048> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    @Override
    public int wins() {
        return wins;
    }

    @Override
    public int playouts() {
        return playouts;
    }
}
