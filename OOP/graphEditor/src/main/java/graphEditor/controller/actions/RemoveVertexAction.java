package graphEditor.controller.actions;

import graphEditor.controller.Enabled;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 18/06/2017. Action that removes a vertex and all its edges.
 */
public class RemoveVertexAction extends AbstractAction implements Observer, Enabled {
    private GraphModel graph;

    //Action only available if a vertex is selected
    public void fixEnabled() {
        if(graph.getSelection() == null)
            setEnabled(false);
        else
            setEnabled(true);
    }

    public RemoveVertexAction(GraphModel graph) {
        super("Remove vertex");
        this.graph = graph;
        graph.setState("removing vertex");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graph.removeVertexUndoable();
    }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }

}

