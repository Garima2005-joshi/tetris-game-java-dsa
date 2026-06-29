package project;

import javax.swing.*;
import java.awt.*;

public class Tetris {
    public static void main(String[] args) {
        Color[][] board = new Color[20][10];

        JFrame frame = new JFrame("TETRIS - DSA (Stack + Queue)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        TetrisPanel panel = new TetrisPanel(board);
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
