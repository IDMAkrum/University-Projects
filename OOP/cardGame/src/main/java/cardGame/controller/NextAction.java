package cardGame.controller;

import cardGame.model.Game;
import cardGame.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

// Puts the next card on top of the players hand so we can see it in the panel
public class NextAction extends AbstractAction implements Observer {
    private Game maumau;
    private Player player;


    private void fixEnabled() {
        if(player.getHand().isEmpty())
            setEnabled(false);
        else
            setEnabled(true);
    }

    public NextAction(Game maumau) {
        super(">>");
        this.maumau = maumau;
        player = maumau.getPlayer(0);
        player.addObserver(this);
        player.getHand().addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { player.getHand().reorder(true); }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}
