package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class MCTSTest {

    @Test
    public void testRunSearchReturnsNewState() {
        State<TicTacToe> initialState = new TicTacToe().start();
        MCTS mcts = new MCTS(new TicTacToeNode(initialState));
        Node<TicTacToe> result = mcts.runSearch(100);
        assertNotNull(result);
        assertNotEquals(initialState, result.state());
    }

    @Test
    public void testRunSearchOnTerminalState() {
        Position terminalPosition = Position.parsePosition("X X X\nO O .\n. . .", TicTacToe.X);
        State<TicTacToe> terminalState = new TicTacToe().new TicTacToeState(terminalPosition);
        MCTS mcts = new MCTS(new TicTacToeNode(terminalState));
        Node<TicTacToe> result = mcts.runSearch(100);
        assertEquals(terminalState, result.state());
    }

    @Test
    public void testSimulationProducesValidWinner() {
        State<TicTacToe> state = new TicTacToe().start();
        MCTS mcts = new MCTS(new TicTacToeNode(state));
        int winner = mcts.runSearch(10).state().winner().orElse(-1);
        assertTrue(winner == -1 || winner == 0 || winner == 1);
    }

    @Test
    public void testBackpropagationIncreasesPlayouts() {
        State<TicTacToe> initialState = new TicTacToe().start();
        TicTacToeNode root = new TicTacToeNode(initialState);
        MCTS mcts = new MCTS(root);
        mcts.runSearch(100);
        assertTrue(root.playouts > 0);
    }

    @Test
    public void testExpandAddsChildren() {
        State<TicTacToe> state = new TicTacToe().start();
        TicTacToeNode root = new TicTacToeNode(state);
        MCTS mcts = new MCTS(root);
        mcts.runSearch(1);
        assertFalse(root.children().isEmpty());
    }

    @Test
    public void testSimulateNearEndGame() {
        Position almostEnd = Position.parsePosition("X O X\nO X O\nX . O", TicTacToe.O);
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(almostEnd);
        MCTS mcts = new MCTS(new TicTacToeNode(state));
        int winner = mcts.runSearch(1).state().winner().orElse(-1);
        assertTrue(winner == 0 || winner == 1 || winner == -1);
    }

    @Test
    public void testUCTValueCalculation() {
        Node<TicTacToe> mockChild = new Node<>() {
            public int wins() { return 6; }
            public int playouts() { return 5; }
            public boolean isLeaf() { return true; }
            public State<TicTacToe> state() { return null; }
            public boolean white() { return true; }
            public Collection<Node<TicTacToe>> children() { return new ArrayList<>(); }
            public void addChild(State<TicTacToe> s) {}
            public void backPropagate() {}
            public void explore() {}
        };

        Node<TicTacToe> parent = new TicTacToeNode(new TicTacToe().start());

        MCTS mcts = new MCTS(parent);
        double logParentVisits = Math.log(10);
        double uct = callUctValue(mcts, mockChild, logParentVisits);
        assertTrue(uct > 0);
    }

    private double callUctValue(MCTS mcts, Node<TicTacToe> child, double logVisits) {
        try {
            var method = MCTS.class.getDeclaredMethod("uctValue", Node.class, double.class);
            method.setAccessible(true);
            return (double) method.invoke(mcts, child, logVisits);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke uctValue via reflection", e);
        }
    }

}