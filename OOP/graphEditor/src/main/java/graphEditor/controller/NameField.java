package graphEditor.controller;

import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * Created by Ivana on 18/06/2017. Namefield is a text field that overlaps the currently selected
 * vertex, hidden and not editable unless vertex is double clicked.
 */
public class NameField extends JTextField implements ActionListener {
    private GraphVertex vertex;

    public NameField(GraphVertex vertex) {
        super(vertex.getName());
        this.vertex = vertex;
        Rectangle rect = vertex.getRect();
        this.setBounds(rect.x, rect.y, rect.width+1, rect.height+1);
        this.addActionListener(this);
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        this.setForeground(Color.BLUE);
        this.setVisible(false);
        this.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String newname = this.getText();
        if(newname.length() < 20) {
            vertex.setName(newname);
        }
        this.setVisible(false);
        this.setEditable(false);
    }

}
