import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.util.ArrayList;

/**
 * Java Minesweeper - GUIPlayer
 * 
 * A class that allows the Minesweeper game to be played through a GUI
 * 
 * @author Yohan Berg
 * @version January 4, 2021
 */
public class GUIPlayer extends JComponent implements Runnable {
    
    JTextField heightTextField; // A text field to enter a specified height. Default value is 16.
    JTextField widthTextField; // A text field to enter a specified width. Default value is 30.
    JTextField numBombsTextField; // A text field to enter a specified number of bombs. Default value is 99.
    JPanel gamePanel; // A panel used to represent the game.
    JButton startButton; // A button that uses the height and width provided to create a minesweeper game.
    JButton[][] gameButtons; // An 2D array storing the minesweeper tile buttons.
    Game game; // The minesweeper game object.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUIPlayer());
    }

    public void run() {
        JFrame frame = new JFrame("Minesweeper");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Creating game settings inputs and start game button
        heightTextField = new JTextField("16", 2);
        widthTextField = new JTextField("30", 2);
        numBombsTextField = new JTextField("99", 3);
        startButton = new JButton("New Game");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(widthTextField);
        buttonsPanel.add(heightTextField);
        buttonsPanel.add(numBombsTextField);
        buttonsPanel.add(startButton);
        content.add(buttonsPanel, BorderLayout.NORTH);
        
        // Initializing empty game panel.
        gamePanel = new JPanel();
        content.add(gamePanel, BorderLayout.CENTER);

        gameButtons = null;

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int rows = 0;
                int cols = 0;
                int numBombs = 0;

                // Catching non-integer input
                try {
                    rows = Integer.parseInt(heightTextField.getText());
                    cols = Integer.parseInt(widthTextField.getText());
                    numBombs = Integer.parseInt(numBombsTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid fields! Make sure you entered valid integers",
                        "Input Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Catching illegal board size for game.
                try {
                    game = new Game(rows, cols, numBombs);
                } catch (ImpossibleBoardException ex) {
                    JOptionPane.showMessageDialog(null, "Could not generate board with given fields! " 
                        + "Please make sure the board is large enough to hold all the bombs " 
                        + "and create a starting space.", "Input Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                gamePanel.removeAll();
                gamePanel.setLayout(new GridLayout(rows, cols));

                gameButtons = new JButton[rows][cols];
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        // Storing the button in gameButtons
                        JButton button = new JButton(" ");
                        button.setName(row + "," + col);
                        gameButtons[row][col] = button;
                        
                        // Adding game function calls on button press.
                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                System.out.println(button.getName());
                                int row = Integer.valueOf(button.getName().split(",")[0]);
                                int col = Integer.valueOf(button.getName().split(",")[1]);
                                if (game.isStarted()) {
                                    // Call revealTile at row and col of button
                                    // and create a display text for all revealed buttons.

                                    game.revealTile(row, col);

                                    // Below code is attempted smart reveal.
                                    // for (int[] pos : game.revealTile(row, col)) {
                                    //     String display = "?";
                                    //     Tile tile = game.getTile(row, col);
                                    //     if (tile.getFlagged()) {
                                    //         display = "X";
                                    //     } else if (tile.getHidden()) {
                                    //         display = "-";
                                    //     } else {
                                    //         Tile.TileType type = tile.getType();
                                    //         display = switch (type) {
                                    //             case BOMB ->    "B";
                                    //             case ZERO ->    "0";
                                    //             case ONE ->     "1";
                                    //             case TWO ->     "2";
                                    //             case THREE ->   "3";
                                    //             case FOUR ->    "4";
                                    //             case FIVE ->    "5";
                                    //             case SIX ->     "6";
                                    //             case SEVEN ->   "7";
                                    //             case EIGHT ->   "8";
                                    //             default ->      "?";
                                    //         };
                                    //     }
                                    //     gameButtons[pos[0]][pos[1]].setText(display);
                                    // }
                                } else {
                                    // Call startGame at row and col of button
                                    try {
                                        game.startGame(row, col);
                                    } catch (IndexOutOfBoundsException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Starting tile is out-of-bounds!", 
                                        "startGame Error!", JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                                // Create a display text for all buttons
                                int rows = game.getRows();
                                int cols = game.getCols();
                                for (int r = 0; r < rows; r++) {
                                    for (int c = 0; c < cols; c++) {
                                        String display = "?";
                                        Tile tile = game.getTile(r, c);
                                        if (tile.getFlagged()) {
                                            display = "X";
                                            gameButtons[r][c].setEnabled(false);
                                        } else if (tile.getHidden()) {
                                            display = "-";
                                        } else {
                                            Tile.TileType type = tile.getType();
                                            display = switch (type) {
                                                case BOMB ->    "B";
                                                case ZERO ->    "0";
                                                case ONE ->     "1";
                                                case TWO ->     "2";
                                                case THREE ->   "3";
                                                case FOUR ->    "4";
                                                case FIVE ->    "5";
                                                case SIX ->     "6";
                                                case SEVEN ->   "7";
                                                case EIGHT ->   "8";
                                                default ->      "?";
                                            };
                                            gameButtons[r][c].setEnabled(false);
                                        }
                                        gameButtons[r][c].setText(display);
                                    }
                                }
                            }
                        });

                        // Adding the button to the gamePanel.
                        gamePanel.add(button);
                    }
                }
                // Updates the game panel with the new information
                gamePanel.revalidate();

            }
        };
        startButton.addActionListener(actionListener);
    }
}