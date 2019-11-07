package View;

import Controller.OptionButtonClicked;

import javax.swing.*;

public class OptionButton extends JButton {
    private int id;
    public OptionButton(String text,int id) {
        super(text);
        this.id = id;
        this.addActionListener(new OptionButtonClicked(id));
    }

    public int getId() {
        return id;
    }

}
