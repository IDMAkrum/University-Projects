package Model;

import View.DiagFrame;

import javax.swing.*;
import java.util.ArrayList;

public class InferenceEngine {

    private static DiagFrame frame;
    private static Question currentQuestion;

    public static Boolean waitingForQuestion = false;

    private InferenceEngine(){}

    private static void setToUnknown(Goal goal) {
        String name = goal.getName();
        Fact fact = new Fact(name,"unknown");
        KnowledgeBase.add(fact);
    }

    private static void forwardChain() {
        while (KnowledgeBase.nextApplicableRule() != null) {
            KnowledgeBase.nextApplicableRule().apply();
        }
    }

    private static void backwardChain() {
        Goal goal = KnowledgeBase.nextGoal();
        if (goal == null) {
            return;
        }

        ArrayList<Rule> rules = KnowledgeBase.getRules();
        ArrayList<Question> questions = KnowledgeBase.getQuestions();

        for(int i = 0; i < rules.size(); i++) {
            if (rules.get(i).canInfer(goal.getName())){
                rules.get(i).addConditionToGoals();
                return;
            }
        }

        for(int i = 0; i < questions.size(); i++) {
            if (questions.get(i).canAnswer(goal.getName())){
                questions.get(i).ask();
                waitingForQuestion = true;
                return;
            }
        }

        setToUnknown(goal);
    }

    public static void solveNextGoal() {

        if (KnowledgeBase.hasFact("diagnosis")) {
            showResult();
        }

        System.out.println("---------------------------------------------------");
        KnowledgeBase.printFacts();
        KnowledgeBase.printGoals();
        KnowledgeBase.printRules();

        Goal goal = KnowledgeBase.nextGoal();

        forwardChain();

        if (goal.isCompleted()) {
            KnowledgeBase.remove(goal);
        }

        backwardChain();

        if (!waitingForQuestion) {
            solveNextGoal();
        }
    }

    public static void setFrame(DiagFrame f) {
        frame = f;
    }

    public static Question getCurrentQuestion() {
        return currentQuestion;
    }

    public static void run() {
        solveNextGoal();
    }

    public static void setCurrentQuestion(Question currentQuestion) {
        InferenceEngine.currentQuestion = currentQuestion;
    }

    public static DiagFrame getFrame() {
        return frame;
    }

    public static void showResult() {

        String diagnosis = KnowledgeBase.valueOfFact("diagnosis");
        Recommendation rec = KnowledgeBase.getRecommendation(diagnosis);

        JFrame resultFrame = new JFrame();
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JOptionPane.showMessageDialog(resultFrame,
                "<html><body><p style='width: 200px;'>"+rec.getValue()+"</p></body></html>",
                "Diagnosis: " + rec.getName(),
                JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }
}

