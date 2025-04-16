package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import org.junit.Test;
import static org.junit.Assert.*;

public class Game2048MoveTest {

    @Test
    public void testMoveEnumValues() {
        // Verify all move directions exist
        Game2048Move[] moves = Game2048Move.values();
        assertEquals(4, moves.length);
        assertArrayEquals(new Game2048Move[]{
                        Game2048Move.UP,
                        Game2048Move.DOWN,
                        Game2048Move.LEFT,
                        Game2048Move.RIGHT},
                moves);
    }

    @Test
    public void testLeftMoveRotationConsistency() {
        // Create test board
        int[][] original = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Game2048Board board = new Game2048Board(original, 0);

        // Test left-right sequence
        board.move(Game2048Move.LEFT);
        board.move(Game2048Move.RIGHT);

        // Verify board returns to original state
        assertArrayEquals(original, board.getBoard());
    }

    @Test
    public void testUpMoveEffectiveness() {
        // Setup initial board state
        int[][] initial = {
                {0, 0, 0, 0},
                {0, 0, 0, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Game2048Board board = new Game2048Board(initial, 0);

        // Execute move
        assertTrue(board.move(Game2048Move.UP));

        // Verify tile moved to top
        assertEquals(2, board.getBoard()[0][3]);
    }

    @Test
    public void testDownMoveWithMultipleTiles() {
        // Setup stack of mergeable tiles
        int[][] initial = {
                {2, 0, 0, 0},
                {2, 0, 0, 0},
                {4, 0, 0, 0},
                {4, 0, 0, 0}
        };
        Game2048Board board = new Game2048Board(initial, 0);

        // Execute move
        assertTrue(board.move(Game2048Move.DOWN));

        // Verify merges and positions
        assertArrayEquals(new int[]{0, 0, 0, 0}, board.getBoard()[0]);
        assertArrayEquals(new int[]{4, 0, 0, 0}, board.getBoard()[2]);
        assertArrayEquals(new int[]{8, 0, 0, 0}, board.getBoard()[3]);
    }

    @Test
    public void testPlayerAssociation() {
        // Verify all moves belong to player 0
        for (Game2048Move move : Game2048Move.values()) {
            assertEquals("Move " + move + " has incorrect player association",
                    0, move.player());
        }
    }

    @Test
    public void testRightMoveWithMerge() {
        // Setup mergeable pair
        int[][] initial = {
                {0, 0, 2, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Game2048Board board = new Game2048Board(initial, 0);

        // Execute move
        assertTrue(board.move(Game2048Move.RIGHT));

        // Verify merge
        assertArrayEquals(new int[]{0, 0, 0, 4}, board.getBoard()[0]);
    }

    @Test
    public void testMovePriorityOrder() {
        // Verify enum declaration order
        Game2048Move[] moves = Game2048Move.values();
        assertEquals(Game2048Move.UP, moves[0]);
        assertEquals(Game2048Move.DOWN, moves[1]);
        assertEquals(Game2048Move.LEFT, moves[2]);
        assertEquals(Game2048Move.RIGHT, moves[3]);
    }
}