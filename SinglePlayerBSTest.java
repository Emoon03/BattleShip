package org.cis1200.battleship;

import java.io.File;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerBSTest {
    private SinglePlayerBS battleShip;

    @BeforeEach
    public void setUp() {
        battleShip = new SinglePlayerBS();
    }

    @Test
    public void testInitialBoard() {
        int[][] board = battleShip.getBoard();
        assertNotNull(board);
        assertEquals(7, board.length);
        assertEquals(7, board[0].length);
    }

    @Test
    public void testAllShipsHit() {
        battleShip.resetTest();
        battleShip.placeShipTest();

        battleShip.playerHit(0, 0);
        battleShip.playerHit(0, 1);
        battleShip.playerHit(0, 2);

        battleShip.playerHit(0, 4);
        battleShip.playerHit(0, 5);
        battleShip.playerHit(0, 6);

        battleShip.playerHit(2, 1);
        battleShip.playerHit(3, 1);
        battleShip.playerHit(4, 1);

        battleShip.playerHit(2, 3);
        battleShip.playerHit(2, 4);
        battleShip.playerHit(2, 5);

        battleShip.playerHit(4, 4);
        battleShip.playerHit(5, 4);
        battleShip.playerHit(6, 4);

        if (battleShip.isGameOver() && battleShip.getNumShips() == 0) {
            assertEquals("You Win!", battleShip.winLoss());
        }
        assertEquals(20, battleShip.getMissilesRemaining());
        assertEquals(0, battleShip.getNumShips());

    }

    @Test
    public void testLose() {
        battleShip.resetTest();
        battleShip.placeShipTest();

        battleShip.playerHit(0, 3);
        battleShip.playerHit(1, 0);
        battleShip.playerHit(1, 1);

        battleShip.playerHit(1, 2);
        battleShip.playerHit(1, 3);
        battleShip.playerHit(0, 6);

        battleShip.playerHit(1, 4);
        battleShip.playerHit(1, 5);
        battleShip.playerHit(1, 6);

        battleShip.playerHit(2, 0);
        battleShip.playerHit(2, 2);
        battleShip.playerHit(2, 6);

        battleShip.playerHit(3, 0);
        battleShip.playerHit(3, 2);
        battleShip.playerHit(3, 3);

        battleShip.playerHit(3, 4);
        battleShip.playerHit(3, 5);
        battleShip.playerHit(3, 6);

        battleShip.playerHit(6, 0);
        battleShip.playerHit(6, 1);
        battleShip.playerHit(6, 2);

        assertTrue(battleShip.winLoss().equals("You Lose!"));
        assertTrue(battleShip.isGameOver());
        assertEquals(5, (battleShip.getNumShips()));
        assertEquals(0, battleShip.getMissilesRemaining());

    }

    @Test
    public void testReset() {
        battleShip.reset();
        assertEquals(20, battleShip.getMissilesRemaining());
        assertEquals(5, battleShip.getNumShips());
        assertTrue(battleShip.getCurrentPlayer());
        assertNotNull(battleShip.getShips());
        assertEquals(new LinkedList<String>(), battleShip.getToBeRemoved());
    }

    @Test
    public void testPlaceShips() {
        // Place ships again
        battleShip.resetTest();
        battleShip.placeShips();

        // Check that the board has been reset and ships have been placed
        assertEquals(20, battleShip.getMissilesRemaining());
        assertEquals(5, battleShip.getNumShips());

    }

    @Test
    public void testHitRepeatedButNotSink() {
        battleShip.resetTest();
        int[][] board = battleShip.getBoard();
        TreeMap<String, int[][]> ships = battleShip.getShips();

        // Manually place ships on board for testing purposes
        board[0][0] = 1;
        board[0][1] = 1;
        board[0][2] = 1;

        int[][] shipCoords = new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 } };
        ships.put("Ship 1", shipCoords);

        battleShip.playerHit(0, 1);

        // Verify that hitting a ship registers as a hit
        assertEquals(-1, board[0][1]); // Verify that board was updated
        assertFalse(battleShip.isGameOver()); // Game should not be over
        assertFalse(battleShip.isShipSunk("Ship 1")); // Ship should not be sunk yet

        battleShip.playerHit(0, 1);
        // Hit the same spot again, should not register as another hit
        assertTrue(battleShip.playerHit(0, 1)); // Hit same spot again
        assertEquals(-1, board[0][1]); // Verify that board was not updated again
        assertFalse(battleShip.isGameOver()); // Game should not be over
        assertFalse(battleShip.isShipSunk("Ship 1")); // Ship should not be sunk yet
    }

    @Test
    public void testMissRepeated() {
        battleShip.resetTest();
        battleShip.placeShipTest();
        int[][] board = battleShip.getBoard();

        // Verify that hitting a spot without a ship does not register as a hit
        assertTrue(battleShip.playerHit(0, 3)); // Miss ship at (0,3)
        assertEquals(-2, board[0][3]); // Verify that board was updated as miss
        assertFalse(battleShip.isGameOver()); // Game should not be over

        // Hit the same spot again, should not register as another hit
        assertTrue(battleShip.playerHit(0, 3)); // Hit same spot again
        assertEquals(-2, board[0][3]); // Verify that board was not updated again
        assertFalse(battleShip.isGameOver()); // Game should not be over
    }

    @Test
    public void testSaveFile() {
        battleShip.resetTest();

        battleShip.placeShipTest(); // Place some ships for testing purposes
        File file = new File("testSaveFile.txt");

        battleShip.playerHit(0, 0);
        battleShip.playerHit(0, 1);
        battleShip.playerHit(0, 2);
        battleShip.playerHit(2, 2);
        battleShip.playerHit(0, 5);
        battleShip.playerHit(1, 0);

        battleShip.saveGame(file);
        assertTrue(file.exists());

        battleShip.loadGame(file);

        int[][] expectedBoard = {
            { -1, -1, -1, 0, 1, -1, 1 },
            { -2, 0, 0, 0, 0, 0, 0 },
            { 0, 1, -2, 1, 1, 1, 0 },
            { 0, 1, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0 } };

        int expectedMissilesRemaining = 18;
        int expectedNumShips = 4;

        assertArrayEquals(expectedBoard, battleShip.getBoard());
        assertEquals(expectedMissilesRemaining, battleShip.getMissilesRemaining());
        assertEquals(expectedNumShips, battleShip.getNumShips());
    }

    @Test
    public void testLoadGameInProgress() {
        // Reset the game
        battleShip.resetTest();
        battleShip.placeShipTest(); // Place some ships for testing purposes

        // fire missiles
        battleShip.playerHit(0, 0);
        battleShip.playerHit(0, 1);
        battleShip.playerHit(0, 2);
        battleShip.playerHit(2, 2);
        battleShip.playerHit(0, 5);
        battleShip.playerHit(1, 0);

        // Save the game
        File saveFile = new File("testSaveGameInProgress.txt");
        battleShip.saveGame(saveFile);
        assertTrue(saveFile.exists());

        // Fire some more missiles
        battleShip.playerHit(6, 6);
        battleShip.playerHit(1, 3);
        battleShip.playerHit(1, 4);

        // Load the game
        battleShip.loadGame(saveFile);

        // Check if the game state is restored correctly
        int[][] expectedBoard = {
            { -1, -1, -1, 0, 1, -1, 1 },
            { -2, 0, 0, 0, 0, 0, 0 },
            { 0, 1, -2, 1, 1, 1, 0 },
            { 0, 1, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0 } };

        int expectedMissilesRemaining = 18;
        int expectedNumShips = 4;

        assertArrayEquals(expectedBoard, battleShip.getBoard());
        assertEquals(expectedMissilesRemaining, battleShip.getMissilesRemaining());
        assertEquals(expectedNumShips, battleShip.getNumShips());
    }

}
