package Controller;

import Model.InferenceEngine;
import Model.KnowledgeBase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionButtonClicked implements ActionListener {

    private int id;

    public OptionButtonClicked(int id) {
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        InferenceEngine.getCurrentQuestion().answer(id);
        InferenceEngine.waitingForQuestion = false;
        InferenceEngine.solveNextGoal();
    }
}
