package cardGame.controller;

import cardGame.model.Game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

// Button which calls DrawAction
public class DrawButton extends JButton implements Observer {
    private Game maumau;
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_D);
        // Change message if draws are forced
        if(maumau.getForceDraws() > 0) { setToolTipText("Forced to draw " + maumau.getForceDraws()
                + " cards(or rebut forced draw)"); }
        else { setToolTipText("Draw a card from the deck"); }
    }

    public DrawButton(Game maumau) {
        super(new DrawAction(maumau));
        this.maumau = maumau;
        maumau.addObserver(this);
        setButtonProperties();
    }

    @Override
    public void update(Observable observed, Object message) { setButtonProperties(); }
}
