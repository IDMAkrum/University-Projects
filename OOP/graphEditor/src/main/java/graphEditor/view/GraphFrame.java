package graphEditor.view;

import graphEditor.controller.MenuBar;
import graphEditor.controller.SelectionController;
import graphEditor.model.GraphModel;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Ivana on 05/06/2017.
 */
public class GraphFrame extends JFrame {
    public GraphFrame(GraphModel graph) {
        super("Graph Editor");
        // dispose on close so that closing one frame doesn't necessarily close all frames
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setJMenuBar(new MenuBar(graph));
        this.setPreferredSize(new Dimension(800, 600));
        this.add(new GraphPanel(graph));
        // has a mouse and mouse motion listener so that multiple frames can be edited
        this.addMouseListener(new SelectionController(graph));
        this.addMouseMotionListener(new SelectionController(graph));
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen.
        this.setVisible(true);
    }
}
