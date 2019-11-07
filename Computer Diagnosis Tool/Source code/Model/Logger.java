package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Creates a log of the question and the option chosen by the user */
public class Logger {
    private static final String FILENAME = "log.txt";

    public void appendLog(Question q, Option o) {
        try(FileWriter fw = new FileWriter(FILENAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(q.getDescription());
            out.println(o.getDescription());
            out.print("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendLog(String string) {
        try(FileWriter fw = new FileWriter(FILENAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(string);
            out.print("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
