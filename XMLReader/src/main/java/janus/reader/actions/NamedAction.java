package janus.reader.actions;

/**
 * An action with a name
 * 
 * @author javaman
 *
 */
public interface NamedAction extends Action, SetAction {
    TagPath getName();
    Action getAction();
    SetAction getSetter();
}
