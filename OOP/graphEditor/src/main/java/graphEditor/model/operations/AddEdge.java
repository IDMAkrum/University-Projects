package graphEditor.model.operations;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * Created by Ivana on 19/06/2017. Allows adding edges to be undone and redone
 */
public class AddEdge extends AbstractUndoableEdit {
    private GraphModel graph;
    private GraphEdge edge;

    public AddEdge(GraphModel graph) {
        this.graph = graph;
        edge = null;
        graph.setState("adding edge");
    }

    //get most recently added edge, and remove it
    @Override
    public void undo() {
        super.undo();
        edge = graph.getEdgeList().get(graph.getEdgeList().size() - 1);
        graph.removeEdge(edge);
    }

    //add edge that was removed in undo
    @Override
    public void redo() {
        super.redo();
        if (edge != null) {
            graph.addEdge(edge.getStart(), edge.getEnd());
        }
    }
}
