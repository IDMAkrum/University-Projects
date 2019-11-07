package Model;


/**
 * Options are used for questions. They have a description and a fact. The description will be the text on the button
 * when the question is posed to the user. When the option is chosen by the user, this option's fact will be added to
 * the knowledge base.
 */
public class Option {
    private String description;
    private Fact fact;

    public Option(String description, String factName, String factValue) {
        this.description = description;
        this.fact = new Fact(factName,factValue);
    }

    public String getDescription() {
        return description;
    }

    public Fact getFact() {
        return fact;
    }

    public void apply() {
        KnowledgeBase.add(fact);
    }
}
