package graphEditor.model;

/**
 * Created by marcobreemhaar on 02/06/2017.
 *
 * Represents an edge in a graph.
 */
public class GraphEdge {
    private GraphVertex start;
    private GraphVertex end;

    // Creates a new graph edge connecting the start and end vertices.
    public GraphEdge(GraphVertex start, GraphVertex end) {
        this.start = start;
        this.end = end;
    }

    public GraphVertex getStart() {
        return start;
    }

    public void setStart(GraphVertex start) {
        this.start = start;
    }

    public GraphVertex getEnd() {
        return end;
    }

    public void setEnd(GraphVertex end) {
        this.end = end;
    }
}
