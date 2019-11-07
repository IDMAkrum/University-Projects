package graphEditor.model;

import graphEditor.controller.NameField;
import graphEditor.model.operations.AddEdge;
import graphEditor.model.operations.AddVertex;
import graphEditor.model.operations.RemoveEdge;
import graphEditor.model.operations.RemoveVertex;

import javax.swing.undo.UndoManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marcobreemhaar on 02/06/2017.
 *
 * Represents a graph.
 */
public class GraphModel extends Observable implements Observer {
    // edgeList and vertexList contain all edges and vertices in the graph.
    private List<GraphEdge> edgeList = new ArrayList<>();
    private List<GraphVertex> vertexList = new ArrayList<>();
    private String state = "idle";
    private NameField namefield = new NameField(new GraphVertex());
    private GraphVertex selected = null;
    private GraphEdge tempedge = null;
    private UndoManager manager = new UndoManager();

    // reset graph values
    public void resetGraph() {
        edgeList.clear();
        vertexList.clear();
        selected = null;
        tempedge = null;
        state = "idle";
        updateSelf();
    }

    // Adds default vertex to the graph
    public void addVertex(){
        GraphVertex newVertex = new GraphVertex();

        vertexList.add(newVertex);
        newVertex.addObserver(this);
        updateSelf();
    }

    // the undoable version of adding vertices, adds an edit to the undo manager
    public void addVertexUndoable() {
        AddVertex edit = new AddVertex(this);
        manager.addEdit(edit);
    }

    // Removes vertex from the graph, along with all connected edges.
    public void removeVertex(GraphVertex vertex) {

        edgeList.removeAll(vertex.getConnections());
        vertexList.remove(vertex);
        updateSelf();
    }

    // the undoable version of removing vertices, adds an edit to the undo manager
    public void removeVertexUndoable() {
        RemoveVertex edit = new RemoveVertex(this);
        manager.addEdit(edit);
    }

    // Adds an edge to the graph connecting the vertices specified with the start and end arguments.
    public void addEdge(GraphVertex start, GraphVertex end) {
        GraphEdge newEdge = new GraphEdge(start,end);

        edgeList.add(newEdge);
        start.addConnection(newEdge);
        end.addConnection(newEdge);
        updateSelf();

    }

    // the undoable version of adding edges, adds an edit to the undo manager
    public void addEdgeUndoable() {
        AddEdge edit = new AddEdge(this);
        manager.addEdit(edit);
    }

    // Removes edges from the graph and also from the connections list of the start and end vertices.
    public void removeEdge(GraphEdge edge) {
        edge.getStart().removeConnection(edge);
        edge.getEnd().removeConnection(edge);
        edgeList.remove(edge);
        updateSelf();
    }

    // the undoable version of removing edges, adds an edit to the undo manager
    public void removeEdgeUndoable() {
        RemoveEdge edit = new RemoveEdge(this);
        manager.addEdit(edit);
    }

    // set currently selected graph, as well as its initialise its name field
    public void setSelection(GraphVertex vertex) {
        selected = vertex;
        if(selected != null) { namefield = new NameField(selected); }
        updateSelf();
    }

    // get currently selected graph
    public GraphVertex getSelection() { return selected; }

    // set the temporary edge, becomes null if not currently adding any edges
    public void setTempEdge(GraphEdge edge) {
        if(state.equals("adding edge")) {
            tempedge = edge;
        } else { tempedge = null; }
        updateSelf();
    }

    // get the temporary edge (line from selected vertex to mouse)
    public GraphEdge getTempEdge() { return tempedge; }

    // make the name field visible and editable
    public void changeNameField() {
        namefield.setVisible(true);
        namefield.setEditable(true);
    }

    // return the name field of the graph
    public NameField getNameField() { return namefield; }

    // set the graph state
    public void setState(String state) { this.state = state; updateSelf(); }

    // return the graph state
    public String getState() { return state; }

    // add a vertex directly to the vertex list
    public void appendVertexList(GraphVertex vertex) {
        vertexList.add(vertex);
        updateSelf();
    }

    // return the vertex list that contains all the vertices in the graph
    public List<GraphVertex> getVertexList() { return vertexList; }

    // adds edge directly to the edge list
    public void appendEdgeList(GraphEdge edge) {
        edgeList.add(edge);
        updateSelf();
    }

    // return edge list that contains all the edges in the graph
    public List<GraphEdge> getEdgeList() { return edgeList; }

    // return the undo manager of the graph
    public UndoManager getManager() { return manager; }

    // method that combines setting itself as changed, and notifying observers of said change
    public void updateSelf() {
        setChanged();
        notifyObservers();
    }

    // update itself if something has changed in its vertices
    @Override
    public void update(Observable observed, Object message) { updateSelf(); }

}
