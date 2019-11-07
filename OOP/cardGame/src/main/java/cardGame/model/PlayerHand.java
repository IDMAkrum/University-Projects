package cardGame.model;

import java.util.ArrayList;
import java.util.Observable;

import cardGame.model.AbstractDeck;
import cardGame.model.Card;
import cardGame.model.Game;
import cardGame.util.*;

/* Class that controls the card the players have i.e. the player hand (created by marco on 16/05/2017). */


public class PlayerHand extends Observable implements Emptiable, Sized{
    private ArrayList<Card> cards;

    public PlayerHand() {
        cards = new ArrayList<Card>();
    }

    protected void draw(Game maumau, int number) {
        for(int i = 0; i < number; i++){
            if(!maumau.getDeck().isEmpty()) {
                cards.add(maumau.getDeck().draw());
            } else {
                maumau.reshuffle();
            }
        }
        setChanged();
        notifyObservers();
    }

    /* get size of deck */
    @Override
    public int size() { return cards.size(); }

    /* return boolean of whether deck is empty or not */
    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /* clear deck (i.e. make isEmpty true) */
    @Override
    public void empty() {
        cards.clear();
    }

    /* return top card i.e. card most recently drawn (so in practice
    * it's the bottom card) */
    public Card top() {
        if(!cards.isEmpty()){
            return cards.get(cards.size()-1);
        }
        return null;
    }

    public void reorder(boolean direction) {
        if(direction) {
            cards.add(cards.remove(0));
        } else {
            cards.add(0,cards.remove(cards.size()-1));
        }
        setChanged();
        notifyObservers();
    }

    protected void playCard(Game maumau){
        System.out.println("Played card " + cards.get(cards.size()-1));
        maumau.getDiscardPile().put(cards.remove(cards.size()-1));
        setChanged();
        notifyObservers();
    }

    protected boolean hasLegalMoves(Game maumau) {
        for(Card card : cards){
           if(maumau.legalMove(card)) {
               return true;
           }
        }
        return false;
    }

}
