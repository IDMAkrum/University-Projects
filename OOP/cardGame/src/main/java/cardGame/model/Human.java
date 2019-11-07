package cardGame.model;

import javax.swing.*;

/* Class for determining the behaviour of the human player (created by Ivana on 22/05/2017). */
public class Human extends Player {

    public Human(Game maumau) {
        this.maumau = maumau;
    }

    /* play is done through controller, save from dealing with forced draws, and skipping a turn */
    @Override
    public void play() {
        makeDrawsLeft();
        if(skip) {
            System.out.println("Skipped turn");
            endTurn();
        }
    }

    /* allows player to change suit if jack or joker is played */
    @Override
    protected void callSuit() {
        String response = JOptionPane.showInputDialog(null,
                "What suit do you want to call? (hearts, diamonds, clubs or spades)",
                "Call a suit!",
                JOptionPane.QUESTION_MESSAGE);

        switch (response) {
            case "hearts": maumau.setCallSuit(Card.Suit.HEARTS);
            break;
            case "diamonds": maumau.setCallSuit(Card.Suit.DIAMONDS);
            break;
            case "clubs": maumau.setCallSuit(Card.Suit.CLUBS);
            break;
            case "spades": maumau.setCallSuit(Card.Suit.SPADES);
            break;
            /* keep calling function until valid response is given */
            default: callSuit();
            break;
        }
    }
}
