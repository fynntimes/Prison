/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author SirFaizdat
 */
public class Config {
	
	public static String serverPrefix;
	
	public static int resetTime;
	public static List<Integer> resetWarnings;
	public static boolean fillMode;
	public static String resetWarningMessage;
	public static String resetBroadcastMessage;
	
	public Config() {
		FileConfiguration c = Core.i().getConfig();
		
		serverPrefix = Core.colorize(c.getString("server-prefix") + " &r");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Core.colorize(c.getString("reset-warning-message"));
		resetBroadcastMessage = Core.colorize(c.getString("reset-broadcast-message"));
	}

}
