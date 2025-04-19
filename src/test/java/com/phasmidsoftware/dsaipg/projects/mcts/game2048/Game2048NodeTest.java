package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;
import static org.junit.Assert.*;

public class Game2048NodeTest {

    @Test
    public void testWithTerminalState() {
        Game2048 game = new Game2048();

        int[][] deadBoard = {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2}
        };

        Game2048Board board = new Game2048Board(deadBoard, 1234);
        Game2048State terminalState = new Game2048State(game, board, new RandomState(4), 0);
        Game2048Node node = new Game2048Node(terminalState);

        assertTrue(node.isLeaf());
        assertEquals(1, node.playouts());
        assertEquals(1234, node.wins());
    }

    @Test
    public void testAddChildCreatesNewNode() {
        Game2048 game = new Game2048();
        Game2048State state = (Game2048State) game.start();
        Game2048Node node = new Game2048Node(state);

        int before = node.children().size();

        Move<Game2048> move = state.moves(state.player()).iterator().next();
        State<Game2048> next = state.next(move);

        node.addChild(next);

        assertEquals(before + 1, node.children().size());

        Game2048Node child = (Game2048Node) node.children().iterator().next();
        assertEquals(node, child.parent);
    }

    @Test
    public void testExploreGeneratesChildren() {
        Game2048 game = new Game2048();
        Game2048State state = (Game2048State) game.start();
        Game2048Node node = new Game2048Node(state);

        node.explore();
        assertTrue(node.children().size() > 0);
    }

    @Test
    public void testBackPropagateAccumulatesStats() {
        Game2048 game = new Game2048();
        Game2048State parentState = (Game2048State) game.start();
        Game2048Node parent = new Game2048Node(parentState);

        Game2048Node c1 = new Game2048Node(parentState, parent);
        c1.playouts = 1;
        c1.wins = 100;

        Game2048Node c2 = new Game2048Node(parentState, parent);
        c2.playouts = 2;
        c2.wins = 200;

        parent.children().add(c1);
        parent.children().add(c2);

        parent.backPropagate();
        assertEquals(3, parent.playouts());
        assertEquals(300, parent.wins());
    }
}

