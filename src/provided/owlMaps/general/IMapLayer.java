package provided.owlMaps.general;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a "layer" of components whose visibility can be controlled as one.
 * This is a Composite Design Pattern of IVisibleMapObjects.
 * This is used for setting an unspecified set of components to all be visible or invisible.
 * Since an IMapLayer is itself an IVisibleMapObject, IMapLayers can be nested inside of other IMapLayers.
 * @author swong
 *
 */
public interface IMapLayer extends IVisibleMapObject {

	/**
	 * Add a component to the layer.  The component's visibility will be set to the layer's visibility.
	 * @param comp A component whose visibility can be controlled.
	 * @return True if the component was not already in the layer.
	 */
	public boolean addComponent(IVisibleMapObject comp);
	
	/**
	 * Remove the specified component from the layer.  
	 * The component's visibility is unchanged.
	 * @param comp A component whose visibility can be controlled.
	 * @return True if the component was in the layer.
	 */
	public boolean removeComponent(IVisibleMapObject comp);
	
	/**
	 * Return a copy of the set of components comprising this layer
	 * @return A Set of IVisibleMapObjects
	 */
	public Set<IVisibleMapObject> getComponents();
	
	/**
	 * Remove all the components from the layer.
	 * The components' visibility is unchanged.
	 */
	public void clear();
	
	/**
	 * Reset all the components' visibility to the layer's visibility.
	 * This is used to restore the visibility setting for any components 
	 * which have been individually mutated.
	 */
	public void reset();
		
		
	/**
	 * Instantiate an empty layer whose visibility is false.
	 * @return An empty invisible layer.
	 */
	public static IMapLayer make() {
		
		return new IMapLayer() {
			
			/**
			 * The common visibility of all the components in this layer.
			 */
			boolean isVisible = false;
			
			/**
			 * The set of all the components in this layer.
			 */
			Set<IVisibleMapObject> components = new HashSet<IVisibleMapObject>();

			
			/**
			 * Set the visibility of all the components in this layer.   
			 * If a particular component's visibility had been individually mutated, 
			 * this method will set its visibility to the given value. 
			 * @param isVisible True if visible, false otherwise
			 */
			@Override
			public void setVisible(boolean isVisible) {
				this.isVisible = isVisible;
				reset();
			}
			
			@Override
			public void reset() {
				components.forEach((comp)->{
					comp.setVisible(isVisible);
				});
			};
			
			/**
			 * Get the visibility of all the components in this layer.  
			 * This may not reflect the actual visibility of a particular component 
			 * if it had been individually mutated. 
			 * @return true if the layer is visible, false otherwise.
			 */
			@Override
			public boolean getVisible() {
				return isVisible;
			};	
			
			@Override
			public boolean addComponent(IVisibleMapObject comp) {
				comp.setVisible(isVisible);
				return components.add(comp);
			}
			
			@Override
			public boolean removeComponent(IVisibleMapObject comp) {
				return components.remove(comp);
			}
			
			@Override
			public void clear() {
				components.clear();
			}

			@Override
			public Set<IVisibleMapObject> getComponents() {
				return new HashSet<IVisibleMapObject>(components);
			}
			
		};
	}
}
