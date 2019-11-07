package Model;

/*
 * Facts are needed to reach goals or apply rules. They have a name and a value.
 * For example: name = "connectiontype", value = "wifi".
 */

public class Fact {
    private String name;
    private String value;

    public Fact(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Fact(String name) {
        this.name = name;
        this.value = "undefined";
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isTrue() {
        return KnowledgeBase.hasFact(this);
    }

    @Override
    public String toString() {
        return name + " = " + value;
    }

    @Override
    public boolean equals(Object o) {
        Fact fact = (Fact) o;
        return this.name.equals(fact.name) && this.value.equals(fact.value);
    }
}
