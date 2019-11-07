package View;

import Model.Option;
import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel {
    ArrayList<OptionButton> buttonList = new ArrayList<>();

    private static final int NUM_BUTTONS = 5;

    public ButtonPanel(){
        GridLayout layout = new GridLayout(NUM_BUTTONS,1);
        this.setLayout(layout);

        for(int i = 0; i < NUM_BUTTONS; i++){
            buttonList.add(new OptionButton("Button " + (i),i));
            this.add(buttonList.get(i));
        }
    }

    public void setButtons(Question q) {
        ArrayList<Option> options = q.getOptions();
        int optionCount = options.size();

        for (int i = 0; i < optionCount; i++) {
            buttonList.get(i).setText(options.get(i).getDescription());
            buttonList.get(i).setEnabled(true);
        }

        for (int i = optionCount; i < NUM_BUTTONS; i++) {
            buttonList.get(i).setText("");
            buttonList.get(i).setEnabled(false);
        }
    }

}