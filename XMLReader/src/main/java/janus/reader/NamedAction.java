package janus.reader;

public interface NamedAction extends Action, SetAction{
    String getName();
    Action getAction();
}
