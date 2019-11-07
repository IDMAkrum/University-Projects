package View;

import Model.Question;

import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel{

    JLabel questionLabel;

    public QuestionPanel() {
        this.questionLabel = new JLabel("QUESTION TEXT",SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(questionLabel, BorderLayout.CENTER);
    }

    public void setText(Question q) {
        questionLabel.setText("<html><center>" + q.getDescription() + "</center></html>");
    }

}
