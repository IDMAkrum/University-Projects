package Model;

public class Recommendation {
    private String name;
    private String value;

    public Recommendation(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
