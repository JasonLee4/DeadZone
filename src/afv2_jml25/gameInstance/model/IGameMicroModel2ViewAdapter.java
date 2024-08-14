package afv2_jml25.gameInstance.model;

import java.util.function.Supplier;

import javax.swing.JComponent;

/**
 * Model to view adapter for the game-level MVC system
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public interface IGameMicroModel2ViewAdapter {

	/**
	 * Adds a map factory component to the game view
	 * 
	 * @param mapFactory factory for generating maps
	 * @param string     label for this new component
	 */
	void addMapComp(Supplier<JComponent> mapFactory, String string);

	/**
	 * Enables or disables the game controller
	 * 
	 * @param enable turns on input components for the game-view if true. Otherwise,
	 *               disables input components
	 */
	void enableInputComponents(boolean enable);

	/**
	 * Displays a loss status for players that have been eliminated
	 */
	void displayLossPanel();

	/**
	 * Updates the game status label
	 */
	void roundTimer();

	/**
	 * Updates the game status display of the player
	 * 
	 * @param status the new game status
	 */
	void updateGameStatus(String status);
}
