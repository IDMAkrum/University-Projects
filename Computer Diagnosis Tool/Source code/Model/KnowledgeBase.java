package Model;

import java.util.ArrayList;
import java.util.Stack;

public class KnowledgeBase {
    private KnowledgeBase(){}

    private static ArrayList<Fact> facts = new ArrayList<>();
    private static ArrayList<Rule> rules = new ArrayList<>();
    private static ArrayList<Question> questions = new ArrayList<>();
    private static Stack<Goal> goals = new Stack<>();
    private static ArrayList<Recommendation> recommendations = new ArrayList<>();
    private static Logger log = new Logger();

    public static void printFacts() {
        System.out.println("Facts:");
        for (int i = 0; i < facts.size(); i++) {
            System.out.println(facts.get(i).getName() + " = " + facts.get(i).getValue());
        }
    }

    public static void printRules() {
        System.out.println("Rules:");
        for (int i = 0; i < rules.size(); i++) {
            System.out.println(rules.get(i));
        }
    }

    public static void printGoals() {
        System.out.println("Goals:");
        for (int i = 0; i < goals.size(); i++) {
            System.out.println(goals.get(i).getName());
        }
    }

    public static void cleanRules() {
        while(nextRedundantRule() != null) {
            remove(nextRedundantRule());
        }
    }

    public static void add(Fact fact) {
        if (!KnowledgeBase.hasFact(fact.getName())) {
            facts.add(fact);
        }

        cleanRules();
    }

    public static void add(Rule rule) {
        rules.add(rule);
    }

    public static void remove(Rule rule) {
        ArrayList<Fact> conditions = rule.getCondition();
        for (int i = 0; i < conditions.size(); i++) {
            removeGoal(conditions.get(i).getName());
        }
        rules.remove(rule);
    }

    public static void add(Question question) {
        questions.add(question);
    }

    public static void remove(Question question) {
        questions.remove(question);
    }

    public static void add(Goal goal) {
        goals.add(goal);
    }

    public static void removeGoal(String name) {
        for (int i = goals.size()-1; i >= 0; i--) {
            if (goals.get(i).getName().equals(name)) {
                goals.remove(i);
            }
        }
    }

    public static void remove(Goal goal) {
        goals.remove(goal);
    }

    public static void add(Recommendation rec) { recommendations.add(rec); }

    public static Recommendation getRecommendation(String diagnosis) {
        for (int i = 0; i < recommendations.size(); i++) {
            if (recommendations.get(i).getName().equals(diagnosis)) {
                return recommendations.get(i);
            }
        }
        return new Recommendation("unknown","Diagnosis is unknown");
    }

    public static Boolean hasFact(Fact fact) {
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i).equals(fact)) {
                return true;
            }
        }
        return false;
    }

    public static String valueOfFact(String name) {
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i).getName().equals(name)) {
                return facts.get(i).getValue();
            }
        }
        return "unknown";
    }

    public static Boolean allGoalsReached() {
        for(int i = 0; i < goals.size(); i++) {
            if (!goals.get(i).isCompleted()){
                return false;
            }
        }
        return true;
    }

    public static Goal nextGoal(){
        if (goals.isEmpty()) {
            return null;
        } else {
            return goals.peek();
        }
    }

    public static Rule nextApplicableRule() {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).isValid()) {
                return rules.get(i);
            }
        }
        return null;
    }

    public static Rule nextRedundantRule() {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).isRedundant()) {
                return rules.get(i);
            }
        }
        return null;
    }

    public static Boolean hasFact(String name) {
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Rule> getRules() {
        return rules;
    }

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public static Logger getLog() { return log; }
}
