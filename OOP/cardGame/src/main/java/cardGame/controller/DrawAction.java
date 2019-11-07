package cardGame.controller;

import cardGame.model.Game;
import cardGame.model.Player;
import cardGame.model.PlayerHand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

// Makes the player draw a card from the deck.
public class DrawAction extends AbstractAction implements Observer {
    private Game maumau;
    private Player player;

    // Player cannot draw a card when deck is empty or no draws or plays left
    private void fixEnabled() {
        if(maumau.getDeck().isEmpty() || player.getDrawsLeft() == 0 || player.getPlaysLeft() <= 0)
            setEnabled(false);
        else
            setEnabled(true);
    }

    public DrawAction(Game maumau) {
        super("Draw a card");
        this.maumau = maumau;
        player = maumau.getPlayer(0);
        player.addObserver(this);
        player.getHand().addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { player.draw(); }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}
