import java.util.HashSet;

public class Memory {
	
	private HashSet<String> memory;
	
	public Memory() {
		memory = new HashSet<>();
	}
	// If the string isn't in memory yet, add it.
	public void remember(String said) {
		if(!memory.contains(said))
			memory.add(said);
	}
	
	// If the string is in memory, return TRUE, otherwise FALSE.
	public boolean canRemember(String said) {
		return memory.contains(said);
	}
}
