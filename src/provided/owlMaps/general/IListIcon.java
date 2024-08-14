package provided.owlMaps.general;

import java.util.List;

import provided.mixedData.IMixedDataDictionary;

/**
 * Represents a List explicitly of Icon options (IMixedDataDictionaries)
 * Used for situations where the type of the generic parameter needs to be known, e.g. for JSON encoding/decoding
 * @author swong
 *
 */
public interface IListIcon extends List<IMixedDataDictionary> {

}
