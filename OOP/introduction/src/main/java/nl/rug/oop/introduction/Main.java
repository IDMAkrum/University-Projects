import java.util.Scanner;

public class Main {
	// Reverse the string which has been given as the argument and return it.
	private static String reverse(String arg) {
		StringBuilder reverse = new StringBuilder();
		
		// Start at the end and add each letter to the newly created string.
		for (int idx = arg.length(); idx > 0; idx--)
			reverse.append(arg.charAt(idx - 1));
		return reverse.toString();
	}
	
	public static void main(String[] args) {
		// Print the reverse of all the input arguments.
		for(String argument : args)
			System.out.println(reverse(argument));
		
		// Create memory for memorizing the input lines.
		Memory mem = new Memory();
		
		// Scan for user input.
		Scanner scan = new Scanner(System.in);
		
		// For every new input, check memory and print appropriate response.
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if(mem.canRemember(line)) 						// If the program has seen the string before
				System.out.println("Pffft, everyone knows that!");
			else { 											// If the program has not seen the string before, print and remember
				System.out.println("You're so smart and intelligent!");
				mem.remember(line);
			}
		}
	}
}