package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;

/**
 * Panel with the buttons for the game controllers
 */
public class ButtonBar extends JMenuBar {

    /**
     * Create a new buttonpanel with all the necessary buttons
     */
    public ButtonBar(Game maumau) {
        add(new DrawButton(maumau));
        add(new PreviousButton(maumau));
        add(new NextButton(maumau));
        add(new PlayButton(maumau));
        add(new EndButton(maumau));
    }

}
