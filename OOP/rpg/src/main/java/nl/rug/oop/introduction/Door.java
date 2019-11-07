package nl.rug.oop.introduction;

/**
 * Created by Ivana on 01/05/2017.
 */
public abstract class Door extends Inspectable implements Interactable {
    private Room location, destination;

    public Door(String description, Room room1, Room room2) {
        super(description);
        this.location = room1;
        this.destination = room2;
    }

    //returns room assumed to be the current location
    public Room assumedLocation() {
        return location;
    }

    //return room assumed to be the current destination
    public Room assumedDestination() {
        return destination;
    }

    //two-way door, player's location will become whichever room the player is not currently in
    public void interact(Player player) {
        System.out.println("You go through the door.");
        if(player.getLocation() == location) {
            player.setLocation(destination);
        } else {
            player.setLocation(location);
        }
    }
}
