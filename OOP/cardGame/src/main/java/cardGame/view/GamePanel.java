package cardGame.view;

import cardGame.model.Game;
import cardGame.model.Card;
import cardGame.model.Player;
import cardGame.model.PlayerHand;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Observable;
import java.util.Observer;

/** View of Game */

public class GamePanel extends JPanel implements Observer {

    private static final int CARD_SPACING = 2; //pixels
    private Game maumau;

    /* Create new GamePanel */
    public GamePanel(Game maumau) {
        this.maumau = maumau;
        observe();
        setBackground(new Color(36, 126, 53));
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /** observe all player decks to keep amount of cards updated for AI, and
     * what cards the player has */
    private void observe() {
        maumau.addObserver(this);
        for(Player player : maumau.getAllPlayers()) {
            player.getHand().addObserver(this);
        }
    }

    /* Get the scaled spacing between edges and cards */
    private int getSpacing() {
        return (int) ((getHeight() * 20) / 600.0);
    }

    /** Get the scaled width of cards. Default height is 600, default
     * width is 436, and cards are scaled depending on which dimension limits
     * their relative dimensions */
    private int cardWidth() {
        if((getHeight() * 600.0) / (getWidth() * 436.0) <= 1.0)
            return (int) ((cardHeight() * 436.0) / 600.0);
        return (getWidth() - getSpacing() * 3) / 6;
    }

    /**
     * Get the scaled height of cards. Default height is 600, default
     * width is 436, and cards are scaled depending on which dimension limits
     * their relative dimensions
     */
    private int cardHeight() {
        if((getHeight() * 600.0) / (getWidth() * 436.0) > 1.0)
            return (int) ((cardWidth() * 600.0) / 436.0);
        return (getHeight() - getSpacing() * 2) / 4;
    }

    /* paint the areas all other painting elements are supposed to go (an overlay)
     * these are also the values used for posX and posY in the specific paint elements functions */
    private void paintAreas(Graphics g) {
        g.setColor(Color.yellow);
        /* deck position */
        g.drawRect(getWidth()/4 + 20, getHeight()/3 + 25, cardWidth(), cardHeight());
        /* discard pile position */
        g.drawRect(getWidth()/2 - getSpacing() - 25, getHeight()/3 + 25, cardWidth(), cardHeight());
        /* PlayerHand Hand position */
        g.drawRect(getWidth()/2 - getSpacing() - 25, getHeight()/2 + 110 + getSpacing(), cardWidth(), cardHeight());
        /* AI hands positions */
        g.drawRect(getWidth()/2 - getSpacing() - 25, getSpacing(), cardWidth(), cardHeight()); /* top */
        g.drawRect(getSpacing(), getHeight()/3 + 25, cardWidth(), cardHeight()); /* left */
        g.drawRect(getWidth() - getSpacing() - 120, getHeight()/3 + 25, cardWidth(), cardHeight()); /* right */
        g.setColor(Color.black);
    }

    /* paint the deck with depth depth, paint black rectangle if deck is empty */
    private void paintDeck(Graphics g) {
        int posX = getWidth() / 4 + 20;
        int posY;
        for (int depth = 0; depth < maumau.getDeck().size(); ++depth) {
            posY = getHeight() / 3 + 25 - CARD_SPACING * depth;
            g.drawImage(CardBackTextures.getTexture(CardBack.CARD_BACK_BLUE),
                posX, posY, cardWidth(), cardHeight(), this);
            g.drawRect(posX, posY, cardWidth(), cardHeight());
        }
        if(maumau.getDeck().isEmpty()) {
            posY = getHeight() / 3 + 25;
            g.drawRect(posX, posY, cardWidth(), cardHeight());
        }
    }

    /* paint discard pile */
    private void paintDiscardPile(Graphics g) {
        Card card = maumau.getDiscardPile().top();
        int posX = getWidth() / 2 - getSpacing() - 25;
        int posY = getHeight() / 3 + 25;
        g.drawImage(CardTextures.getTexture(card),
                    posX, posY, cardWidth(), cardHeight(), this);
        g.drawRect(posX, posY, cardWidth(), cardHeight());
    }

    /* paint player hand */
    private void paintPlayer(Graphics g) {
        PlayerHand hand = maumau.getPlayer(0).getHand();
        String cards = hand.size() + "x";
        Card card = hand.top();
        int posX = getWidth()/2 - getSpacing() - 25;
        int posY = getHeight()/2 + 110 + getSpacing();
        g.drawImage(CardTextures.getTexture(card),
                    posX, posY, cardWidth(), cardHeight(), this);
        /* makes player's outline yellow if it's the player's turn */
        if(maumau.getTurn() == 0) {
            g.setColor(Color.yellow);
        } else { g.setColor(Color.black); }
        g.drawRect(posX, posY, cardWidth(), cardHeight());
        /* if deck isn't empty, print amount of cards next to cards */
        g.setColor(Color.yellow);
        if(hand.size() > 0) { g.drawString(cards, getWidth()/2 + 60, getHeight()/2 + 250); }
        g.setColor(Color.black);
    }

    /* paint AI1 at position left (cards hidden from PlayerHand) */
    private void paintAI1(Graphics g) {
        PlayerHand hand = maumau.getPlayer(1).getHand();
        String cards = hand.size() + "x";
        int posX = getSpacing();
        int posY = getHeight()/3 + 25;
        if(!hand.isEmpty()) {
            g.drawImage(CardBackTextures.getTexture(CardBack.CARD_BACK_BLUE),
                    posX, posY, cardWidth(), cardHeight(), this);
        }
        /* makes player's outline yellow if it's the player's turn */
        if(maumau.getTurn() == 1) {
            g.setColor(Color.yellow);
        } else { g.setColor(Color.black); }
        g.drawRect(posX, posY, cardWidth(), cardHeight());
        /* if deck isn't empty, and opponent has less than 6 cards left, print amount of cards next to cards */
        g.setColor(Color.yellow);
        if(hand.size() < 6 && hand.size() > 0) {
            g.drawString(cards, getSpacing() + 100, getHeight()/3 + 148); }
        g.setColor(Color.black);
    }

    /* paint AI2 at position top (cards hidden from PlayerHand) */
    private void paintAI2(Graphics g) {
        PlayerHand hand = maumau.getPlayer(2).getHand();
        String cards = hand.size() + "x";
        int posX = getWidth()/2 - getSpacing() - 25;
        int posY = getSpacing();
        if(!hand.isEmpty()) {
            g.drawImage(CardBackTextures.getTexture(CardBack.CARD_BACK_BLUE),
                    posX, posY, cardWidth(), cardHeight(), this);
        }
        /* makes player's outline yellow if it's the player's turn */
        if(maumau.getTurn() == 2) {
            g.setColor(Color.yellow);
        } else { g.setColor(Color.black); }
        g.drawRect(posX, posY, cardWidth(), cardHeight());
        /* if deck isn't empty, and opponent has less than 6 cards left, print amount of cards next to cards */
        g.setColor(Color.yellow);
        if(hand.size() < 6 && hand.size() > 0) {
            g.drawString(cards, getWidth()/2 + 57, getSpacing() + 123); }
        g.setColor(Color.black);
    }

    /* paint AI3 at position right (cards hidden from PlayerHand) */
    private void paintAI3(Graphics g) {
        PlayerHand hand = maumau.getPlayer(3).getHand();
        String cards = hand.size() + "x";
        int posX = getWidth() - getSpacing() - 120;
        int posY = getHeight()/3 + 25;
        if(!hand.isEmpty()) {
            g.drawImage(CardBackTextures.getTexture(CardBack.CARD_BACK_BLUE),
                    posX, posY, cardWidth(), cardHeight(), this);
        }
        /* makes player's outline yellow if it's the player's turn */
        if(maumau.getTurn() == 3) {
            g.setColor(Color.yellow);
        } else { g.setColor(Color.black); }
        g.drawRect(posX, posY, cardWidth(), cardHeight());
        /* if deck isn't empty, and opponent has less than 6 cards left, print amount of cards next to cards */
        g.setColor(Color.yellow);
        if(hand.size() < 6 && hand.size() > 0) {
            g.drawString(cards, getWidth() - getSpacing() - 20, getHeight()/3 + 148); }
        g.setColor(Color.black);
    }

    /* paint statement that paints all decks for players */
    private void paintAllPlayers(Graphics g) {
        paintPlayer(g);
        paintAI1(g);
        paintAI2(g);
        paintAI3(g);
    }

    /* Paint the items that this class alone is responsible for. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintAreas(g);
        paintDeck(g);
        paintDiscardPile(g);
        paintAllPlayers(g);
    }

    /* Tell GamePanel that the object it displays has changed */
    @Override
    public void update(Observable observed, Object message) { repaint(); }
}
