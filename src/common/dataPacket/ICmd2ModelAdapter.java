package common.dataPacket;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;

import common.dataPacket.data.IRoomConnectionData;
import common.serverObj.INamedRoomConnection;
import provided.logger.ILogger;
import provided.mixedData.MixedDataKey;


/**
 * Adapter from a command to the local model, to be set when a new command is
 * received.
 * 
 * @author Group C
 */
public interface ICmd2ModelAdapter {

	/**
	 * Displays the input text message to the local GUI.
	 * 
	 * @param text text message to display
	 */
	public void displayText(String text);

	/**
	 * Displays the input panel to the local GUI.
	 * 
	 * @param label Label for component.
	 * @param component Supplier of component to display.
	 * @return a Runnable (can be used for closing the component)
	 */
	public Runnable displayJComponent(String label, Supplier<JComponent> component);

	/**
	 * @return username of the connected chatroom model
	 */
	public String getUsername();

	/**
	 * @return chatroom friendly name of the connected chatroom model
	 */
	public String getRoomName();

	/**
	 * Send a message to the chatroom without datapackets. The local machine should
	 * wrap this data into a DataPacket and send it to every dyad in the chatroom.
	 * 
	 * @param <T>  The type of the message being sent. Bounded by
	 *             IRoomConnectionData since this is being sent at the room level
	 * @param data The data of the message being sent.
	 */
	public <T extends IRoomConnectionData> void sendMessageToRoom(T data);

	/**
	 * Send a message to a specific dyad-stub in a chatroom. The local machine
	 * should wrap this message into a DataPacket and then send it to the specified
	 * dyad.
	 * 
	 * @param <T>  The type of the message being sent. Bounded by
	 *             IRoomConnectionData since this is being sent at the room level
	 * @param data The data of the message being sent.
	 * @param dyad The dyad-stub this message is being sent to.
	 */
	public <T extends IRoomConnectionData> void sendMessageToDyad(T data, INamedRoomConnection dyad);

	/**
	 * Stores a value in the local mixed-key dictionary.
	 * 
	 * @param <T>   The type of the value being stored.
	 * @param key   The key for the value being stored.
	 * @param value The value being stored.
	 * @return The value just put at the key
	 */
	public <T> T putLocalData(MixedDataKey<T> key, T value);

	/**
	 * Gets a value from the local mixed-key dictionary.
	 * 
	 * @param <T> The type of the value being stored.
	 * @param key The key for the desired value.
	 * @return The desired value, based on the key.
	 */
	public <T> T getLocalData(MixedDataKey<T> key);
	
	/**
	 * Gets all keys with the given id.
	 * 
	 * @param id - the id to find keys for
	 * @return a Set of all keys in the local storage with the given id
	 */
	public Set<MixedDataKey<?>> getLocalDataKeys(UUID id);

	/**
	 * Gets the API key for running the google maps api
	 * 
	 * @return an api key
	 */
	public String getAPIKey();

	/**
	 * Gets the local logger.
	 * 
	 * @return An ILogger for the command to use.
	 */
	public ILogger getLocalLogger();

}
