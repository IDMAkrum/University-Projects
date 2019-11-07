package graphEditor;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;
import graphEditor.view.GraphFrame;

import java.awt.*;

public class GraphEditor {
    public static void main(String args[]) {
        GraphModel graph = new GraphModel();
        new GraphFrame(graph);
    }
}