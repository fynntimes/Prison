/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
