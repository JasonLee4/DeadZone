package afv2_jml25.api;

import java.util.HashSet;
import java.util.UUID;

import common.serverObj.INamedRoomConnection;
import common.serverObj.INamedRoomID;

/**
 * Concrete class that represents a chat room.
 * 
 * @author Jason Lee
 * @author Andres Villada
 */
public class GameRoom {

	/**
	 * Name of this chat room.
	 */
	String name;

	/**
	 * Unique uuid id of this chat room.
	 */
	UUID uuid;

	/**
	 * Roster of this chat room
	 */
	HashSet<INamedRoomConnection> roomRoster = new HashSet<>();

	/**
	 * This dyad for encapsulating room to room connection info
	 */
	INamedRoomConnection localRoomConnection;

	/**
	 * Dyad for the game room
	 */
	INamedRoomID roomIDDyad;

	/**
	 * Constructor for a Game Room
	 * 
	 * @param namedRoomID dyad for the game room
	 */
	public GameRoom(INamedRoomID namedRoomID) {
		this.roomIDDyad = namedRoomID;
		this.name = namedRoomID.getName();
		this.uuid = namedRoomID.getUUID();
	}

	/**
	 * @return the game room dyad
	 */
	public INamedRoomID getNamedRoomID() {
		return this.roomIDDyad;
	}

	/**
	 * Gets the name of this chat room.
	 * 
	 * @return String name of this chat room.
	 */

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	/**
	 * @param roomConnection dyad encapsulating info for room connection
	 */
	public void setLocalRoomConnection(INamedRoomConnection roomConnection) {
		this.localRoomConnection = roomConnection;
	}

	/**
	 * @return this dyad encapsulating info for room connection
	 */
	public INamedRoomConnection getLocalRoomConnection() {
		return this.localRoomConnection;
	}

	/**
	 * @param newRoster updated roster of users connected to this room
	 */
	public void setRoster(HashSet<INamedRoomConnection> newRoster) {
		this.roomRoster = newRoster;
	}

	/**
	 * Gets the UUID of this chat room.
	 * 
	 * @return UUID id of this chat room.
	 */

	public UUID getUUID() {
		// TODO Auto-generated method stub
		return this.uuid;
	}

	/**
	 * @return the roster for this chat room
	 */
	public HashSet<INamedRoomConnection> getRoster() {
		return roomRoster;
	}

	/**
	 * Overridden hashcode method. Used to identify chat room instances by uuid.
	 */
	public int hashCode() {
		return uuid.hashCode();
	}

	/**
	 * Overridden equals method
	 * 
	 * @return true if uuids of chat rooms are equal, false otherwise.
	 */
	public boolean equals(Object o) {
		// return this.uuid.equals(room.getUUID());
		if (null != o) {
			if (o instanceof GameRoom) {
				return uuid.equals(((GameRoom) o).uuid);
			}
		}
		return false;
	}

	/**
	 * Overridden toString method
	 * 
	 * @return the String of the chat room name and uuid
	 */
	public String toString() {
		return this.name + " " + this.uuid.toString();
	}

	/**
	 * Sets the uuid of this chat room.
	 * 
	 * @param uuid id to set to for chat room
	 */

	public void setUUID(UUID uuid) {
		// TODO Auto-generated method stub
		this.uuid = uuid;
	}

}
