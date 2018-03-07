package org.team3132.rkouchoo.main;

public class Constants {

	/**
	 * All the global static variables referenced by the robot.
	 */
	
	public static final String SSH_USER = "lvuser";
	public static final String SSH_PASSWORD = "";
	public static final String HOST_ADDRESS = "roborio-3132-frc.local";
	
	public static final int SSH_PORT = 22;

	public static final String SSH_REMOTE_FILE = "/home/lvuser/RobotConfig.txt";
	
	public static enum dataType {
		STRING,
		BOOLEAN,
		LONG,
		DOUBLE,
		JSONArray,
		INVALID
	}
}
