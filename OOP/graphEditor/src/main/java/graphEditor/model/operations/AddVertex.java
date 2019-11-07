package graphEditor.model.operations;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * Created by Ivana on 19/06/2017. Allows adding vertices to be undone and redone
 */
public class AddVertex extends AbstractUndoableEdit {
    private GraphModel graph;
    private GraphVertex vertex;

    public AddVertex(GraphModel graph) {
        this.graph = graph;
        graph.addVertex();
    }

    //gets most recently added vertex and removes it
    @Override
    public void undo() {
        super.undo();
        vertex = graph.getVertexList().get(graph.getVertexList().size()-1);
        graph.removeVertex(vertex);
    }

    //first adds the edges of the removed vertex, then adds vertex itself
    @Override
    public void redo() {
        super.redo();
        for(GraphEdge edge : vertex.getConnections()) {
            graph.appendEdgeList(edge);
        }
        graph.appendVertexList(vertex);
    }
}
