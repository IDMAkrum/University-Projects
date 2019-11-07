package cardGame.model;

import java.util.Observable;

/* Abstract Player class with functions both subclass use (created by Ivana on 22/05/2017). */
public abstract class Player extends Observable {
    protected PlayerHand hand;
    protected boolean skip;
    protected Game maumau;
    protected int playsLeft;
    private int drawsLeft;

    public Player() {
        hand = new PlayerHand();
        skip = false;
        playsLeft = 1;
        drawsLeft = 1;
    }

    public PlayerHand getHand() { return hand; }

    /* if player was forced to make draws, update their drawsLeft */
    protected void makeDrawsLeft() {
        if (maumau.getForceDraws() > 0) {
            drawsLeft = maumau.getForceDraws();
        }
    }

    /* end player turn */
    public void endTurn(){
        if(!skip) { System.out.println("Finished turn"); }
        else { skip = false; }
        /* make playsLeft an invalid amount to keep the endTurn button greyed while its not player's turn */
        playsLeft = -1;
        update();
        /* signal game to call next player's turn */
        maumau.nextTurn();
    }

    /* reset drawsLeft and playsLeft */
    protected void resetMoves() {
        playsLeft = 1;
        drawsLeft = 1;
        update();
    }

    /* play a card from hand */
    public void playCard() {
        hand.playCard(maumau);
        playsLeft--;
        maumau.resetCallSuit();
        /* see if the card played has a special effect */
        checkEffect();
        update();
    }

    /* draws as many cards as there are draws left */
    public void draw() {
        hand.draw(maumau, drawsLeft);
        System.out.println("Drew " + drawsLeft + " card(s)");
        drawsLeft--;
        if (maumau.getForceDraws() > 0) {
            resetMoves();
            maumau.resetForceDraws();
            if (maumau.getDiscardPile().top().getFace() == Card.Face.JOKER) {
                callSuit();
            }
            if (maumau.getDiscardPile().top().getFace() == Card.Face.TWO) {
                playsLeft--;
                drawsLeft--;
            }
        }
        update();
    }

    /* deals with the effects of special cards (see gamerules.txt) */
    private void checkEffect() {
        Card card = maumau.getDiscardPile().top();
        if(card.getFace() == Card.Face.SEVEN || card.getFace() == Card.Face.KING) {
            playsLeft++;
        }
        if(card.getFace() == Card.Face.ACE) {
            maumau.changeReverse();
        }
        if(card.getFace() == Card.Face.EIGHT) {
            int turn = maumau.getNextTurn();
            maumau.getPlayer(turn).mustSkip();
        }
        if(card.getFace() == Card.Face.TWO) {
            maumau.addForceDraws(2);
        }
        if(card.getFace() == Card.Face.JACK) {
            callSuit();
        }
        if(card.getFace() == Card.Face.JOKER) {
            maumau.addForceDraws(5);
        }
    }

    /* sets skip to true (meant to be used by other players */
    private void mustSkip() { skip = true; }

    /* get functions for class variables */
    public int getPlaysLeft() { return playsLeft; }

    public int getDrawsLeft() { return drawsLeft; }

    public abstract void play();

    protected abstract void callSuit();

    private void update() {
        setChanged();
        notifyObservers();
    }
}
