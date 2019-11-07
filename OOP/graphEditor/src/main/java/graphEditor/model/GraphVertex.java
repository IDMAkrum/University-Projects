package graphEditor.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by marcobreemhaar on 02/06/2017.
 *
 * Represents a vertex in a graph
 */
public class GraphVertex extends Observable {
    private String name = "new vertex";
    private Rectangle rectangle = new Rectangle(100,100,100,50);
    private List<GraphEdge> connections = new ArrayList<>();

    // Creates a new vertex with specified parameters, used for loading a model
    public GraphVertex(String name, int x, int y, int width, int height) {
        this.name = name;
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;
    }

    // Creates a new vertex with default values, used in all other instances (besides loading)
    // since all values can be adjusted manually (e.g. a different name or position)
    public GraphVertex() {
    }

    // return the vertex name
    public String getName() {
        return name;
    }

    // set the vertex name, then let observers now vertex has changed
    public void setName(String name) {
        this.name = name;
        updateSelf();
    }

    // Adds a connection to the connection list.
    public void addConnection(GraphEdge edge) {
        connections.add(edge);
    }

    // Removes a connection from the connection list.
    public void removeConnection(GraphEdge edge) {
        connections.remove(edge);
    }

    // Returns a list with all edges connected to the graph.
    public List<GraphEdge> getConnections() {
        return connections;
    }

    // set rectangle of vertex
    public void setRect(Rectangle rectangle) {
        this.rectangle = rectangle;
        updateSelf();
    }

    // return rectangle of vertex
    public Rectangle getRect() {
        return rectangle;
    }

    // method that combines setting itself as changed, and notifying observers of said change
    private void updateSelf() {
        setChanged();
        notifyObservers();
    }
}
