package afv2_jml25.gameApp.mainModel;

import afv2_jml25.api.GameRoom;

/**
 * Interface for model to mini-model adapter
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMainModel2MiniModelAdapter {
	/**
	 * Unsubscribe from data channel method
	 */
	public void unsubscribe();

	/**
	 * Initializes a team for the game and enables them to an active play status
	 * 
	 * @param lobby game lobby for a team
	 */
	public void initTeamStatusMap(GameRoom lobby);

}
