import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Java Minesweeper - GUIPlayer
 * 
 * A class that allows the Minesweeper game to be played through a GUI
 * 
 * @author Yohan Berg
 * @version December 20, 2020
 */
public class GUIPlayer extends JComponent implements Runnable {
    
    JTextField heightTextField; // A text field to enter a specified height. Default value is 16.
    JTextField widthTextField; // A text field to enter a specified width. Default value is 30.
    JTextField numBombsTextField; // A text field to enter a specified number of bombs. Default value is 99.
    JButton startButton; // A button that uses the height and width provided to create a minesweeper game.
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

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Create new Game object and display it.

            }
        };
        startButton.addActionListener(actionListener);
    }
}