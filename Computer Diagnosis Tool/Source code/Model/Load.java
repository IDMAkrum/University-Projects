package Model;

import java.io.*;
import java.util.ArrayList;

/** class that loads in the knowledge base */
public class Load {

    private static File file;

    /** read and process a goal **/
    private static void readGoal(BufferedReader br) throws IOException {
        /* get goal name */
        String line = br.readLine();
        Goal g = new Goal(line);

        /* get goal recommendations */
        while (!(line = br.readLine()).equals("end")) {
            String [] r = line.split("; ");
            KnowledgeBase.add(new Recommendation(r[0], r[1]));
        }

        /* add goal to knowledge base */
        KnowledgeBase.add(g);
    }

    /** read and process a rule **/
    private static void readRule(BufferedReader br) throws IOException {
        /* read first line (Conditions) */
        String line = br.readLine();
        ArrayList<Fact> conditions = new ArrayList<>();
        ArrayList<Fact> result = new ArrayList<>();

        /* get rule conditions */
        while (!(line = br.readLine().toLowerCase()).equals("result")) {
            String [] c = line.split("; ");
            conditions.add(new Fact(c[0], c[1]));
        }

        /* get rule results */
        while (!(line = br.readLine()).equals("end")) {
            String [] r = line.split("; ");
            result.add(new Fact(r[0], r[1]));
        }

        /* create rule with conditions and result */
        Rule rule = new Rule(conditions, result);

        /* add rule to knowledge base */
        KnowledgeBase.add(rule);
    }

    /** read and process a question **/
    private static void readQuestion(BufferedReader br) throws IOException {
        /* read question description */
        String line = br.readLine();
        Question q = new Question(line);

        /* read options header */
        line = br.readLine();

        /* get question options */
        while (!(line = br.readLine()).equals("end")) {
            String [] description = line.split(": ");
            line = br.readLine();
            String [] fact = line.split("; ");
            q.addOption(new Option(description[1], fact[0], fact[1]));
        }

        /* add question to knowledge base */
        KnowledgeBase.add(q);
    }

    private static void createLogFile() {
        file = new File("log.txt");
        KnowledgeBase.getLog().appendLog("-----------NEW RUN----------");
    }

    public static File getFile() { return file; }

    // Loads the knowledge base from the knowledge base text file
    public static void load() {
        try {
            FileReader fr = new FileReader("knowledgebase.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            // Get the type (goal, rule, or question)
            while(!(line = br.readLine()).equals("!")) {
                if (line.toLowerCase().equals("goal")) {
                    readGoal(br);
                }
                if (line.toLowerCase().equals("rule")) {
                    readRule(br);
                }
                if (line.toLowerCase().equals("question")) {
                    readQuestion(br);
                }
            }
            br.close();
            createLogFile();
        } catch (FileNotFoundException e) {
            System.err.println("Text file was not found. Failed to load knowledge base.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("An I/O error occurred. Failed to load knowledge base.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Text file is corrupted. Failed to load knowledge base.");
            e.printStackTrace();
        }
    }
}
