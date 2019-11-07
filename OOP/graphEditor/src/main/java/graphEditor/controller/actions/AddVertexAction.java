package graphEditor.controller.actions;

import graphEditor.controller.Enabled;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 18/06/2017. Add default vertex to the graph.
 */
public class AddVertexAction extends AbstractAction implements Observer, Enabled {
    private GraphModel graph;

    //Action always available
    public void fixEnabled() {
        setEnabled(true);
    }

    public AddVertexAction(GraphModel graph) {
        super("Add vertex");
        this.graph = graph;
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) { graph.addVertexUndoable(); }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }
}
