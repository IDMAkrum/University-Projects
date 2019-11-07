package graphEditor.controller;

/**
 * Created by Ivana on 18/06/2017. Interface for classes extending AbstractAction, must have a fixenabled method
 * that determines when action is usable.
 */
public interface Enabled {
    void fixEnabled();
}
