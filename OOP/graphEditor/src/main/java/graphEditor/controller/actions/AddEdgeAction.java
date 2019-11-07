package graphEditor.controller.actions;

import graphEditor.controller.Enabled;
import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 18/06/2017. Adds edge to the graph.
 */
public class AddEdgeAction extends AbstractAction implements Observer, Enabled {
    private GraphModel graph;

    //Action only available if a vertex is selected, and there are at least two vertices
    public void fixEnabled() {
        if(graph.getSelection() == null || graph.getVertexList().size() < 2)
            setEnabled(false);
        else
            setEnabled(true);
    }

    public AddEdgeAction(GraphModel graph) {
        super("Add edge");
        this.graph = graph;
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graph.addEdgeUndoable();
    }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }
}
