package provided.owlMaps.components.shapes;

import java.util.function.Consumer;

import provided.owlMaps.mouse.MapMouseEventType;



/**
 * An entity that accepts the MapMouseEventsType events but where 
 * the more specialized IPolyMouseEvent is provided when the event 
 * is event is invoked instead of just an IMouseEvent.
 * @author swong
 *
 */
public interface IPolyMouseEventObject {

	/**
	 * Set the event 
	 * @param eventType The type of event being set
	 * @param eventFn The callback to use when the event is invoked.
	 */
	void setPolyMouseEvent(MapMouseEventType eventType, Consumer<IPolyMouseEvent> eventFn);
	
}