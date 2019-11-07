import Model.InferenceEngine;
import Model.Load;
import View.DiagFrame;
import java.io.File;

public class Main {
    public static File file = new File("log.txt");

    public static void main(String[] args) {
        DiagFrame frame = new DiagFrame();
        InferenceEngine.setFrame(frame);
        Load.load();
        InferenceEngine.run();
    }
}
