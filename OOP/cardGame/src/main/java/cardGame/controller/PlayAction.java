package cardGame.controller;

import cardGame.model.Game;
import cardGame.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

// If the card to be played is a legal move, card can be played.
public class PlayAction extends AbstractAction implements Observer {
    private Game maumau;
    private Player player;

    // Card cannot be played when no plays left or illegal move
    private void fixEnabled() {
        if(player.getHand().isEmpty() ||
                player.getPlaysLeft() <= 0 || !maumau.legalMove(player.getHand().top()))
            setEnabled(false);
        else
            setEnabled(true);
    }

    public PlayAction(Game maumau) {
        super("Play card");
        this.maumau = maumau;
        player = maumau.getPlayer(0);
        player.addObserver(this);
        player.getHand().addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { player.playCard();}

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}
