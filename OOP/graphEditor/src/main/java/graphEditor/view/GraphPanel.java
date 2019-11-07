package graphEditor.view;

import graphEditor.controller.SelectionController;
import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 05/06/2017. Describes view for graph model.
 */
public class GraphPanel extends JPanel implements Observer {
    private GraphModel graph;

    /* Create new GraphPanel */
    public GraphPanel(GraphModel graph) {
        this.graph = graph;
        graph.addObserver(this);
        setBackground(new Color(255, 253, 248));
        SelectionController controller = new SelectionController(graph);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

    // can make the view present a different graph
    public void setGraphModel(GraphModel graph) {
        this.graph = graph;
        graph.addObserver(this);
    }

    private int vertexWidth(Rectangle rect) {
        if((getHeight() * rect.height) / (getWidth() * rect.width) <= 1.0)
            return ((vertexHeight(rect) * rect.width) / rect.height);
        return getWidth()/10;
    }

    private int vertexHeight(Rectangle rect) {
        if((getHeight() * rect.height) / (getWidth() * rect.width) > 1.0)
            return ((vertexWidth(rect) * rect.height) / rect.width);
        return getHeight()/10;
    }

    // draw a string in the center of a vertex
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    //paint the vertices of the model
    private void paintVertices(Graphics g) {
        g.setColor(Color.black);
        for(GraphVertex vertex : graph.getVertexList()) {
            Rectangle rect = vertex.getRect();
            g.setColor(Color.WHITE);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            //a selected vertex has a blue border and text, otherwise its text and border are black
            if(vertex.equals(graph.getSelection())) {
                g.setColor(Color.BLUE);
                add(graph.getNameField());
            } else { g.setColor(Color.BLACK); }
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
            drawCenteredString(g,vertex.getName(),rect,new Font("TimesRoman", Font.PLAIN, 12));
        }
    }

    //method for painting an edge
    private void paintEdge(Graphics g, GraphEdge edge) {
        Rectangle start = edge.getStart().getRect();
        Rectangle end = edge.getEnd().getRect();

        int startX = (int) start.getCenterX();
        int startY = (int) start.getCenterY();

        int endX = (int) end.getCenterX();
        int endY = (int) end.getCenterY();

        g.drawLine(startX,startY,endX,endY);
    }

    //paint all edges in the model
    private void paintEdges(Graphics g) {
        //if we are adding an edge, draw the temporary edge between the selected vertex and the mouse
        if(graph.getSelection() != null && graph.getTempEdge() != null) {
            g.setColor(Color.blue);
            paintEdge(g, graph.getTempEdge());
        }
        for (GraphEdge edge: graph.getEdgeList()) {
            g.setColor(Color.black);
            paintEdge(g, edge);
        }
    }

    /* Paint the items that this class alone is responsible for. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintEdges(g);
        paintVertices(g);
    }

    /* Tell GraphPanel that the object it displays has changed, revalidate to make sure name field is
     * appropriately displayed  */
    @Override
    public void update(Observable observed, Object message) {
        revalidate();
        repaint(); }
}
