package Model;

import java.io.File;
import java.util.ArrayList;

public class Question {
    private String description;
    private ArrayList<Option> options;

    public Question(String description) {
        this.description = description;
        this.options = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void answer(int option) {
        options.get(option).apply();
        if(Load.getFile().exists() && !Load.getFile().isDirectory()) {
            KnowledgeBase.getLog().appendLog(this, options.get(option));
        }
        KnowledgeBase.remove(this);
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void addOption(Option o) {
        options.add(o);
    }

    public Boolean canAnswer(String name) {
        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getFact().getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void ask() {
        InferenceEngine.setCurrentQuestion(this);
        InferenceEngine.getFrame().askQuestion(this);
    }
}
