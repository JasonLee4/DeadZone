package common.serverObj;

import java.io.Serializable;

import java.util.UUID;

/**
 * Serializable dyad encapsulating a friendly String name and a PubSubSync
 * channel UUID representing a chatroom.
 * 
 * @author Group C
 */
public interface INamedRoomID extends Serializable {

	/**
	 * Gets the friendly name associated with this dyad.
	 * 
	 * @return the friendly name
	 */
	public String getName();

	/**
	 * Gets the UUID associated with this dyad.
	 * 
	 * @return the PubSubSync channel UUID of this dyad
	 */
	public UUID getUUID();

	/**
	 * Factory method that constructs a concrete INamedRoomID object.
	 * 
	 * @param roomName - the name to associated with the room UUID
	 * @param roomID   - the UUID of the PubSubSync channel representing the room
	 * @return a concrete instance of INamedRoomID
	 */
	public static INamedRoomID make(String roomName, UUID roomID) {
		return new INamedRoomID() {
			/**
			 * Generated serialVersionUID for INamedRoomID make()
			 */
			private static final long serialVersionUID = 716823821180789843L;

			@Override
			public String getName() {
				return roomName;
			}

			@Override
			public UUID getUUID() {
				return roomID;
			}

			@Override
			public String toString() {
				return this.getName();
			}

			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof INamedRoomID)) {
					return false;
				}
				return roomID.equals(((INamedRoomID) obj).getUUID());
			}

			@Override
			public int hashCode() {
				return roomID.hashCode();
			}
		};
	}

}
