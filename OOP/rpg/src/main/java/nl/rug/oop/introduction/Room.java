package nl.rug.oop.introduction;

import java.util.ArrayList;

/**
 * Created by IDM Akrum on 28/04/2017.
 */
public class Room extends Inspectable {

    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<NPC> npcs = new ArrayList<NPC>();

    //Rooms are initialised with a description of said Room.
    public Room(String description) {
        super(description);
    }

    //Add and remove functions for the arraylists
    public void addDoor(Door newDoor) {
        doors.add(newDoor);
    }

    public void addItem(Item newItem) {
        items.add(newItem);
    }

    public void addNPC(NPC person) {
        npcs.add(person);
    }

    public void removeItem(Item oldItem) {
        try{
            items.remove(oldItem);
        }catch(NullPointerException e){
            System.err.print("Invalid item pointer: " + e + "\n");
        }
    }

    public void removeNPC(NPC ghosted) {
        try{
            npcs.remove(ghosted);
        }catch(NullPointerException e){
            System.err.print("Invalid npc pointer: " + e + "\n");
        }
    }

    //print function for the arraylists
    public void printDoors() {
        if(doors.size() == 0){
            System.out.println("There are no doors in this room.");
            return;
        }
        System.out.println("You look around for doors. You see:");
        for(int i = 0; i < doors.size(); i++){
            try{
                System.out.print("(" + i + ") ");
                doors.get(i).inspect();
            }catch(NullPointerException e){
                System.err.print("Invalid door pointer: " + e + "\n");
            }
        }
    }

    //print items and print npcs has extra success check for further handling of items and npcs
    public boolean printItems() {
        boolean success = false;
        if(items.size() == 0){
            System.out.println("Nothing to see here...");
            return success;
        }
        System.out.println("You look around for something useful. You see:");
        for(int i = 0; i < items.size(); i++){
            success = true;
            try {
                System.out.print("(" + i + ") ");
                items.get(i).inspect();
            }catch(NullPointerException e){
                System.err.print("Invalid item pointer: " + e + "\n");
            }

        }
        return success;
    }

    public boolean printNPCS() {
        boolean success = false;
        if(npcs.size() == 0){
            System.out.println("There is nobody around here...");
            return success;
        }
        System.out.println("You look around for some company. You see:");
        for(int i = 0; i < npcs.size(); i++){
            success = true;
            try {
                System.out.print("(" + i + ") ");
                npcs.get(i).inspect();
            }catch(NullPointerException e){
                System.err.print("Invalid NPC pointer: " + e + "\n");
            }
        }
        return success;
    }

    //gives size of different arraylists
    public int getDoorsSize() {
        return doors.size();
    }

    public int getItemsSize() {
        return items.size();
    }

    public int getNPCsSize() {
        return npcs.size();
    }

    //returns object in arraylist at index choice
    public Door getDoor(int choice) {
        return doors.get(choice);
    }

    public Item getItem(int choice) {
        return items.get(choice);
    }

    public NPC getNPC(int choice) {
       return npcs.get(choice);
    }

}
