package nl.rug.oop.introduction;

import java.util.Scanner;

/**
 * Created by Ivana on 01/05/2017.
 */
public class Guard extends NPC {

    private Item bribe;
    private Item key;
    private String bribeSuccess;
    private String bribeFail;

    //extends normal NPCs with possession of a key and an item they can be bribed with to receive said key
    public Guard(String description, Item key, Item bribe) {
        super(description);
        this.key = key;
        this.bribe = bribe;
    }

    //Receive the guard's key if their bribe is in the inventory. Prints associated messages as well
    public void bribe(Player player) {
        if(player.inInventory(bribe)) {
            System.out.println(bribeSuccess);
            player.addToInventory(key);
            player.removeFromInventory(bribe);
        } else {
            System.out.println("You were ignored. If only you had something that could interest them.");
            System.out.println(bribeFail);
        }
    }

    //Adds dialogue to be said when a bribe is attempted
    public void addBribeDialogue(String success, String failure) {
        bribeSuccess = success;
        bribeFail = failure;
    }

    //overrides chooseInteraction of NPC to give the extra option for "give" i.e. bribe
    public int chooseInteraction() {
        System.out.println("What do you want to do? (-1: changed your mind)");
        System.out.println("(0) talk.");
        System.out.println("(1) give.");
        //read integer, unless there is none, then read String
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose an action.");
        }
        return sc.nextInt();
    }

    //NPCS can be talked to, Guard can also be bribed
    public void interact(Player player) {
        int choice = chooseInteraction();
        switch (choice) {
            case -1:
                break;
            case 0:
                while(choice != -1) {
                    choice = startConversation();
                }
                break;
            case 1:
                bribe(player);
                break;
            default:
                System.out.println("That's not an option though.");
            }
    }

}
