package nl.rug.oop.introduction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivana on 08/05/2017. Stores all values about the world we want to save
 * in class-relevant arraylists.
 */
public class WorldState implements Serializable {
    private static final long serialVersionUID = 42L;
    private ArrayList<Room> allRooms = new ArrayList<Room>();
    private ArrayList<Person> allPeople = new ArrayList<Person>();
    private ArrayList<Guard> allGuards = new ArrayList<Guard>();
    private ArrayList<Conditional> allConditions = new ArrayList<Conditional>();

    public WorldState(ArrayList<Room> rooms, ArrayList<Person> people, ArrayList<Guard> guards, ArrayList<Conditional> conditions) {
        allRooms = rooms;
        allPeople = people;
        allGuards = guards;
        allConditions = conditions;
    }

    //get and set methods for all arraylists
    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    public ArrayList<Person> getAllPeople() {
        return allPeople;
    }

    public ArrayList<Guard> getAllGuards() {
        return allGuards;
    }

    public ArrayList<Conditional> getAllConditions() {
        return allConditions;
    }


    public void setAllRooms(ArrayList<Room> rooms) {
        allRooms = rooms;
    }

    public void setAllPeople(ArrayList<Person> people) {
        allPeople = people;
    }

    public void setAllGuards(ArrayList<Guard> guards) {
        allGuards = guards;
    }

    public void setAllConditions(ArrayList<Conditional> conditions) {
        allConditions = conditions;
    }


}
