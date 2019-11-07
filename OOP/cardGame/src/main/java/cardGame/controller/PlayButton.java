package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;
import java.awt.event.KeyEvent;
// Button that puts the card on the discard pile
public class PlayButton extends JButton {

    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_SPACE);
        setToolTipText("Play card");
    }

    public PlayButton(Game maumau) {
        super(new PlayAction(maumau));
        setButtonProperties();
    }

}
