package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;
import static org.junit.Assert.*;

public class Game2048StateTest {

    private Game2048 game;
    private Game2048State initialState;
    private final int initialPlayer = 0;

    @Before
    public void setUp() {
        game = new Game2048();
        Game2048Board board = new Game2048Board();
        // Initialize with controlled random seed
        RandomState randomState = new RandomState(123);
        board.addRandomTile(randomState.random);
        board.addRandomTile(randomState.random);
        initialState = new Game2048State(game, board, randomState, initialPlayer);
    }

    @Test
    public void testInitialStateSetup() {
        assertEquals("Initial player should be 0", initialPlayer, initialState.player());
        assertEquals("Initial score should be 0", 0, initialState.getScore());
        assertEquals("Should start with 2 tiles", 2, countNonZeroTiles(initialState.getBoard()));
    }

    @Test
    public void testStateTransition() {
        // Setup board state for predictable move
        int[][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Game2048State state = createState(initialBoard);

        State<Game2048> nextState = state.next(Game2048Move.LEFT);

        // Verify merge and new tile
        assertEquals("Score should increase by 4", 4, ((Game2048State) nextState).getScore());
        assertEquals("Should have 2 tiles after merge + new tile",
                2, countNonZeroTiles(((Game2048State) nextState).getBoard()));
    }

    @Test
    public void testTerminalStateDetection() {
        // Create full board with no possible merges
        int[][] terminalBoard = {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2}
        };
        Game2048State state = createState(terminalBoard);

        assertTrue("Should recognize terminal state", state.isTerminal());
        assertEquals("No winner in terminal state", Optional.empty(), state.winner());
    }

    @Test
    public void testWinnerDetection() {
        // Create board with 2048 tile
        int[][] winningBoard = {
                {2048, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Game2048State state = createState(winningBoard);

        assertEquals("Should detect winner", Optional.of(initialPlayer), state.winner());
        assertFalse("Game should continue after winning", state.isTerminal());
    }

    @Test
    public void testScoreAccumulation() {
        // Setup merge scenario
        int[][] mergeBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Game2048State state = createState(mergeBoard);

        State<Game2048> nextState = state.next(Game2048Move.LEFT);
        assertEquals("Score should reflect merge", 4, ((Game2048State) nextState).getScore());
    }

    private Game2048State createState(int[][] board) {
        return new Game2048State(game, new Game2048Board(board, 0), new RandomState(0), initialPlayer);
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
}