package provided.owlMaps.mouse;

import java.util.function.Consumer;


/**
 * Represents a map component that responds to mouse click events
 * @author swong
 *
 */
public interface IComponentMouseEventObject {

	/**
	 * Set the given ComponentMouseEventType event to invoke the given event handler function
	 * @param eventType The desired event type
	 * @param eventFn The event handler function to invoke when the event occurs.
	 */
	void setComponentMouseEvent(ComponentMouseEventType eventType, Consumer<IMouseEvent> eventFn);
	
}