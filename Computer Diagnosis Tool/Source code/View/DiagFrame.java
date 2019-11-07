package View;

import Model.Question;

import javax.swing.*;
import java.awt.*;

public class DiagFrame extends JFrame {

    public QuestionPanel questionPanel;
    public ButtonPanel buttonPanel;

    public DiagFrame() {
        // Initialize frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Computer Diagnostics Tool");

        // Set size and restrict resizing
        this.setSize(600,400);
        this.setResizable(false);

        // Set location in center of screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPos = (dim.width/2 - this.getWidth()/2);
        int yPos = (dim.height/2 - this.getHeight()/2);
        this.setLocation(xPos,yPos);

        this.setLayout(new BorderLayout());

        this.questionPanel = new QuestionPanel();
        this.add(this.questionPanel, BorderLayout.CENTER);

        // Add answer buttons
        this.buttonPanel = new ButtonPanel();
        this.add(this.buttonPanel,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void askQuestion(Question q) {
        questionPanel.setText(q);
        buttonPanel.setButtons(q);
    }

}