package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;
import java.awt.event.KeyEvent;

// Button that calls NextAction
public class NextButton extends JButton {

    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_N);
        setToolTipText("Next card");
    }

    public NextButton(Game maumau) {
        super(new NextAction(maumau));
        setButtonProperties();
    }

}
