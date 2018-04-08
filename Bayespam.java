/** @author: Ivana Akrum (s2861348)
 *  @author: Eline Hageman (s2909073)
 *  @organisation: University of Groningen 
 *  @input: files with .msg extension separated into spam and normal folders. 
 *  @description: Classifies messages into spam and normal through Bayesian learning. 
 * */ 
import java.io.*;
import java.util.*;
import java.lang.*;

public class Bayespam
{
    // This defines the two types of messages we have.
    static enum MessageType
    {
        NORMAL, SPAM
    }

    // This a class with two counters (for regular and for spam)
    static class Multiple_Counter
    {
        int counter_spam    = 0;
        int counter_regular = 0;

        // Increase one of the counters by one
        public void incrementCounter(MessageType type)
        {
            if ( type == MessageType.NORMAL ){
                ++counter_regular;
            } else {
                ++counter_spam;
            }
        }
    }

    // Listings of the two subdirectories (regular/ and spam/)
    private static File[] listing_regular = new File[0];
    private static File[] listing_spam = new File[0];

    // A hash table for the vocabulary (word searching is very fast in a hash table)
    private static Hashtable <String, Multiple_Counter> vocab = new Hashtable <String, Multiple_Counter> ();

    
    // Add a word to the vocabulary
    private static void addWord(String word, MessageType type)
    {
        Multiple_Counter counter = new Multiple_Counter();

        if ( vocab.containsKey(word) ){                  // if word exists already in the vocabulary..
            counter = vocab.get(word);                  // get the counter from the hashtable
        }
        counter.incrementCounter(type);                 // increase the counter appropriately

        vocab.put(word, counter);                       // put the word with its counter into the hashtable
    }


    // List the regular and spam messages
    private static void listDirs(File dir_location)
    {
        // List all files in the directory passed
        File[] dir_listing = dir_location.listFiles();

        // Check that there are 2 subdirectories
        if ( dir_listing.length != 2 )
        {
            System.out.println( "- Error: specified directory does not contain two subdirectories.\n" );
            Runtime.getRuntime().exit(0);
        }

        listing_regular = dir_listing[1].listFiles();
        listing_spam    = dir_listing[0].listFiles();
    }

    
    // Print the current content of the vocabulary
    private static void printVocab()
    {
        Multiple_Counter counter = new Multiple_Counter();

        for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
        {   
            String word;
            
            word = e.nextElement();
            counter  = vocab.get(word);
            
            System.out.println( word + " | in regular: " + counter.counter_regular + 
                                " in spam: "    + counter.counter_spam);
        }
    }

	/**	This new function improves the vocabulary of the text, by
	*  transforming it to only lowercase letters. */
	private static String onlyLetters(String line)
	{
		line = line.toLowerCase();
		char[] tempLine = line.toCharArray();
		for(char x=0; x<tempLine.length; x++){
			/// If character isn't a letter, change to space for better tokenisation later
			if(Character.isLetter(tempLine[x]) == false){
				tempLine[x] = ' '; 
			}
		}
		String newLine = new String(tempLine);
		return newLine;
	}

    // Read the words from messages and add them to your vocabulary. The boolean type determines whether the messages are regular or not  
    private static void readMessages(MessageType type)
    throws IOException
    {
        File[] messages = new File[0];

        if (type == MessageType.NORMAL){
            messages = listing_regular;
        } else {
            messages = listing_spam;
        }
        
        for (int i = 0; i < messages.length; ++i)
        {
            FileInputStream i_s = new FileInputStream( messages[i] );
            BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
            String line;
            String newWord;
            
            while ((line = in.readLine()) != null)                      // read a line
            {
				line = onlyLetters(line);
                StringTokenizer st = new StringTokenizer(line);         // parse it into words
        
                while (st.hasMoreTokens())                  // while there are stille words left..
                {	
					newWord = st.nextToken();
					
					if(newWord.length() >= 4){
						addWord(newWord, type);                  // add them to the vocabulary
					}
                }
            }

            in.close();
        }
    }
   
   
	/// Calculate a priori class probability for normal and spam messages
	public static double priori (MessageType type){		
		double nReg = listing_regular.length;
		double nSpam = listing_spam.length;
		double nTotal = nReg + nSpam;
		double answer;
		if(type == MessageType.NORMAL){
			answer = nReg / nTotal;
		} else {
			answer = nSpam / nTotal;
		}
		return Math.log(answer);
	}
	
	/** Returns a hashmap of the class conditional probabilities and 
	* and its associated word */
	public static HashMap<String, double[]> classConditional ()
	{
		HashMap<String, double[]> probability = new HashMap<String, double[]>();
		double pReg, pSpam;
		double nReg = 0;
		double nSpam = 0;
		Set<String> words = vocab.keySet();
			
		/// Get total regular and total spam messages in vocabulary
		for (String word : words){
			nReg += vocab.get(word).counter_regular;
			nSpam += vocab.get(word).counter_spam;
		}
			
		/// Compute probability for each word in vocabulary
		for (String word : words){
			pReg = vocab.get(word).counter_regular / nReg;
			pSpam = vocab.get(word).counter_spam / nSpam;
			double[] p = new double[2];
			p[0] = pReg;
			p[1] = pSpam;
			for(int i = 0; i < 2; i++) {
				if(p[i] == 0.0) {
					p[i] = 1.0/(nReg + nSpam);
				}
				/// Transform probability to log probability
				p[i] =  Math.log(p[i]);
			}
			probability.put(word, p);
		}
			
		return probability;
	}
		
	/// Returns true if message is a spam message	
	public static boolean isSpam(File message, HashMap<String, double[]> probability, double logP_Reg, double logP_Spam)
	throws IOException 
	{
		double P_Msg_Reg = logP_Reg; /// + alpha
		double P_Msg_Spam = logP_Spam; /// + alpha
		/// Read Message
		FileInputStream i_s = new FileInputStream(message);
		BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
		String line;
		String newWord;
		while ((line = in.readLine()) != null)                      
		{
			line = onlyLetters(line);
			StringTokenizer st = new StringTokenizer(line);      
			while (st.hasMoreTokens())                 						 
			{	
				newWord = st.nextToken();
				/// Get word probability from probability hashmap and update message probability
				if(vocab.containsKey(newWord)){
					P_Msg_Reg += probability.get(newWord)[0];
					P_Msg_Spam += probability.get(newWord)[1];
				}
			}
		}
		in.close();
		return (P_Msg_Reg < P_Msg_Spam ? true : false);
	}
	
    public static void main(String[] args)
    throws IOException
    {
        // Location of the directory (the path) taken from the cmd line (first arg)
        File dir_location = new File( args[0] );
        
        // Check if the cmd line arg is a directory
        if ( !dir_location.isDirectory() )
        {
            System.out.println( "- Error: cmd line arg not a directory.\n" );
            Runtime.getRuntime().exit(0);
        }

        // Initialize the regular and spam lists
        listDirs(dir_location);

        // Read the e-mail messages
        readMessages(MessageType.NORMAL);
        readMessages(MessageType.SPAM);
        
 		/// Compute a priori probability 
        double logP_Reg = priori(MessageType.NORMAL);
		double logP_Spam = priori(MessageType.SPAM);
				
		/// Compute class conditional probability 
        HashMap<String, double[]> probability = classConditional();
        
        // Print out the hash table
		printVocab();
        
        /// Classify new messages from test set
		dir_location = new File( args[1] );
        // Initialize the regular and spam lists
        listDirs(dir_location);
        
        double counterSpamRight = 0;
        double counterSpamWrong = 0;
        double counterRegRight = 0;
        double counterRegWrong = 0;
        
        /// Regular Messages; wrongly classified if message is a spam message
        for(File message : listing_regular){
			if(isSpam(message, probability, logP_Reg, logP_Spam)){
				counterRegWrong++;
			} else {
				counterRegRight++;
			}
		}
				
		/// Spam Messages; wrongly classified is message is not a spam message
		for(File message : listing_spam){
			if(isSpam(message, probability, logP_Reg, logP_Spam)){
				counterSpamRight++;
			} else {
				counterSpamWrong++;
			}
		}
        
        /// Print final statistics
		System.out.println("\n---------------STATS---------------");
        System.out.println("pReg: " + logP_Reg + "\npSpam: " + logP_Spam);
        System.out.println("Class\t regular\t spam\t total\n-----------------------------------------\n");
        System.out.println("regular\t " + counterRegRight + "\t" + counterRegWrong + "\t" + listing_regular.length);
        System.out.println("spam\t " + counterSpamWrong + "\t" + counterSpamRight + "\t" + listing_spam.length);
		System.out.println("False accept rate: " + 100*(counterRegWrong/listing_regular.length)+ "%");
		System.out.println("False reject rate: " + 100*(counterSpamWrong/listing_spam.length) + "%");
		System.out.println("Overall accuracy: " + 100*((counterSpamRight+counterRegRight)/(listing_regular.length + listing_spam.length))+"%\n");
				
    }
}
