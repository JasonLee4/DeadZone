package afv2_jml25.api;

import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;

import afv2_jml25.gameInstance.IGameMicroAdapter;
import afv2_jml25.gameInstance.model.GameMicroModel;
import afv2_jml25.gameInstance.model.IGameMicroModel2ViewAdapter;
import afv2_jml25.gameInstance.view.GameMicroView;
import afv2_jml25.gameInstance.view.IGameMicroView2ModelAdapter;
import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import common.serverObj.INamedRoomConnection;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Command that initializes a game instance
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public class GameInitAlgoCmd extends ARoomDataPacketAlgoCmd<IGameInitData> {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -3203284951256134521L;
	/**
	 * Unique ID for this game instance
	 */
	private UUID gameUUID;
	/**
	 * Model for the game-level MVC system
	 */
	private GameMicroModel gameMicroModel;
	/**
	 * View for the game-level MVC system
	 */
	private GameMicroView gameMicroView;
	/**
	 * Dyad of the game server
	 */
	private INamedRoomConnection serverRoomConnection;

	/**
	 * Constructor is called by the game server because that's who instantiates the
	 * game commands.
	 * 
	 * @param gameUUID      The UUID identifying the current instance of the game
	 *                      server.
	 * @param namedReceiver Dyad of the game server
	 */
	public GameInitAlgoCmd(UUID gameUUID, INamedRoomConnection namedReceiver) {
		this.gameUUID = gameUUID;
		this.serverRoomConnection = namedReceiver;
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<IGameInitData> host, Void... params) {

		// The name and signature of the method may vary as it is defined by the API.
		getCmd2ModelAdpt().displayJComponent("Dead Zone", () -> {
			// This command is essentially the micro-controller, so there's
			// no real need for an explicit named class for the micro-controller.
			// The typical controller's constructor code is just being written out here:

			// Instantiate the model and view components with their normal adapters.
			// The adapter code is elided here as it is game-dependent. Use adapter names
			// that fit the game implemenation.
			gameMicroModel = new GameMicroModel(host.getData().getPlayer(), gameUUID, serverRoomConnection,
					new IGameMicroModel2ViewAdapter() {

						@Override
						public void addMapComp(Supplier<JComponent> mapFactory, String string) {
							gameMicroView.addComponent(mapFactory, string);
						}

						@Override
						public void enableInputComponents(boolean val) {
							gameMicroView.enableInputAndButton(val);

						}

						@Override
						public void displayLossPanel() {
							gameMicroView.genLossPanel();
						}

						@Override
						public void roundTimer() {
							gameMicroView.roundTimer();
						}

						@Override
						public void updateGameStatus(String status) {
							gameMicroView.updateGameStatus(status);
							
						}

					}, getCmd2ModelAdpt());
			gameMicroView = new GameMicroView(new IGameMicroView2ModelAdapter() {

				@Override
				public void quit() {
					gameMicroModel.eliminatePlayer();
					
				}

				@Override
				public void sendCountry(String country) {
					// TODO Auto-generated method stub
					gameMicroModel.processCountry(country);
				}

			});

			// Start the model and view
			gameMicroModel.start();
			gameMicroView.start();

			// Create the adapter(s) to the micro-MVC. Make as many as needed.
			IGameMicroAdapter gameAdpt = new IGameMicroAdapter() {
				// methods that access the micro-model/view
			};

			// Make the keys to the game adapters. Use an informative description string.
			MixedDataKey<IGameMicroAdapter> adapterKey = new MixedDataKey<IGameMicroAdapter>(gameUUID,
					"IGameMicroAdapter", IGameMicroAdapter.class);
			// Make a unique key for each adapter being stored.

			// Store the adapter(s) in the local storage for other game-commands to
			// retrieve.
			// The name and signature of the method may vary as it is defined by the API.
			getCmd2ModelAdpt().putLocalData(adapterKey, gameAdpt);
			// Add as many adapters to the local storage as needed, each with their own key.

			return gameMicroView; // Return the micro-view so that it will be displayed on the client's UI.
		});

		return null;
	}

}
