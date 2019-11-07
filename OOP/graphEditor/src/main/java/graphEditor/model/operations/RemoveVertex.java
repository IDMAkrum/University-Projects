package graphEditor.model.operations;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * Created by Ivana on 19/06/2017. Allows removing vertices to be undone and redone
 */
public class RemoveVertex extends AbstractUndoableEdit {
    private GraphModel graph;
    private GraphVertex vertex;

    public RemoveVertex(GraphModel graph) {
        this.graph = graph;
        vertex = graph.getSelection();
        graph.removeVertex(vertex);
        graph.setSelection(null);
    }

    //Add all edges that were removed, then add the vertex itself
    @Override
    public void undo() {
        super.undo();
        for(GraphEdge edge : vertex.getConnections()) {
            graph.appendEdgeList(edge);
        }
        graph.appendVertexList(vertex);
    }

    //removes most recently added vertex
    @Override
    public void redo() {
        super.redo();
        vertex = graph.getVertexList().get(graph.getVertexList().size()-1);
        graph.removeVertex(vertex);
    }
}
