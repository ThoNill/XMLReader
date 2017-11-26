package janus.reader.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link Action} or {@link SetAction} with a name
 * 
 * @author javaman
 *
 */
public class SimpleNamedAction implements NamedAction {
    static private Logger log = LoggerFactory.getLogger(SimpleNamedAction.class);

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
    public void setValue(Object value) {
        if (setter != null && value != null) {
            log.debug("simpleNamedAction setze mit {} auf {}",setter,value);
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
    

    @Override
    public SetAction getSetter() {
        return setter;
    }

   @Override
   public boolean isSetableFromString() {
       return setter != null && setter.isSetableFromString();
   }

   @Override
   public TagPath getValuePath() {
       return (setter != null ) ? setter.getValuePath() : null;
   }

}
