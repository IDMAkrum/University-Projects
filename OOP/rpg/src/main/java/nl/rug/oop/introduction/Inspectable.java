package nl.rug.oop.introduction;

import java.io.Serializable;

/**
 * Created by Ivana on 01/05/2017.
 */
public abstract class Inspectable implements Serializable {
    private static final long serialVersionUID = 42L;
    private String description;

    public Inspectable(String description) {
        this.description = description;
    }

    //prints the current description
    public void inspect() {
        System.out.println(description);
    }
}
