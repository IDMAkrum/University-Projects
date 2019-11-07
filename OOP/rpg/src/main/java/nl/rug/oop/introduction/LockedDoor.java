package nl.rug.oop.introduction;

/**
 * Created by Ivana on 01/05/2017.
 */
public class LockedDoor extends Door {

    private Item key;

    public LockedDoor(String description, Room room1, Room room2, Item key){
        super(description, room1, room2);
        this.key = key;
    }

    public void interact(Player player) {
        System.out.print("The door is locked!");
        if(player.inInventory(key)) {
            System.out.println(" Luckily you have the key.");
            System.out.println("You go through the door.");
            if(player.getLocation() == this.assumedLocation()) {
                player.setLocation(this.assumedDestination());
            } else {
                player.setLocation(this.assumedLocation());
            }
        } else {
            System.out.printf("%n");
            System.out.println("You don't have the key! Try again when you do.");
        }
    }

}
