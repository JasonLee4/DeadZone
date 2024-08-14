package provided.owlMaps.mouse;

import java.util.function.Consumer;

/**
 * Represents a map component that responds to mouse drag events
 * @author swong
 *
 */
public interface IComponentMouseDragEventObject {

	/**
	 * Set the given ComponentMouseDragEventType event to invoke the given event handler function
	 * @param eventType The desired event type
	 * @param eventFn The event handler function to invoke when the event occurs.
	 */
	void setComponentMouseDragEvent(ComponentMouseDragEventType eventType, Consumer<IMouseEvent> eventFn);

}