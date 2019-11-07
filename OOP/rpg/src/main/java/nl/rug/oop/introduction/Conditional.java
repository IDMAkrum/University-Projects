package nl.rug.oop.introduction;

import java.io.Serializable;

/**
 * Created by Ivana on 08/05/2017. Works as a mutable Boolean class. Used
 * as conditions in if-statements to regulate run-time updates to game.
 */
public class Conditional implements Serializable {
    private static final long serialVersionUID = 42L;
    private boolean status;

    public Conditional(boolean status) {
        this.status = status;
    }

    public boolean getCondition() {
        return status;
    }

    public void setCondition(boolean status) {
        this.status = status;
    }
}
