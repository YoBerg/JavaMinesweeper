import java.awt.*;
import java.swing.*;

import javax.swing.JFrame;
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

        
    }

    public GUIPlayer() {

    }
}