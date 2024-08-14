package provided.owlMaps.components.marker;

import java.io.Serializable;
import provided.mixedData.IMixedDataDictionary;

/**
 * Represents a label for a marker
 * @author swong
 *
 */
public interface IMarkerLabel extends Serializable {
	
	/**
	 * Get the color string of the label
	 * @return The color string
	 */
	public String getColor();
	
	/**
	 * Set the color of the label text. Default color is black.
	 * @param colorStr  The new color of the label
	 */
	public void setColor(String colorStr);
	
	/**
	 * Get the font family of the label
	 * @return The font family
	 */
	public String getFontFamily();
	
	/**
	 * Set the font family of the label text (equivalent to the CSS font-family property).
	 * @param fontFamily The font family to use
	 */
	public void setFontFamily(String fontFamily);

	/**
	 * Get the font size of the label text
	 * @return The font size of the label
	 */
	public String getFontSize();
	
	/**
	 * Set the font size of the label text (equivalent to the CSS font-size property). Default size is 14px.
	 * @param fontSize  The new font size
	 */
	public void setFontSize(String fontSize);
	
	/**
	 * Get the font weight of the label text
	 * @return The font weight of the label
	 */
	public String getFontWeight();
	
	/**
	 * Set the font weight of the label text (equivalent to the CSS font-weight property).
	 * @param fontWeight  The font weight
	 */
	public void setFontWeight(String fontWeight);
	
	/**
	 * Get the label text
	 * @return The label text
	 */
	public String getText();
	
	/**
	 * SEt the text to be displayed in the label.
	 * @param text The new text of the label
	 */
	public void setText(String text);
	
	/**
	 * Return a dictionary of IMarkerLabelOption key-values corresponding to this label.
	 * No key-value pair will be generated for any fields that are null.
	 * @return A dictionary of IMarkerLabelOption key-values
	 */
	public IMixedDataDictionary getOptions();
	
	
	/**
	 * Instantiate a new IMarkerLabel instance from the given parameters.
	 * null parameter values will use the default value for that parameter.
	 * @param colorStr The color of the label text. Default color is black.
	 * @param fontFamilyStr The font family of the label text (equivalent to the CSS font-family property).
	 * @param fontSizeStr The font size of the label text (equivalent to the CSS font-size property). Default size is 14px.
	 * @param fontWeightStr The font weight of the label text (equivalent to the CSS font-weight property).
	 * @param textStr The text to be displayed in the label.
	 * @return A new IMarkerLabel instance
	 */
	public static IMarkerLabel make(String colorStr, String fontFamilyStr, String fontSizeStr, String fontWeightStr, String textStr) {
		
		return new IMarkerLabel() {
			
			private static final long serialVersionUID = -4090303903910356789L;

			private String color = colorStr;
			
			private String fontFamily = fontFamilyStr;
			private String fontSize = fontSizeStr;
			private String fontWeight = fontWeightStr;
			private String text = textStr; 

			@Override
			public String getColor() {
				return color;
			}

			@Override
			public void setColor(String color) {
				this.color = color;
			}

			@Override
			public String getFontFamily() {
				return fontFamily;
			}

			@Override
			public void setFontFamily(String fontFamily) {
				this.fontFamily = fontFamily;
			}

			@Override
			public String getFontSize() {
				return fontSize;
			}

			@Override
			public void setFontSize(String fontSize) {
				this.fontSize = fontSize;
			}

			@Override
			public String getFontWeight() {
				return fontWeight;
			}

			@Override
			public void setFontWeight(String fontWeight) {
				this.fontWeight = fontWeight;
			}

			@Override
			public String getText() {

				return text;
			}

			@Override
			public void setText(String text) {
				this.text = text;
			}
			
			@Override
			public IMixedDataDictionary getOptions() {
				IMixedDataDictionary result = IMarkerLabelOptions.makeDefault();
				if(null != color ) result.put(IMarkerLabelOptions.COLOR, color);
				if(null != fontFamily ) result.put(IMarkerLabelOptions.FONT_FAMILY, fontFamily);
				if(null != fontSize ) result.put(IMarkerLabelOptions.FONT_SIZE, fontSize);
				if(null != fontWeight ) result.put(IMarkerLabelOptions.FONT_WEIGHT, fontWeight);
				if(null != text ) result.put(IMarkerLabelOptions.TEXT, text);
				return result;	
			}
		};
	}
	
	/**
	 * Convenience factory method to instantiate a new IMarkerLabel using just a text string 
	 * and default values for all other options
	 * @param text The text of the label
	 * @return A new IMarkerLabel instance
	 */
	public static IMarkerLabel makeFromText(String text) {
		return make(null, null, null, null, text);
	}
	
	/**
	 * Make a new instance of IMarkerLabel given a dictionary with IMarkerLabelOption key-values.
	 * Default values will be used for missing keys.
	 * @param options A dictionary holding IMarkerLabelOption key-values.
	 * @return A new IMarkerLabel instance
	 */
	public static IMarkerLabel makeFromOptions(IMixedDataDictionary options) {
		return make(
			options.get(IMarkerLabelOptions.COLOR),
			options.get(IMarkerLabelOptions.FONT_FAMILY),
			options.get(IMarkerLabelOptions.FONT_SIZE),
			options.get(IMarkerLabelOptions.FONT_WEIGHT),
			options.get(IMarkerLabelOptions.TEXT)
		);
		
	}
	
}
