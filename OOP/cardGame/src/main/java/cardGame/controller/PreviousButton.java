package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;
import java.awt.event.KeyEvent;
// Button that calls PreviousAction

public class PreviousButton extends JButton {

    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_P);
        setToolTipText("Previous card");
    }

    public PreviousButton(Game maumau) {
        super(new PreviousAction(maumau));
        setButtonProperties();
    }

}
