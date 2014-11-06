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

	public String serverPrefix;
	public boolean checkUpdates;

	public int resetTime;
	public List<Integer> resetWarnings;
	public boolean fillMode;
	public String resetWarningMessage;
	public String resetBroadcastMessage;
	
	public List<String> rankWorlds;
	public boolean enableMultiworld;

	public Config() {
		FileConfiguration c = Prison.i().getConfig();

		serverPrefix = Prison.colorize(c.getString("server-prefix"));
		checkUpdates = c.getBoolean("check-updates");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Prison.colorize(c
				.getString("reset-warning-message"));
		resetBroadcastMessage = Prison.colorize(c
				.getString("reset-broadcast-message"));
		rankWorlds = c.getStringList("world-list");
		enableMultiworld = c.getBoolean("multiworld");
	}

	public void reload() {
		Prison.i().reloadConfig();
		FileConfiguration c = Prison.i().getConfig();

		serverPrefix = Prison.colorize(c.getString("server-prefix"));
		checkUpdates = c.getBoolean("check-updates");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Prison.colorize(c
				.getString("reset-warning-message"));
		resetBroadcastMessage = Prison.colorize(c
				.getString("reset-broadcast-message"));
		enableMultiworld = c.getBoolean("multiworld");
	}

}
