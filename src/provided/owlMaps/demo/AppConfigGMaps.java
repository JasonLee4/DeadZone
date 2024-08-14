package provided.owlMaps.demo;

import provided.config.AppConfig;

public class AppConfigGMaps extends AppConfig {

	public final boolean useDev;
	public String apiKey;

	/**
	 * Consructor for the class
	 * @param name The name of the config
	 * @param apiKey  The GMaps API key to use 
	 * @param useDev if true, show the Chromium dev panel
	 */
	public AppConfigGMaps(String name, String apiKey, boolean useDev) {
		super(name);
		this.apiKey = apiKey;
		this.useDev = useDev;
	}
	
}