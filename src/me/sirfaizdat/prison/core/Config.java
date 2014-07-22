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
		FileConfiguration c = Core.i().getConfig();

		serverPrefix = Core.colorize(c.getString("server-prefix") + " &r");
		checkUpdates = c.getBoolean("check-updates");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Core.colorize(c
				.getString("reset-warning-message"));
		resetBroadcastMessage = Core.colorize(c
				.getString("reset-broadcast-message"));
		rankWorlds = c.getStringList("world-list");
		enableMultiworld = c.getBoolean("multiworld");
	}

	public void reload() {
		Core.i().reloadConfig();
		FileConfiguration c = Core.i().getConfig();

		serverPrefix = Core.colorize(c.getString("server-prefix") + " &r");
		checkUpdates = c.getBoolean("check-updates");
		resetTime = c.getInt("reset-time");
		resetWarnings = c.getIntegerList("reset-warnings");
		fillMode = c.getBoolean("fill-mode");
		resetWarningMessage = Core.colorize(c
				.getString("reset-warning-message"));
		resetBroadcastMessage = Core.colorize(c
				.getString("reset-broadcast-message"));
		enableMultiworld = c.getBoolean("multiworld");
	}

}
