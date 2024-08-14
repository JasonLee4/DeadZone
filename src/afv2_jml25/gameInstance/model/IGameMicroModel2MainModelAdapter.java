package afv2_jml25.gameInstance.model;

/**
 * Model to model adapter from the game-level to the app-level
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IGameMicroModel2MainModelAdapter {

	/**
	 * Checks to see if someone has won the game (last team standing)
	 */
	public void checkWinner();

}
