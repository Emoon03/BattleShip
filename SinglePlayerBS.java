package org.cis1200.battleship;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class SinglePlayerBS {
    private int[][] board;
    private int missilesRemaining;
    private int numShips;
    private boolean player1;
    private boolean shipSunk;

    private TreeMap<String, int[][]> ships;
    private LinkedList<String> toBeRemoved;

    public SinglePlayerBS() {
        reset();
    }

    public void reset() {
        board = new int[7][7];
        missilesRemaining = 20;
        player1 = true;
        toBeRemoved = new LinkedList<>();
        placeShips();
    }

    public void resetTest() {
        board = new int[7][7];
        missilesRemaining = 20;
        player1 = true;
        toBeRemoved = new LinkedList<>();
    }

    public void placeShips() {

        numShips = 0;
        ships = new TreeMap<>(); // ship map

        while (numShips < 5) {
            int randomRow = (int) (Math.random() * 5); // random number between 0 and 4 inclusive
            int randomCol = (int) (Math.random() * 5); // random number between 0 and 4 inclusive
            int row = randomRow;
            int col = randomCol;

            boolean canPlaceShip = true;
            boolean isVertical = Math.random() < 0.5;

            if (isVertical) {
                for (int i = row; i < row + 3; i++) {
                    if (board[i][col] != 0) {
                        canPlaceShip = false;
                        break;
                    }
                }

                if (canPlaceShip) {
                    int[][] shipCoords = new int[3][2]; // 2d array of ship coords
                    for (int i = row; i < row + 3; i++) {
                        board[i][col] = 1;
                        shipCoords[i - row][0] = i; // store row
                        shipCoords[i - row][1] = col; // store col
                    }
                    ships.put("Ship " + (numShips + 1), shipCoords); // place the completed ship
                                                                     // into map
                    numShips++;
                }

            } else {
                for (int i = col; i < col + 3; i++) {
                    if (board[row][i] != 0) {
                        canPlaceShip = false;
                        break;
                    }
                }

                if (canPlaceShip) {
                    int[][] shipCoords = new int[3][2]; // ship map
                    for (int i = col; i < col + 3; i++) {
                        board[row][i] = 1;
                        shipCoords[i - col][0] = row; // ship map
                        shipCoords[i - col][1] = i; // ship map
                    }
                    ships.put("Ship " + (numShips + 1), shipCoords); // ship map
                    numShips++;
                }
            }
        }
    }

    // 1 signifies the existence of a ship, -1 if ship is hit, 2 if missed
    // Need to find a way to decrement number of ships when each 1x3 ship is taken
    // down.

    public boolean playerHit(int r, int c) {
        if (isGameOver()) {
            return false;
        }

        if (board[r][c] == 1) {
            board[r][c] = -1;

            // iterate through the collection of ships
            for (Map.Entry<String, int[][]> entry : ships.entrySet()) {
                int[][] shipCoords = entry.getValue();
                shipSunk = true;
                for (int i = 0; i < shipCoords.length; i++) {
                    int row = shipCoords[i][0];
                    int col = shipCoords[i][1];
                    if (board[row][col] != -1) {
                        shipSunk = false;
                        break;
                    }
                }
                if (shipSunk) {
                    toBeRemoved.add(entry.getKey());
                    numShips--;
                }
            }

            for (String element : toBeRemoved) {
                ships.remove(element);
            }

        } else if (board[r][c] == 0) {
            board[r][c] = -2;
            missilesRemaining--;
        }

        return true;
    }

    public boolean isGameOver() {
        if (numShips == 0 || missilesRemaining == 0) {
            return true;
        }
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public String winLoss() {
        if (isGameOver()) {
            if (numShips == 0) {
                return "You Win!";
            }

            if (missilesRemaining == 0) {
                return "You Lose!";
            }
        }
        return null;
    }

    public void saveGame(File fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            // Write board state to file
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    writer.write(board[row][col] + ",");
                }
                writer.write("\n");
            }

            // Write other game state information to file
            writer.write(missilesRemaining + "\n");
            writer.write(numShips + "\n");

            for (Map.Entry<String, int[][]> entry : ships.entrySet()) {
                int[][] shipCoords = entry.getValue();
                writer.write(entry.getKey() + "\n");
                for (int i = 0; i < shipCoords.length; i++) {
                    int row = shipCoords[i][0];
                    int col = shipCoords[i][1];
                    writer.write(row + "," + col + "\n");
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving game state: " + e.getMessage());
        }
    }

    public void loadGame(File fileName) {
        ships.clear();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            // Read board state from file
            for (int row = 0; row < board.length; row++) {
                line = bufferedReader.readLine();
                String[] values = line.split(",");
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                }
            }

            // Read other game state information from file
            missilesRemaining = Integer.parseInt(bufferedReader.readLine());
            numShips = Integer.parseInt(bufferedReader.readLine());

            for (int i = 0; i < numShips; i++) {
                String name = bufferedReader.readLine();
                int[][] array = new int[3][2];
                for (int j = 0; j < 3; j++) {
                    String coords = bufferedReader.readLine();
                    String[] coordinate = coords.split(",");
                    array[j][0] = Integer.parseInt(coordinate[0]);
                    array[j][1] = Integer.parseInt(coordinate[1]);
                }

                ships.put(name, array);
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Error loading game state: " + e.getMessage());
        }
    }

    public int getMissilesRemaining() {
        return missilesRemaining;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean getCurrentPlayer() {
        return player1;
    }

    public int getNumShips() {
        return numShips;
    }

    public int getCell(int c, int r) {
        return board[r][c];
    }

    public LinkedList<String> getToBeRemoved() {
        return toBeRemoved;
    }

    public TreeMap<String, int[][]> getShips() {
        return ships;
    }

    public boolean isShipSunk(String s) {
        return shipSunk;
    }

    public void placeShipTest() {
        board[0][0] = 1;
        board[0][1] = 1;
        board[0][2] = 1;

        board[0][4] = 1;
        board[0][5] = 1;
        board[0][6] = 1;

        board[2][1] = 1;
        board[3][1] = 1;
        board[4][1] = 1;

        board[2][3] = 1;
        board[2][4] = 1;
        board[2][5] = 1;

        board[4][4] = 1;
        board[5][4] = 1;
        board[6][4] = 1;

        int[][] shipCoords;

        shipCoords = new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 } };
        ships.put("Ship 1", shipCoords);

        shipCoords = new int[][] { { 0, 4 }, { 0, 5 }, { 0, 6 } };
        ships.put("Ship 2", shipCoords);

        shipCoords = new int[][] { { 2, 1 }, { 3, 1 }, { 4, 1 } };
        ships.put("Ship 3", shipCoords);

        shipCoords = new int[][] { { 2, 3 }, { 2, 4 }, { 2, 5 } };
        ships.put("Ship 4", shipCoords);

        shipCoords = new int[][] { { 4, 4 }, { 5, 4 }, { 6, 4 } };
        ships.put("Ship 5", shipCoords);
    }

    public void printGameState() {
        System.out.println("----------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == -1) {
                    System.out.print("X|");
                } else if (board[i][j] == -2) {
                    System.out.print("O|");
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println("\n----------------");
        }
        System.out.println("Missiles Remaining= " + missilesRemaining);
        System.out.println("ships remaining= " + numShips);
    }

    public static void main(String[] args) {
        SinglePlayerBS t = new SinglePlayerBS();

        t.resetTest();
        t.placeShipTest();

        t.playerHit(0, 0);
        t.printGameState();
        t.playerHit(0, 1);
        t.printGameState();
        t.playerHit(0, 2);
        t.printGameState();

        t.playerHit(0, 3);
        t.printGameState();
        t.playerHit(1, 1);
        t.printGameState();
        t.playerHit(1, 5);
        t.printGameState();
        t.playerHit(6, 6);
        t.printGameState();

        t.playerHit(0, 4);
        t.printGameState();
        t.playerHit(0, 5);
        t.printGameState();
        t.playerHit(0, 6);
        t.printGameState();

        t.playerHit(2, 1);
        t.printGameState();
        t.playerHit(3, 1);
        t.printGameState();
        t.playerHit(4, 1);
        t.printGameState();

        t.playerHit(2, 3);
        t.printGameState();
        t.playerHit(2, 4);
        t.printGameState();
        t.playerHit(2, 5);
        t.printGameState();

        t.playerHit(4, 4);
        t.printGameState();
        t.playerHit(5, 4);
        t.printGameState();
        t.playerHit(6, 4);
        t.printGameState();

        System.out.println(t.winLoss());
    }

}
