package nl.rug.oop.introduction;

import java.io.Serializable;

/**
 * Created by Ivana on 08/05/2017. Dialogue consists of a two strings, one (response) responds to the other (question).
 * Question does not necessarily have to be a question.
 */
public class Dialogue implements Serializable{
    private static final long serialVersionUID = 42L;
    private String question, response;

    public Dialogue(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public String getQuestion() {
        return question;
    }

    public String getResponse() {
        return response;
    }

}
