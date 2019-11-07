package graphEditor.controller;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Ivana on 18/06/2017. The selection controller keeps track of mouse movements and events. It allows
 * user to select vertices, change vertex size, add and remove edges, all through mouse events.
 */
public class SelectionController extends MouseInputAdapter {
    private GraphModel graph;
    private GraphVertex selected;

    public SelectionController(GraphModel graph) { this.graph = graph; }

    //dragging the mouse while holding shift moves a vertex
    @Override
    public void mouseDragged(MouseEvent e) {
        if(graph.getSelection() != null && e.isShiftDown()) { updatePosition(e, selected); }
    }

    //if mouse is moved while an edge is being added, a temporary edge is drawn between selected vertex and mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        if(graph.getSelection() != null && graph.getState().equals("adding edge")) { updateTempEdge(e); }
    }

    //if a vertex is double clicked, make its name field editable and visible
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && graph.getSelection() != null) {
            graph.changeNameField();
        }
    }

    //mouse pressed does different things depending on graph state
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //alters the selected vertex' size
        if(graph.getSelection() != null && (e.isControlDown() || e.isAltDown())) {
            updateSize(e, graph.getSelection());
        }
        //check if pressed location is equal to a vertex
        for (int i = graph.getVertexList().size() - 1; i >= 0; i--) {
            GraphVertex vertex = graph.getVertexList().get(i);
            Rectangle rect = vertex.getRect();
            if (x >= rect.x && x < (rect.x + rect.width)
                    && y > rect.y && y <= (rect.y + rect.height)) {
                //if in the process of adding an edge, add an edge between previous selected and current selected
                if (graph.getState().equals("adding edge")) {
                    System.out.println("Currently adding an edge...");
                    addEdgeOnPress(vertex);
                }
                //if in the process of removing an edge, remove edge between previous selected and current selected
                if (graph.getState().equals("removing edge")) {
                    System.out.println("Currently removing an edge...");
                    removeEdgeOnPress(vertex);
                }
                //update selected vertex
                changeSelection(vertex);
                break;
            } else {
                //the location pressed does not equal a vertex, so deselect the current selected vertex
                graph.setSelection(null);
                graph.getNameField().setVisible(false);
                graph.getNameField().setEditable(false);
            }
        }
    }

    //update selected vertex in graph, as well as own selected variable
    private void changeSelection(GraphVertex vertex) {
        graph.setSelection(vertex);
        System.out.println("Selected vertex: " + vertex.getName());
        selected = vertex;
        graph.getNameField().setVisible(false);
        graph.getNameField().setEditable(false);
    }

    //add edge between previously selected vertex (saved in selected variable) and current selected (passed along
    //by mouse event). Removes temporary edge
    private void addEdgeOnPress(GraphVertex vertex) {
        graph.setTempEdge(null);
        graph.addEdge(selected, vertex);
        graph.setState("idle");
    }

    //Check if edge exists, if so remove it
    private void removeEdgeOnPress(GraphVertex vertex) {
        for(GraphEdge edge : graph.getEdgeList()) {
            if((edge.getStart().equals(selected) && edge.getEnd().equals(vertex)) ||
                    (edge.getStart().equals(vertex) && edge.getEnd().equals(selected))) {
                graph.removeEdge(edge);
                break;
            }
        }
        graph.setState("idle");
    }

    //update temporary edge by changing the x, y coordinates of the mouse vertex
    private void updateTempEdge(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        GraphVertex mouse = new GraphVertex(null,x, y, 1, 1);
        graph.setTempEdge(new GraphEdge(selected, mouse));
    }

    //change size of vertex
    private void updateSize(MouseEvent e, GraphVertex vertex) {
        graph.setState("changing size");
        Rectangle old = vertex.getRect();
        int width = old.width, height = old.height;
        //enlarge vertex up till limit
        if(e.isControlDown() && width < 150 && height < 75) {
            width = (int) (old.width * 1.10);
            height = (int) (old.height * 1.10);
        //shrink vertex up till limit
        } else if(e.isAltDown() && width > 90 && height > 45) {
            width = (int) (old.width - (old.width * (0.10)));
            height = (int) (old.height - (old.height * (0.10)));
        }
        vertex.setRect(new Rectangle(old.x,old.y, width, height));
    }

    //update position while moving a vertex by updating the x and y position of the vertex' rectangle
    private void updatePosition(MouseEvent e, GraphVertex vertex) {
        int x = e.getX();
        int y = e.getY();
        graph.setState("moving vertex");
        Rectangle old = vertex.getRect();
        vertex.setRect(new Rectangle(x, y, old.width, old.height));
    }

}
