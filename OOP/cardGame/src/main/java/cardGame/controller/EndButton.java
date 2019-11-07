package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;
import java.awt.event.KeyEvent;

// Button that the player can click to end their turn
public class EndButton extends JButton {

    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_ENTER);
        setToolTipText("End turn");
    }

    public EndButton(Game maumau) {
        super(new EndAction(maumau));
        setButtonProperties();
    }

}
