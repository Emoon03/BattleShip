package org.cis1200.battleship;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private SinglePlayerBS bs; // model for the game
    private JLabel status; // current status text

    private JLabel missilesLabel;

    private JLabel shipsLabel;

    // Game constants
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    private File saveFile = new File("saveFile.txt");

    /**
     * Initializes the game board.
     */
    public GameBoard(JPanel statusPanel) {
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        bs = new SinglePlayerBS(); // initializes model for the game

        status = new JLabel("Player 1's Turn");
        status.setAlignmentX(Component.CENTER_ALIGNMENT); // centers the JLabel horizontally
        statusPanel.add(status);

        missilesLabel = new JLabel("Missiles remaining: 20");
        missilesLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // centers the JLabel horizontally
        statusPanel.add(missilesLabel);

        shipsLabel = new JLabel("Ships remaining: 5");
        shipsLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // centers the JLabel horizontally
        statusPanel.add(shipsLabel);

        String instructions = "Welcome to Battleship!\n\n" +
                "In this game, you are trying to sink your opponent's ships " +
                "by guessing where they are randomly located on the board.\n\n" +
                "To play, click on a square on the game board to make a guess. \n" +
                "If there is a ship in that square, it will be hit and display an X. \n" +
                "If you miss, it will display an O. \nYou will have 20 missiles to fire. " +
                "Missiles deplete only when you miss! \n\n" +

                "Press the reset button to start a new game. Use the save and load buttons \n" +
                "to save and load the current game you are playing. \n\n" +

                "Try to shoot down all 5 ships before you run out of missiles. \n" +
                "Good luck!";
        JOptionPane.showMessageDialog(
                this, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                bs.playerHit(p.y / 100, p.x / 100);

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        bs.reset();
        status.setText("Player 1's Turn");
        missilesLabel.setText("Missiles Remaining: " + bs.getMissilesRemaining());// update missile
                                                                                  // label
        shipsLabel.setText("Ships Remaining: " + bs.getNumShips());

        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void saveGame() {
        bs.saveGame(saveFile);
    }

    public void loadGame() {
        bs.loadGame(saveFile);
        status.setText("Player 1's Turn");
        missilesLabel.setText("Missiles Remaining: " + bs.getMissilesRemaining());// update missile
                                                                                  // label
        shipsLabel.setText("Ships Remaining: " + bs.getNumShips());
        repaint();

        requestFocusInWindow();

    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (bs.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
            missilesLabel.setText("Missiles Remaining: " + bs.getMissilesRemaining());// update
                                                                                      // missile
                                                                                      // label
            shipsLabel.setText("Ships Remaining: " + bs.getNumShips());

        }

        boolean gameOver = bs.isGameOver();
        if (gameOver) {
            status.setText(bs.winLoss());
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 800);
        g.drawLine(200, 0, 200, 800);
        g.drawLine(300, 0, 300, 800);
        g.drawLine(400, 0, 400, 800);
        g.drawLine(500, 0, 500, 800);
        g.drawLine(600, 0, 600, 800);

        g.drawLine(0, 100, 800, 100);
        g.drawLine(0, 200, 800, 200);
        g.drawLine(0, 300, 800, 300);
        g.drawLine(0, 400, 800, 400);
        g.drawLine(0, 500, 800, 500);
        g.drawLine(0, 600, 800, 600);

        // Draws X's and O's
        // Find ships and ocean
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                int state = bs.getCell(i, j);
                if (state == -1) {
                    g.drawLine(30 + 100 * i, 30 + 100 * j, 70 + 100 * i, 70 + 100 * j);
                    g.drawLine(30 + 100 * i, 70 + 100 * j, 70 + 100 * i, 30 + 100 * j);
                } else if (state == -2) {
                    g.drawOval(30 + 100 * i, 30 + 100 * j, 40, 40);

                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
