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
	public static boolean checkUpdates;
	
	public static int resetTime;
	public static List<Integer> resetWarnings;
	public static boolean fillMode;
	public static String resetWarningMessage;
	public static String resetBroadcastMessage;

	public static boolean scoreboardsEnabled;
	public static int updateInterval;
	
	public Config() {
		FileConfiguration c = Core.i().getConfig();
		
		serverPrefix = Core.colorize(c.getString("server-prefix") + " &r");
		checkUpdates = c.getBoolean("check-updates");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Core.colorize(c.getString("reset-warning-message"));
		resetBroadcastMessage = Core.colorize(c.getString("reset-broadcast-message"));
		
		scoreboardsEnabled = c.getBoolean("enable-scoreboards");
		updateInterval = c.getInt("update-interval");
	}

}
