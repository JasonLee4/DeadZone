package afv2_jml25.gameInstance.view;

/**
 * View to model adapter for the game-level MVC system
 * 
 * @author Andres Villada (afv2)
 * @author James Lee (jml25)
 */
public interface IGameMicroView2ModelAdapter {
	/**
	 * Quits out of the game
	 */
	public void quit();

	/**
	 * @param country user-selected country
	 */
	public void sendCountry(String country);

}
