package nl.rug.oop.introduction;

/**
 * Created by Ivana on 01/05/2017.
 */
public abstract class Item extends Inspectable implements Interactable {

    public Item(String description) {
        super(description);
    }

    //adds item to player inventory
    public void interact(Player player) {
        player.addToInventory(this);
    }

}
