package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Game2048Test {

    private Game2048 game;

    @Before
    public void setUp() {
        game = new Game2048();
    }

    private int countNonZeroTiles(Game2048Board board) {
        int count = 0;
        for (int[] row : board.getBoard()) {
            for (int val : row) {
                if (val != 0) count++;
            }
        }
        return count;
    }

    @Test
    public void startWithTwoTiles() {
        State<Game2048> state = game.start();
        Game2048Board board = ((Game2048State) state).getBoard();
        assertEquals(2, countNonZeroTiles(board));
    }

    @Test
    public void openerIsPlayerZero() {
        assertEquals(0, game.opener());
    }

    @Test
    public void deterministicInitialization() {
        // Use int seed instead of long
        int seed = 123; // Now using int instead of long

        // Create two independent random states with the same int seed
        RandomState controlledRandom1 = new RandomState(seed);
        RandomState controlledRandom2 = new RandomState(seed);

        // Initialize first state
        Game2048Board board1 = new Game2048Board();
        board1.addRandomTile(controlledRandom1.random);
        board1.addRandomTile(controlledRandom1.random);
        Game2048State state1 = new Game2048State(game, board1, controlledRandom1, 0);

        // Initialize second state
        Game2048Board board2 = new Game2048Board();
        board2.addRandomTile(controlledRandom2.random);
        board2.addRandomTile(controlledRandom2.random);
        Game2048State state2 = new Game2048State(game, board2, controlledRandom2, 0);

        // Verify boards are identical
        assertEquals(state1.render(), state2.render());
    }

    @Test
    public void initialScoreZero() {
        Game2048State state = (Game2048State) game.start();
        assertEquals(0, state.getScore());
    }

    @Test
    public void gameInstanceConsistency() {
        Game2048State state = (Game2048State) game.start();
        assertSame(game, state.game());
    }

    @Test
    public void terminalStateDetection() {
        int[][] fullBoard = {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2}
        };
        Game2048State state = new Game2048State(game, new Game2048Board(fullBoard, 0), new RandomState(4), 0);
        assertTrue(state.isTerminal());
    }

    @Test
    public void testRenderOutput() {
        int[][] testBoard = {
                {0, 2, 0, 4},
                {8, 0, 16, 0},
                {0, 32, 0, 64},
                {128, 0, 256, 0}
        };
        Game2048Board board = new Game2048Board(testBoard, 0);
        String expected =
                ". 2 . 4 \n" +
                        "8 . 16 . \n" +
                        ". 32 . 64 \n" +
                        "128 . 256 . \n";
        assertEquals(
                expected.replaceAll("\\s+", ""),
                board.render().replaceAll("\\s+", "")
        );
    }
}