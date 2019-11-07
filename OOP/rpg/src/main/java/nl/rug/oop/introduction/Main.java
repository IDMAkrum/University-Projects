package nl.rug.oop.introduction;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //methods for printing introduction and ending
    private static void introduction() {
        System.out.println("Darkness. Light. Darkness. Light. You blink rapidly, alternating between seeing nothing and the by-then \n" +
                "familiar visage of your cell room. God, your head is pounding. What happened? The last thing you remember is poker night \n" +
                "with the guys...");
    }

    private static void ending(boolean tobyFree) {
        System.out.printf("%n");
        System.out.println("Exiting, the sun blinds you. You can feel a heat you'd missed on your face. The air is crisp, \n" +
                "and pleasant, not too humid, not too dry.");
        if(tobyFree) {
            System.out.printf("%n");
            System.out.println("You and Toby meet the new sun together. Though the world is most likely just as you left it,\n" +
                    "something is new, better. A part of you is still unsure, but the one thing you can count on is that Toby\n" +
                    "has your back. No... the world is the same, but you are a different man.");
        } else {
            System.out.printf("%n");
            System.out.println("You walk into the world, unsure of what it'll hold. You're not the same person you were when \n" +
                    "you were put in there, but whether you changed for the best...? Who could say? At least one thing is\ncertain:" +
                    " A free man again, at long last. The world is your oyster.");
        }
        System.out.printf("%n");
        System.out.printf("See you on the next journey!");
        System.out.printf("%n");
        System.out.println("The END.");
    }

    //methods for updating variables during runtime of game
    private static void updateGuard(Guard guard) {
        guard.resetConversation();
        guard.addConversation("So... coke, huh?", "\"Yeeeeeeep.\"");
        guard.addConversation("Thanks for the key, ye friendly guard!", "\"Fuck you.\"");
    }

    private static void confideInToby(Person toby) {
        toby.addConversation("I have this key...", "\"What?! Shit, holy- what? It's gotta be Johnny, man! Mate, listen to me, I " +
                "heard two guards talk, and I'm\npretty sure the key to the exit is in the security office! If you can get in there " +
                "somehow, you're good to go.\"\n\nInteresting... seems like you've overspent your time in your cell. You're about to" +
                " leave when Toby calls after you.\n\n\"Mate, the key to my cell's probably also in the SO, you're gonna get me out " +
                "of here too, right?\"\n\nIf you get to the security office, you can help Toby too... You keep this in mind.");
    }

    private static void haveTobysKey(Person toby) {
        //revert what happens in confideInToby method
        toby.removeConversation(4);
        toby.addConversation("I've got your cell key.", "\"That's great to hear, please let me out!\"");
    }

    private static void freeToby(Person toby, Room myCell, Room cellBlock, Room exit) {
        System.out.println("\nToby is standing there with the broadest smile.\n" +
                "\"Thanks, mate, I'm going to go ahead. See you at the entrance,\" he says before taking off.");
        myCell.removeNPC(toby);
        cellBlock.removeNPC(toby);
        exit.addNPC(toby);
        toby.resetConversation();
        toby.addConversation("Freedom awaits you!", "Toby hugs you, patting your back. \"Awaits us, mate, awaits us...\"");
    }

    private static void createDir(File saves) {
        boolean success = saves.mkdir();
        if (success) {
            //creating the directory succeeded
            System.out.println("Successfully created new folder for save games!");
        } else {
            //creating the directory failed
            System.out.println("Something went wrong with creating the directory for save games.");
        }
    }

    //save method
    private static void save(Player player, WorldState world, String saveName) {
        try {
            FileOutputStream fileOut = new FileOutputStream("SaveFiles" + File.separator + saveName + ".ser");
            ObjectOutputStream save = new ObjectOutputStream(fileOut);
            //saves player information and world information
            save.writeObject(player);
            save.writeObject(world);
            save.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //intermediate method for choosing a save file to load
    private static String chooseSave(File dir) {
        int choice = -1;
        String fileName = "", extension = "";

        System.out.println("Please choose a save file:");
        //gets all files in the directory and prints options
        File[] saveFiles = dir.listFiles();
        assert saveFiles != null;
        for(int i = 0; i < saveFiles.length; i++){
            fileName = saveFiles[i].getName();
            if(saveFiles[i].isFile()){
                System.out.println("(" + i + ") " + fileName);
            }
        }
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose a save file.");
        }
        choice = sc.nextInt();
        while((choice < 0) || (choice > saveFiles.length)) {
            System.out.println("Sorry, that's not a valid save file!");
            choice = sc.nextInt();
        }
        fileName = saveFiles[choice].getName();
        extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        //pick a valid save file
        if(!(extension.equals("ser")) || fileName.equals("")) {
            System.out.println("Sorry, something went wrong!");
            return "failure";
        }
        return fileName;
    }

    //load method
    private static void load(Player player, WorldState world, String saveName) {
        String path = ("SaveFiles" + File.separator + saveName);
        if(saveName.equals("quicksave")) {
            path = path.concat(".ser");
        }
        try {
            FileInputStream save = new FileInputStream(path);
            ObjectInputStream load = new ObjectInputStream(save);

            //loads the saved variables into current player
            Player newPlayer = (Player) load.readObject();
            player.setLocation(newPlayer.getLocation());
            player.newInventory(newPlayer.getInventory());

            //loads all saved WorldState variables into current world
            WorldState newWorld = (WorldState) load.readObject();
            world.setAllPeople(newWorld.getAllPeople());
            world.setAllGuards(newWorld.getAllGuards());
            world.setAllRooms(newWorld.getAllRooms());
            world.setAllConditions(newWorld.getAllConditions());

            load.close();
            save.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //method that exits the game
    private static void exitGame() {
        System.out.println("Are you sure you want to quit the game?");
        System.out.println("(0) Yes.");
        System.out.println("(1) No.");

        //read integer and return it
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose either yes or no.");
        }
        switch(sc.nextInt()) {
            case 1:
                break;
            default:
                System.out.printf("%n");
                System.out.println("Goodbye.");
                System.exit(0);
        }
    }

    //allows player to choose an action
    private static void chooseAction(Player player, WorldState world, File saveFiles) {
        System.out.println("What do you want to do?");
        System.out.println("(0) Look around.");
        System.out.println("(1) Look for a way out.");
        System.out.println("(2) Look for something useful.");
        System.out.println("(3) Look for company.");
        System.out.println("(4) QuickSave.");
        System.out.println("(5) QuickLoad.");
        System.out.println("(6) Save.");
        System.out.println("(7) Load.");
        System.out.println("(-1) Quit game.");


        //read integer and return it
        Scanner sc = new Scanner(System.in);
        while(!sc.hasNextInt()){
            sc.next();
            System.out.println("Please choose an action.");
        }
        switch(sc.nextInt()) {
            case -1:
                //exits
                exitGame();
                break;
            case 0:
                //prints description of current location
                player.getLocation().inspect();
                break;
            case 1:
                //displays door options and gives option to interact with one
                player.chooseDoors();
                break;
            case 2:
                //displays item in room and gives option to take one (or more)
                player.takeItems();
                break;
            case 3:
                //displays NPCs on site and allows interaction with them
                player.chooseNPC();
                break;
            case 4:
                //quicksave is save method with saveName quicksave
                save(player, world, "quicksave");
                break;
            case 5:
                //quickload is load method with saveName quicksave
                load(player, world, "quicksave");
                break;
            case 6:
                //allows player to save under an inputted name
                System.out.println("Please name your save file.");
                Scanner name = new Scanner(System.in);
                save(player, world, name.next());
                break;
            case 7:
                //loads relevant parts of save file
                String save = chooseSave(saveFiles);
                if(!save.equals("failure")){
                    load(player, world, save);
                }
                break;
            default:
                System.out.println("That's not an option though.");
        }
    }

    public static void main(String[] args) {
        //Initialisations
        Player player = new Player();
        Person toby = new Person("Your mate in the cell next to yours. Wild gambler.");
        Person johnny = new Person("A guard whom you've befriended.");
        Person cellGuard = new Person("A guard hovering around one of the cells. He seems preoccupied.");
        Room myCell = new Room("Your jail cell, there's not much here.");
        Room tobyCell = new Room("It looks identical to your own cell.");
        Room cellBlockTango = new Room("Cell Block Tango: It's a seemingly endless hallway filled with cells.");
        Room hallway = new Room("You're in a hallway. At the far end of it you can see the security office.");
        Room commonRoom = new Room("The common room. It's quiet this time of the day...");
        Room securityOffice = new Room("There are monitors everywhere. Despite the high security, there is only one guard.");
        Room prisonEntrance = new Room("The exit door stands tall. Though you know you can't escape, it draws you nearer.");
        Room freedom = new Room("The whole world lies at your feet.");
        Key cellKey = new Key("A heavy object in your pocket. It's a key!");
        Key tobyCellKey = new Key("This key opens Toby's cell.");
        Key exitKey = new Key("It's the key to freedom!");
        Key officeKey = new Key("The key to the security office.");
        Beverage coke = new Beverage("An ice cold can of Coca Cola.");
        Guard officeGuard = new Guard("A guard leaning against the wall.", officeKey, coke);
        NormalDoor toHall = new NormalDoor("A long hallway...", prisonEntrance, hallway);
        NormalDoor toCommonRoom = new NormalDoor("A door that's always open.", prisonEntrance, commonRoom);
        NormalDoor openDoor = new NormalDoor("A large, uninviting door. It is unlocked!", cellBlockTango, prisonEntrance);
        LockedDoor securityOfficeDoor = new LockedDoor("A door that says security office.", hallway, securityOffice, officeKey);
        LockedDoor exit = new LockedDoor("Big, flashing letters tell you this door is the EXIT.", prisonEntrance, freedom, exitKey);
        LockedDoor myCellDoor = new LockedDoor("A barred door with a rusted, old lock.", myCell, cellBlockTango, cellKey);
        LockedDoor tobyCellDoor = new LockedDoor("The door to Toby's cell.", cellBlockTango, tobyCell, tobyCellKey);
        Conditional bffs = new Conditional(false);
        Conditional gaveBribe = new Conditional(false);
        Conditional confideIn = new Conditional(false);
        Conditional tobyKey = new Conditional(false);

        //adds doors to rooms
        securityOffice.addDoor(securityOfficeDoor);
        prisonEntrance.addDoor(openDoor);
        prisonEntrance.addDoor(toHall);
        prisonEntrance.addDoor(toCommonRoom);
        prisonEntrance.addDoor(exit);
        hallway.addDoor(securityOfficeDoor);
        hallway.addDoor(toHall);
        commonRoom.addDoor(toCommonRoom);
        myCell.addDoor(myCellDoor);
        tobyCell.addDoor(tobyCellDoor);
        cellBlockTango.addDoor(myCellDoor);
        cellBlockTango.addDoor(tobyCellDoor);
        cellBlockTango.addDoor(openDoor);

        //adds Items and NPCS to rooms
        myCell.addItem(cellKey);
        myCell.addNPC(toby);
        hallway.addNPC(officeGuard);
        securityOffice.addItem(exitKey);
        securityOffice.addItem(tobyCellKey);
        securityOffice.addNPC(johnny);
        cellBlockTango.addNPC(toby);
        cellBlockTango.addNPC(cellGuard);
        commonRoom.addItem(coke);

        //adds conversations to NPCS
        toby.addConversation("Hey", "\"Finally up, sunshine? I figured you'd be sleeping till end of the week, Johnny gave you a pretty good beating.\"");
        toby.addConversation("What happened again?", "\"You don't remember? Losing heavy, downing the only good alcohol we managed to smuggle in?" +
                " You're a real trip.\nAfter drinking our precious vodka, you pissed off our guard pal Johnny, who beat you up pretty good.\"" +
                "\nRight, the beating, Johnny... It was starting to come back.");
        toby.addConversation("Who are you?", "\"Sorry? Did you get a concussion or something, mate? It's me: Toby. Friend, drinking body, occasionally" +
                " a\ngood listener even. You know what, I'll chock this up to your beating and forget you even asked me that.\"");
        toby.addConversation("How do I get out of here?", "\"Out of here? Mate, we're in prison. Get real.\"");

        officeGuard.addConversation("Hey!", "\"Hey? Don't 'hey' me, get back to your cell!\"");
        officeGuard.addConversation("Category: things I like to receive.", "\"Me? I loooove me some Coca Cola, it's my favourite and... Wait!\n" +
                "Don't play mind games with me, get back to your cell before I get really angry!\"");
        officeGuard.addBribeDialogue("\"Oooh, a can of Coca Cola. Now that's what makes a man smile!\nTake this key. You might need it... Just don't tell anyone.\"", "\"I sure am thirsty. Wish I had an ice cold can of coke,\" the guard mumbles to himself.");

        cellGuard.addConversation("Hey lil mamma, let me whisper in your ear.", "...is what you want to say. Better you don't.");
        cellGuard.addConversation("BOOOOH!!", "...is what you want to say, but wouldn't it be smarter to leave while he's preoccupied?");
        cellGuard.addConversation("What ya'll talking about?", "Siiigh. This is your inner voice speaking: please stop. Leave the guard be.");

        johnny.addConversation("Johnny?", "\"That's my name last I checked yes.\" Johnny looks you up and down. \"You look like shit.\"");
        johnny.addConversation("About my cell key...", "\"Oy, oy, oy, keep your voice down. Seriously.\" Johnny sighs deeply.");
        johnny.addConversation("Seriously though, this key...", "\"It's a nice fucking key, it'd be even nicer if you kept it in your pocket though.\"");
        johnny.addConversation("Love you, cuddle-wuddles", "\"I will cut you.\" He smiles wryly. \"Just get your ass out of here, bro.\"");

        //puts all things that can be changed during game runtime in class-relevant
        //arraylists. These are added to the WorldState which is saved when save method is called

        ArrayList<Person> people = new ArrayList<Person>();
        people.add(toby);

        ArrayList<Guard> guards = new ArrayList<Guard>();
        guards.add(officeGuard);

        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.add(myCell);
        rooms.add(tobyCell);
        rooms.add(cellBlockTango);
        rooms.add(hallway);
        rooms.add(commonRoom);
        rooms.add(prisonEntrance);
        rooms.add(securityOffice);

        ArrayList<Conditional> conditions = new ArrayList<Conditional>();
        conditions.add(bffs);
        conditions.add(confideIn);
        conditions.add(gaveBribe);
        conditions.add(tobyKey);

        //initializes world state that holds all the above arrays
        WorldState world = new WorldState(rooms, people, guards, conditions);
        File saves = new File("SaveFiles");

        //make the SaveFiles directory
        if(!(saves.isDirectory())){
            createDir(saves);
        }

        //Introduction
        player.setLocation(myCell);
        introduction();

        //Main gameplay, exits when you escape the prison
        while(player.getLocation() != freedom) {
            chooseAction(player, world, saves);

            //special dialogues
            //booleans in if-statements to make sure updates only happens once
            if(!(gaveBribe.getCondition()) && player.inInventory(officeKey)) {
                updateGuard(officeGuard);
                gaveBribe.setCondition(true);
            }

            //tell Toby you have your cell key (and can escape)
            if(!(confideIn.getCondition()) && player.inInventory(cellKey)) {
                confideInToby(toby);
                confideIn.setCondition(true);
            }

            //tell Toby you have his cell key
            if(!(tobyKey.getCondition()) && player.inInventory(tobyCellKey)) {
                haveTobysKey(toby);
                tobyKey.setCondition(true);
            }

            //free Toby event
            if (!(bffs.getCondition()) && player.getLocation() == tobyCell) {
                freeToby(toby, myCell, cellBlockTango, prisonEntrance);
                bffs.setCondition(true);
            }

        }

        //End
        ending(bffs.getCondition());
    }
}