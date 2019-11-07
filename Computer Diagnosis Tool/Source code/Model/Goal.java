package Model;

import java.util.ArrayList;

/**
 * A goal is what we eventually want to find. If there is a fact with the same name as a goal and it has a value equal
 * to one of the recommendation's names, this recommendation will be given to the user.
 */
public class Goal {
    private String name;
    private ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();

    public Goal(String name) {
        this.name = name;
    }

    public void add(Recommendation recommendation) {
        recommendations.add(recommendation);
    }

    public Boolean isCompleted(){
        return KnowledgeBase.hasFact(this.name);
    }

    public String getName() {
        return name;
    }
}
