package janus.reader.actions;

/**
 * An {@link Action} or {@link SetAction} with a name
 * 
 * @author javaman
 *
 */
public class SimpleNamedAction implements NamedAction {
    private TagPath name;
    private Action action;
    private SetAction setter;

    public SimpleNamedAction(TagPath name, Action action, SetAction setter) {
        super();
        this.name = name;
        this.action = action;
        this.setter = setter;
    }

    @Override
    public void setValue(String value) {
        if (setter != null) {
            setter.setValue(value);
        }

    }

    @Override
    public void pop() {
        if (action != null) {
            action.pop();
        }
    }

    @Override
    public void push() {
        if (action != null) {
            action.push();
        }
    }

    @Override
    public TagPath getName() {
        return name;
    }

    @Override
    public Action getAction() {
        return action;
    }
}
