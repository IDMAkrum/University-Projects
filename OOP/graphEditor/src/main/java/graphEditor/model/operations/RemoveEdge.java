package graphEditor.model.operations;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * Created by Ivana on 19/06/2017. Allows removing edges to be undone and redone
 */
public class RemoveEdge extends AbstractUndoableEdit {
    private GraphModel graph;
    private GraphVertex start, end;

    public RemoveEdge(GraphModel graph) {
        this.graph = graph;
        start = graph.getSelection();
        end = graph.getSelection();
        graph.setState("removing edge");
    }

    //start vertex stored in start, end vertex determined by currently selected vertex, if a vertex is
    //currently selected. Creates edge between start and end
    @Override
    public void undo() {
        super.undo();
        if(graph.getSelection() != null ) { end = graph.getSelection(); }
        graph.addEdge(start, end);
    }

    //removes most recently added edge
    @Override
    public void redo() {
        super.redo();
        GraphEdge edge = graph.getEdgeList().get(graph.getEdgeList().size()-1);
        graph.removeEdge(edge);
    }
}
