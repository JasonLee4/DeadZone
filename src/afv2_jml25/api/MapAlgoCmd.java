package afv2_jml25.api;

import java.util.function.Supplier;

import javax.swing.JComponent;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.control.IOwlMapControl;
import provided.owlMaps.map.IOwlMap;

/**
 * Command for sending map initialization with algo configurations
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public class MapAlgoCmd extends ARoomDataPacketAlgoCmd<IMapData> {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -606525181210610442L;

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<IMapData> host, Void... params) {
		// Options to render the map with
		IMixedDataDictionary mapOptions = host.getData().getMapOptions();

		// Declare the entire map in the factory but do NOT run it yet
		Supplier<JComponent> mapFactory = IOwlMapControl.makeMapFactory(getCmd2ModelAdpt().getAPIKey(), mapOptions,
				(owlMapControl) -> {

					// Your initial processing of the map here...

					IOwlMap map = owlMapControl.getMap(); // map is not available until now

					// Your map control initializations here...

				}, getCmd2ModelAdpt().getLocalLogger());

		// Display the map on the view
		getCmd2ModelAdpt().displayJComponent("Sent Map", mapFactory);
		return null;
	}

}
