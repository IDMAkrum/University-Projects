package nl.rug.oop.introduction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by IDM Akrum on 28/04/2017.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 42L;
    private ArrayList<Item> inventory = new ArrayList<Item>();     //inventory array
    private Room location;

    public Player() {
    }

    //Sets the current location of the player.
    public void setLocation(Room currentLocation) {
        location = currentLocation;
    }

    //Return player location
    public Room getLocation() {
        return location;
    }

    //replaces inventory with new inventory
    public void newInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    //adds an item to the inventory
    public void addToInventory(Item someItem) {
        inventory.add(someItem);
    }

    //removes an item to the inventory
    public void removeFromInventory(Item someItem) {
        try {
            inventory.remove(someItem);
        } catch(NullPointerException e){
            System.err.print("Invalid key pointer: " + e + "\n");
        }
    }

    //look through player's inventory for item
    public boolean inInventory(Item target) {
        boolean found = false;
        for(int i = 0; i < inventory.size(); i++){
            if (inventory.get(i) == target) {
                found = true;
                break;
            }
        }
        return found;
    }

    //allows player to choose a door
    public void chooseDoors() {
        location.printDoors();
        System.out.println("Which door do you want to take? (-1: stay here)");
        int choice = -1;
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose an action.");
        }
        choice = sc.nextInt();
        if(choice == -1) {
            System.out.println("You remain here.");
        } else if(choice < location.getDoorsSize() && choice >= 0) {
            //if statement to check if door exists
            location.getDoor(choice).interact(this);
        } else {
            System.out.println("That door doesn't exist!");
        }
    }

    //allows player to take items from room
    public void takeItems() {
        if(location.printItems()) {
            System.out.println("Which item do you want to take? (-1: take none)");
            int choice = -1;

            Scanner sc = new Scanner(System.in);
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Please choose an action.");
            }
            choice = sc.nextInt();
            if (choice == -1) {
                System.out.println("You take none of the items.");
            } else if (choice < location.getItemsSize() && choice >= 0) {
                //adds item to inventory, but also removes it from room
                location.getItem(choice).interact(this);
                location.removeItem(location.getItem(choice));
            } else {
                System.out.println("That item doesn't exist!");
            }
        }
    }

    //allows player to choose which NPC to interact with
    public void chooseNPC() {
        if(location.printNPCS()) {
            System.out.println("Who do you want to interact with? (-1: no one.)");
            int choice = -1;

            Scanner sc = new Scanner(System.in);
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Please choose an action.");
            }
            choice = sc.nextInt();
            if (choice == -1) {
                System.out.println("You don't talk to anyone.");
            } else if (choice < location.getNPCsSize() && choice >= 0) {
                location.getNPC(choice).interact(this);
            } else {
                System.out.println("That person doesn't exist!");
            }
        }
    }
}
