package cardGame.controller;

import cardGame.model.Game;
import cardGame.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

// Puts the previous card on top of the players hand so we can see it in the panel
public class PreviousAction extends AbstractAction implements Observer {
    private Game maumau;
    private Player player;

    private void fixEnabled() {
        if(player.getHand().isEmpty())
            setEnabled(false);
        else
            setEnabled(true);
    }

    public PreviousAction(Game maumau) {
        super("<<");
        this.maumau = maumau;
        player = maumau.getPlayer(0);
        player.addObserver(this);
        player.getHand().addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { player.getHand().reorder(false); }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}
