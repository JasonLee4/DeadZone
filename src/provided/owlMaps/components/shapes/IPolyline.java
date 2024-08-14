package provided.owlMaps.components.shapes;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.general.IEditableObject;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IComponentMouseDragEventObject;


/**
 * Represents a polyline drawn on the map
 * Corresponds to the Google Maps Javascript API google.maps.Polyline
 * A polyline defines a series of connected coordinates in an ordered sequence, i.e. an IPath of ILatLngs
 * @author swong
 *
 */
public interface IPolyline extends  IVisibleMapObject, IEditableObject, IPolyMouseEventObject, IComponentMouseDragEventObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Gets the IPath used to define this polyline
	 * @return An IPath of ILatLngs
	 */
	public IPath<ILatLng> getPath();
	
	/**
	 * Sets the path of this polyline
	 * @param path An IPath of ILatLngs
	 */
	public void setPath(IPath<ILatLng> path);

}
