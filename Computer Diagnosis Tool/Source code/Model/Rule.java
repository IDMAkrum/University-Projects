package Model;

import java.util.ArrayList;

public class Rule {
    private ArrayList<Fact> condition;
    private ArrayList<Fact> result;

    public Rule(ArrayList<Fact> condition, ArrayList<Fact> result) {
        this.condition = condition;
        this.result = result;
    }

    public Boolean isValid(){
        if (condition.size() == 0) {
            return false;
        }
        for(int i = 0; i < condition.size(); i++) {
            if (!KnowledgeBase.hasFact(condition.get(i))) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Fact> getCondition() {
        return condition;
    }

    public void apply() {
        if (this.isValid()) {

            for (int i = 0; i < result.size(); i++) {
                KnowledgeBase.add(result.get(i));
            }
            KnowledgeBase.remove(this);
        }
    }

    public Boolean canInfer(String name) {
        for(int i = 0; i < result.size(); i++) {
            if (result.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String needsFact(String name) {
        for (int i = 0; i < condition.size(); i++) {
            if (condition.get(i).getName().equals(name)) {
                return condition.get(i).getValue();
            }
        }
        return null;
    }

    public Boolean isRedundant() {
        for (int i = 0; i < condition.size(); i++) {
            if (KnowledgeBase.hasFact(condition.get(i).getName()) && !KnowledgeBase.valueOfFact(condition.get(i).getName()).equals(condition.get(i).getValue())) {
                return true;
            }
        }
        return false;
    }

    public void addConditionToGoals() {
        for(int i = 0; i < condition.size(); i++) {
            KnowledgeBase.add(new Goal(condition.get(i).getName()));
        }
    }

    @Override
    public String toString() {
        return condition.toString() + result.toString();
    }
}
