/*package diagtool.src.Model;
import java.io.*;

public class Command {
    public static void main(String[] args) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "ping 8.8.8.8");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
}*/