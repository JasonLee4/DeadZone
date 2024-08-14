package afv2_jml25.gameApp.mainController;

import provided.rmiUtils.RMIPortConfigWithBoundName;

/**
 * Class for app configs
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class MyAppConfig extends RMIPortConfigWithBoundName {

	/**
	 * Username selected
	 */
	public final String username;

	/**
	 * Constructor for app configs
	 * @param name friendly server name
	 * @param stubPort port
	 * @param classServerPort server port
	 * @param boundName name of the server
	 * @param username name of user
	 */
	public MyAppConfig(String name, int stubPort, int classServerPort, String boundName, String username) {
		super(name, stubPort, classServerPort, boundName);
		this.username = username;
	}
}
