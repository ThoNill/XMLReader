package janus.reader.actions;

public interface NamedAction extends Action, SetAction {
    String getName();

    Action getAction();
}
