package graphEditor.controller.actions;

import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivana on 19/06/2017. Undo functionality of the UndoManager
 */
public class UndoAction extends AbstractAction implements Observer {
    private GraphModel graph;

    //Action only available if UndoManager can undo something
    public void fixEnabled() {
        if(!graph.getManager().canUndo())
            setEnabled(false);
        else
            setEnabled(true);
    }

    public UndoAction(GraphModel graph) {
        super("Undo");
        this.graph = graph;
        graph.addObserver(this);
        fixEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graph.getManager().undo();
    }

    @Override
    public void update(Observable observed, Object message) { fixEnabled(); }
}

