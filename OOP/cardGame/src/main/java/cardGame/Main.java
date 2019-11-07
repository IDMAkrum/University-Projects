package cardGame;

import cardGame.controller.ButtonBar;
import cardGame.model.Game;
import cardGame.view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){
        Game maumau = new Game();

        JFrame frame = new JFrame("Maumau!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(new ButtonBar(maumau));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.add(new GamePanel(maumau));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen.
        frame.setVisible(true);
    }
}