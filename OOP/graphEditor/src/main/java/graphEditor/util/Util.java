package graphEditor.util;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.List;
import java.util.Observable;

/**
 * Created by marcobreemhaar on 08/06/2017.
 */
public class Util extends Observable {

    // Saves the model to a file
    public static void save(GraphModel model) {
        File filename;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = chooser.showSaveDialog(new JFrame("Save graph"));

        if (result == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile();
        } else {
            return;
        }

        List<GraphEdge> edgeList = model.getEdgeList();
        List<GraphVertex> vertexList = model.getVertexList();

        // Get number of edges and vertices to put at the top of the save file.
        int nEdges = edgeList.size();
        int nVertices = vertexList.size();

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");

            // Write number of vertices and edges to save file.
            writer.println(nVertices + " " + nEdges);

            // Write the information of each vertex to the file.
            for (GraphVertex v : vertexList) {
                int x = v.getRect().x;
                int y = v.getRect().y;
                int width = v.getRect().width;
                int height = v.getRect().height;
                String name = v.getName();

                writer.printf("%d %d %d %d %s\n",x,y,width,height,name);
            }

            // Write the information of each edge to the file.
            for (GraphEdge e : edgeList) {
                GraphVertex start = e.getStart();
                GraphVertex end = e.getEnd();

                int iStart = vertexList.indexOf(start);
                int iEnd = vertexList.indexOf(end);

                writer.printf("%d %d\n",iStart,iEnd);
            }

            writer.close();

        } catch (IOException e) {
            System.err.print("Failed to save file. ");
            e.printStackTrace();
        }
    }


    // Loads a model from the specified file.
    public static void load(GraphModel model) {
        File filename;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = chooser.showOpenDialog(new JFrame("Load graph"));

        if (result == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile();
        } else {
            return;
        }

        List<GraphEdge> edgeList = model.getEdgeList();
        List<GraphVertex> vertexList = model.getVertexList();

        // Discard the current model.
        model.resetGraph();

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            // Get the number of vertices and edges from the first line.
            String line = br.readLine();

            int nVertices = Integer.parseInt(line.split(" ")[0]);
            int nEdges = Integer.parseInt(line.split(" ")[1]);

            // For each vertex, get the information from the file and create it.
            for (int i = 0; i < nVertices; i++) {
                String[] data = br.readLine().split(" ");

                int x = Integer.parseInt(data[0]);
                int y = Integer.parseInt(data[1]);
                int width = Integer.parseInt(data[2]);
                int height = Integer.parseInt(data[3]);

                StringBuilder stringBuilder = new StringBuilder();

                for (int j = 4; j < data.length; j++) {
                    stringBuilder.append(data[j]);
                    if (j + 1 != data.length) {
                        stringBuilder.append(" ");
                    }
                }

                String name = stringBuilder.toString();

                GraphVertex vertex = new GraphVertex(name, x, y, width, height);
                vertexList.add(vertex);
            }

            // For each edge, get the information from the file and link the vertices.
            for (int i = 0; i < nEdges; i++) {
                String[] data = br.readLine().split(" ");
                int startIndex = Integer.parseInt(data[0]);
                int endIndex = Integer.parseInt(data[1]);

                GraphVertex start = vertexList.get(startIndex);
                GraphVertex end = vertexList.get(endIndex);

                GraphEdge edge = new GraphEdge(start, end);
                edgeList.add(edge);

                start.addConnection(edge);
                end.addConnection(edge);
            }

            br.close();

            model.updateSelf();

        } catch (FileNotFoundException e) {
            System.err.println("Save file was not found. Failed to load model.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("An I/O error occurred. Failed to load model.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Save file is corrupted. Failed to load model.");
            e.printStackTrace();
        }

    }
}
