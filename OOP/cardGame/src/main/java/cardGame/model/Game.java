package cardGame.model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

// Represents the game of Game (created by marco on 16/05/2017).

public class Game extends Observable {

    private AbstractDeck deck;
    private DiscardPile discardPile;
    private ArrayList<Player> players;
    private int turn;
    private boolean reverse;
    private int forceDraws;
    private Card.Suit callSuit;


    private static AbstractDeck makeDeck() {
        AbstractDeck deck = new CompleteDeck();
        deck.shuffle();
        return deck;
    }

    public Game() {
        deck = makeDeck();
        discardPile = new DiscardPile();
        players = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            if(i == 0) { players.add(new Human(this));
            } else { players.add(new AI(this)); }
        }
        dealCards();
        discardPile.put(deck.draw());
        reverse = false;
        forceDraws = 0;
        turn = 0;
        callSuit = null;
        update();
        System.out.println("Turn: player " + turn);
    }

    /* turn functionality */
    protected void nextTurn() {
        if (deck.isEmpty()) { reshuffle(); }
        /* check if no one has won yet */
        winStatus();

        turn = getNextTurn();
        System.out.println("Turn: player " + turn);
        /* let observers know turn has changed */
        update();
        /* let current player play */
        playerTurn();
    }

    /* if deck is empty, makes new deck from discard pile */
    protected void reshuffle() {
        Card currentCard = discardPile.top();
        discardPile.remove();
        deck.addAll(discardPile.emptyPile());
        deck.shuffle();
        /* make new discard pile the top card of last discard pile */
        discardPile.put(currentCard);
    }

    protected void dealCards() {
        for(Player player : players) {
            if(deck.size() > 7) { player.getHand().draw(this, 7); }
        }
    }

    /* print message if a player has won */
    private void winStatus() {
        if(getPlayer(turn).getHand().isEmpty()) {
            if (turn == 0) {
                JOptionPane.showMessageDialog(null, "You won!");
            } else {
                JOptionPane.showMessageDialog(null, "You lost");
            }
            System.exit(0);
        }
    }

    private void playerTurn() {
        /* reset moves of player(turn) */
        players.get(turn).resetMoves();
        players.get(turn).play();
    }

    /* returns true if playing the card is allowed */
    public boolean legalMove(Card playCard){
        Card pileCard = discardPile.top();

        /* joker is always allowed */
        if (playCard.getFace() == Card.Face.JOKER) { return true; }

        /* jack is always allowed, unless cards still have to be drawn */
        if(playCard.getFace() == Card.Face.JACK && forceDraws == 0) { return true; }

        /* if a two was played, can only counter the two (or draw cards) */
        if (forceDraws > 0 && pileCard.getFace() == Card.Face.TWO) {
            return (playCard.getFace() == Card.Face.TWO);
        }

        /* if the suit was changed by jack, can only play a card from that suit */
        if (callSuit != null) {
            return callSuit == playCard.getSuit();
        }

        return (pileCard.getSuit() == playCard.getSuit() || pileCard.getFace() == playCard.getFace());
    }

    /* change the direction of the game flow */
    protected void changeReverse() {
        reverse = !reverse;
    }

    /* functions for controlling the forceful draws a two or joker result in */
    protected void addForceDraws(int n) {
        forceDraws += n;
        update();
    }

    public int getForceDraws() { return forceDraws; }

    protected void resetForceDraws() {
        forceDraws = 0;
        update();
    }

    /* functions for controlling the suit called after a jack or joker card */
    protected void setCallSuit(Card.Suit s) {
        callSuit = s;
        System.out.println("Called " + callSuit);
    }

    protected void resetCallSuit() { callSuit = null; }

    /* get methods for private variables in package */
    public AbstractDeck getDeck() { return deck; }

    public DiscardPile getDiscardPile() { return discardPile; }

    public Player getPlayer(int i) { return players.get(i); }

    public ArrayList<Player> getAllPlayers() { return players; }

    public int getTurn() { return turn; }

    /* determines and returns next turn */
    protected int getNextTurn() {
        int temp = turn;
        /* set next turn based on whether game is clockwise or counter-clockwise */
        if(!reverse) {
            temp = temp < (players.size() - 1) ? (temp + 1) : 0;
        } else {
            temp = temp > 0 ? (temp - 1) : (players.size() - 1);
        }
        return temp;
    }

    private void update() {
        setChanged();
        notifyObservers();
    }
}
