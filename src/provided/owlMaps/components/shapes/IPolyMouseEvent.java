package provided.owlMaps.components.shapes;

import provided.owlMaps.general.ILatLng;
import provided.owlMaps.mouse.IMouseEvent;

/**
 * An extension of IMouseEvent that adds parameters applicable to events occurring
 * on IPolygons or IPolylines.
 * @author swong
 *
 */
public interface IPolyMouseEvent extends IMouseEvent {
	/**
	 * The index of the edge within the path beneath the cursor when the event occurred, 
	 * if the event occurred on a mid-point on an editable polygon.
	 * @return The index of the edge 
	 */
	public int getEdge();
	
	/**
	 * The index of the path beneath the cursor when the event occurred, 
	 * if the event occurred on a vertex and the polygon is editable. Otherwise undefined.
	 * @return  The index of the path
	 */
	public int getPath();
	/**
	 * The index of the vertex beneath the cursor when the event occurred, if the event occurred on a vertex and the polyline or polygon is editable. 
	 * If the event does not occur on a vertex, the value is undefined.
	 * @return The index of the vertex
	 */
	public int getVertex();	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory to instantiate an IPolyMouseEvent from the given data values
	 * @param latLng The ILatLng where the event occurred on the map
	 * @param edge The edge index of where the event occurred on the polyline/polygon
	 * @param path The index of the path segment where the event occurred on the polyline/polygon
	 * @param vertex The index of the vertex where the event occurred on the polyline/polygon
	 * @return A new IPolyMouseEvent instance
	 */
	public static IPolyMouseEvent make(final ILatLng latLng, final int edge, final int path, final int vertex) {
		return new IPolyMouseEvent() {

			@Override
			public ILatLng getLatLng() {
				return latLng;
			}

			@Override
			public int getEdge() {
				return edge;
			}
			@Override
			public int getPath() {
				return path;
			}
			@Override
			public int getVertex() {
				return vertex;
			}
			
			@Override
			public String toString() {
				return "IPolyMouseEvent(latLng="+latLng+")";
			}
			
		};
	}

}
