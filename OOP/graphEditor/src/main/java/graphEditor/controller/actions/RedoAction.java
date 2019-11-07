package graphEditor.controller.actions;

import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 19/06/2017. Implements redo method of the UndoManager
 */
public class RedoAction extends AbstractAction implements Observer {
    private GraphModel graph;

    //Action only available if the UndoManager can redo something
    public void fixEnabled() {
        if(!graph.getManager().canRedo())
            setEnabled(false);
        else
            setEnabled(true);
    }

    public RedoAction(GraphModel graph) {
        super("Redo");
        this.graph = graph;
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graph.getManager().redo();
    }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }
}
