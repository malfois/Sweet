package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import ncd.gui.component.initialvalue.IComponent;
import ncd.logger.NcdLogger;

public class ComponentHandler<T> implements EventHandler<Event> {

	private IComponent<T> component;

	public ComponentHandler(IComponent<T> component) {
		this.component = component;
	}

	public IComponent<T> getComponent() {
		return component;
	}

	@Override
	public void handle(Event arg0) {
		NcdLogger.Info(this.getClass().getSimpleName(), "handle", this.component.actionToLogger());
		ComponentEvent<T> e = new ComponentEvent<>(ComponentEvent.INPUT_VALIDATED, this.component.getValue());
		this.component.getControl().fireEvent(e);
	}

}
