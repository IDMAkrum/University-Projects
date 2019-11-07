package graphEditor.controller;

import graphEditor.model.GraphModel;
import graphEditor.util.Util;
import graphEditor.view.GraphFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by marcobreemhaar on 17/06/2017. FileMenu contains the actions that do not edit the model directly, but do
 * provide other functions.
 */
public class FileMenu extends JMenu {

    public FileMenu(GraphModel graph) {
        this.setText("File");

        JMenu submenu = new JMenu("New...");
        //add new model action that resets current model and discards old model
        JMenuItem newmodel = new JMenuItem(new AbstractAction("Model") {
           @Override
            public void actionPerformed(ActionEvent e) {
               if(!graph.getVertexList().isEmpty()) {
                   int response = JOptionPane.showConfirmDialog(null, "Start a new model? (current model will be discarded!)", "Confirm",
                           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if (response == JOptionPane.YES_OPTION) {
                       graph.resetGraph();
                   }
               }
           }
        });
        submenu.add(newmodel);
        newmodel.setMnemonic(KeyEvent.VK_M);

        //add new frame that shows the same model as the original frame
        JMenuItem newframe = new JMenuItem(new AbstractAction("Window") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GraphFrame(graph);
            }
        });
        submenu.add(newframe);
        newframe.setMnemonic(KeyEvent.VK_W);

        this.add(submenu);
        submenu.setMnemonic(KeyEvent.VK_N);
        //add open item that links to the load method
        JMenuItem open = new JMenuItem(new AbstractAction("Open...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.load(graph);
            }
        });
        this.add(open);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));

        //add save item that links to the save method
        JMenuItem save = new JMenuItem(new AbstractAction("Save...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.save(graph);
            }
        });
        this.add(save);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));

        addSeparator();

        //add exit item that exits the entire program
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exit);
        exit.setMnemonic(KeyEvent.VK_ESCAPE);
        exit.setToolTipText("alt+esc");
    }
}
