package com.phasmidsoftware.dsaipg.projects.mcts.game2048;

import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

public class Game2048BoardTest {
    private Game2048Board board;
    private final Random seededRandom = new Random(123);

    @Before
    public void setUp() {
        board = new Game2048Board();
    }

    @Test
    public void testInitialBoardEmpty() {
        assertEquals(0, countNonZeroTiles(board));
    }

    @Test
    public void testAddRandomTile() {
        assertTrue(board.addRandomTile(seededRandom));
        assertEquals(1, countNonZeroTiles(board));
    }

    @Test
    public void testMoveLeftMerge() {
        int[][] initial = {{2,2,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
        board = new Game2048Board(initial, 0);
        assertTrue(board.move(Game2048Move.LEFT)); // Fixed: Use Game2048Move
        assertArrayEquals(new int[]{4,0,0,0}, board.getBoard()[0]);
    }

    @Test
    public void testMoveRightWithSpace() {
        int[][] initial = {{2,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
        board = new Game2048Board(initial, 0);
        assertTrue(board.move(Game2048Move.RIGHT)); // Fixed
        assertArrayEquals(new int[]{0,0,0,2}, board.getBoard()[0]);
    }

    @Test
    public void testVerticalMergeUp() {
        int[][] initial = {{2,0,0,0}, {2,0,0,0}, {0,0,0,0}, {0,0,0,0}};
        board = new Game2048Board(initial, 0);
        assertTrue(board.move(Game2048Move.UP)); // Fixed
        assertArrayEquals(new int[]{4,0,0,0}, board.getBoard()[0]);
    }

    @Test
    public void testNoPossibleMoves() {
        int[][] fullBoard = {{2,4,2,4}, {4,2,4,2}, {2,4,2,4}, {4,2,4,2}};
        board = new Game2048Board(fullBoard, 0);
        assertFalse(board.canMove());
    }

    @Test
    public void testBoardCopyIndependence() {
        board.addRandomTile(seededRandom);
        Game2048Board copy = board.copy();
        copy.move(Game2048Move.LEFT); // Fixed
        assertNotEquals(board.render(), copy.render());
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