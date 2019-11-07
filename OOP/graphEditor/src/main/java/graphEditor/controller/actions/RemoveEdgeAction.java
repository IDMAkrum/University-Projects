package graphEditor.controller.actions;

import graphEditor.controller.Enabled;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 18/06/2017. Removes an edge
 */
public class RemoveEdgeAction extends AbstractAction implements Observer, Enabled {
    private GraphModel graph;

    //Action only available if a vertex is selected, and that vertex has edges that can be removed
    public void fixEnabled() {
        if(graph.getSelection() == null || graph.getSelection().getConnections().isEmpty())
            setEnabled(false);
        else
            setEnabled(true);
    }

    public RemoveEdgeAction(GraphModel graph) {
        super("Remove edge");
        this.graph = graph;
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graph.removeEdgeUndoable();
    }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }
}
