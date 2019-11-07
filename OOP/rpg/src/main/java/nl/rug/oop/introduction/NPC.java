package nl.rug.oop.introduction;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by marco on 01/05/2017.
 */
public abstract class NPC extends Inspectable implements Interactable {

    private ArrayList<Dialogue> conversation = new ArrayList<Dialogue>();

    public NPC(String description){
        super(description);
    }

    //adds a new dialogue to the conversation arraylist
    public void addConversation(String question, String response) {
        conversation.add(new Dialogue(question, response));
    }

    //remove a specific dialogue from the conversation arraylist based on index
    public void removeConversation(int index) {
        conversation.remove(index);
    }

    //removes all dialogues from the conversation arraylist
    public void resetConversation() {
        conversation.clear();
    }

    //gives dialogue options stored in questions, then gives associated response to question
    public int startConversation() {
        if(conversation.size() == 0){
            System.out.println("This person has nothing to say...");
        } else {
            System.out.println("What do you want to say? (-1: never mind...)");
            for (int i = 0; i < conversation.size(); i++) {
                System.out.print("(" + i + ") ");
                try {
                    System.out.println(conversation.get(i).getQuestion());
                } catch (NullPointerException e) {
                    System.err.print("Invalid question pointer: " + e + "\n");
                }
            }
            Scanner sc = new Scanner(System.in);
            //if there is no int, accept String
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Please choose an action.");
            }
            //the key for the response value is the same as key of question
            int choice = sc.nextInt();
            if (choice != -1) {
                try {
                    System.out.println(conversation.get(choice).getResponse());
                } catch (NullPointerException e) {
                    System.err.print("Invalid response pointer: " + e + "\n");
                }
            }
            return choice;
        }
        return -1;
    }

    //prints choices available for NPCS and returns user's choice
    public int chooseInteraction() {
        System.out.println("What do you want to do? (-1: changed your mind)");
        System.out.println("(0) talk.");
        //read integer, unless there is none, then read String
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose an action.");
        }
        return sc.nextInt();
    }

    //NPCS can be talked to
    public void interact(Player player) {
        int choice = chooseInteraction();
        switch (choice) {
            case -1:
                break;
            case 0:
                //conversation can be kept on-going as long as desired
                while(choice != -1) {
                    choice = startConversation();
                }
                break;
            default:
                System.out.println("That's not an option though.");
        }
    }
}
