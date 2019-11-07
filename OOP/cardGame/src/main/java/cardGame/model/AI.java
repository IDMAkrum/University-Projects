package cardGame.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Class represents the behaviour of the AI players (created by Ivana on 22/05/2017).
 */

public class AI extends Player {
    private Timer timer;

    public AI(Game maumau) {
        this.maumau = maumau;
    }

    public void play() {
        makeDrawsLeft();
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                boolean done = false;
                /* player skips turn */
                if(skip) {
                    System.out.println("Skipped turn");
                    done = true;
                }

                /* player plays one (or more) card(s), draws if they can't play (have no legal moves in hand) */
                while(!done) {
                    if (hand.hasLegalMoves(maumau)) {
                        pickCard();
                        if (playsLeft == 0) { done = true; }
                    }
                    else {
                        draw();
                        done = true;
                    }
                }

                timer.stop();
                endTurn();
            }
        };
        /* player takes 1 second (1000 milliseconds) for the actionPerformed() */
        timer = new Timer(1500, al);
        timer.start();
    }

    /* player picks a card from their hand. Player will pick the first legal move they come across. */
    private void pickCard() {
        if (maumau.legalMove(hand.top())) {
            playCard();
        } else {
            hand.reorder(true);
        }
    }

    /* player randomly calls a new suit when it plays a jack or joker card */
    @Override
    protected void callSuit() {
        int rand = new Random().nextInt(4);
        Card.Suit suit;
        switch (rand) {
            case 0: suit = Card.Suit.HEARTS;
            break;
            case 1: suit = Card.Suit.DIAMONDS;
            break;
            case 2: suit = Card.Suit.CLUBS;
            break;
            case 3: suit = Card.Suit.SPADES;
            break;
            default: suit = null;
        }
        maumau.setCallSuit(suit);
        JOptionPane.showMessageDialog(null, "Player " + maumau.getTurn() + " called " + suit);
    }
}
