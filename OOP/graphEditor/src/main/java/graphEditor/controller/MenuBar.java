package graphEditor.controller;

import graphEditor.model.GraphModel;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar {
    public MenuBar(GraphModel graph) {
        JMenu file = new FileMenu(graph);
        file.setMnemonic(KeyEvent.VK_F);
        JMenu edit = new EditMenu(graph);
        edit.setMnemonic(KeyEvent.VK_E);
        this.add(file);
        this.add(edit);
    }

}
