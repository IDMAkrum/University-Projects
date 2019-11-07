package cardGame.controller;

import cardGame.model.Game;
import cardGame.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

// Action which ends the turn of a player.
public class EndAction extends AbstractAction implements Observer {
    private Game maumau;
    private Player player;

    // Turn can only be ended when no draws and plays left
    private void fixEnabled() {
        if (player.getPlaysLeft() < 0 || player.getPlaysLeft() > 0 && player.getDrawsLeft() > 0) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

    public EndAction(Game maumau) {
        super("End turn");
        this.maumau = maumau;
        player = maumau.getPlayer(0);
        player.addObserver(this);
        player.getHand().addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { player.endTurn(); }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}
