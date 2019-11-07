package graphEditor.controller;

import graphEditor.controller.actions.*;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by marcobreemhaar on 17/06/2017. Edit menu contains the actions that edit the model directly.
 */
public class EditMenu extends JMenu {
    public EditMenu(GraphModel graph) {
        this.setText("Edit");

        //add undo menu item with shortcut alt + z
        JMenuItem undo = new JMenuItem(new UndoAction(graph));
        this.add(undo);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                ActionEvent.CTRL_MASK));

        //add redo menu item with shortcut alt + y
        JMenuItem redo = new JMenuItem(new RedoAction(graph));
        this.add(redo);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                ActionEvent.CTRL_MASK));

        this.addSeparator();

        //add addVertex menu item, shortcut for adding is alt + a
        JMenuItem addVertex = new JMenuItem(new AddVertexAction(graph));
        this.add(addVertex);

        //add removeVertex menu item, shortcut for removing is alt + r
        JMenuItem removeVertex = new JMenuItem(new RemoveVertexAction(graph));
        this.add(removeVertex);

        this.addSeparator();

        //add addEdge menu item, shortcut for adding is alt + a
        JMenuItem addEdge = new JMenuItem(new AddEdgeAction(graph));
        this.add(addEdge);

        //add removeEdge menu item, shortcut for removing is alt + r
        JMenuItem removeEdge = new JMenuItem(new RemoveEdgeAction(graph));
        this.add(removeEdge);
    }
}
