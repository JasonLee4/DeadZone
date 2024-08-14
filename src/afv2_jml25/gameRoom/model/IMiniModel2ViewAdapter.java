package afv2_jml25.gameRoom.model;

import java.util.function.Supplier;

import javax.swing.JComponent;

/**
 * Interface for an adapter from the room-level model to the room-level
 * view/chat room GUI.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMiniModel2ViewAdapter {

	/**
	 * Displays text on the mini view/ chat room GUI
	 * 
	 * @param text string message to be sent
	 */
	void displayTextMessage(String text);

	/**
	 * Displays component on the mini view.
	 * 
	 * @param label     label name of component
	 * @param component GUI component to display
	 * @return executable code to be run for the new component
	 */
	Runnable displayJComponent(String label, Supplier<JComponent> component);

}
