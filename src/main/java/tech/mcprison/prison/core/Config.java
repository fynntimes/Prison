/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tech.mcprison.prison.core;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author SirFaizdat
 */
public class Config {

    public String serverPrefix;
    public boolean checkUpdates;
    public boolean optOut;

    public boolean enableMines, enableRanks, enableAutosmelt, enableAutoblock;

    public List<String> worlds;
    public boolean enableMultiworld;

    public int resetTime;
    public List<Integer> resetWarnings;
    public boolean fillMode, asyncReset;
    public String resetWarningMessage;
    public String resetBroadcastMessage;

    public boolean fireworksOnRankup;

    public boolean flamesOnAutosmelt;

    public Config() {
        FileConfiguration c = Prison.i().getConfig();
        loadAll();
    }

    public void reload() {
        Prison.i().reloadConfig();
        loadAll();
    }

    private void loadAll() {
        FileConfiguration c = Prison.i().getConfig();
        try {
            serverPrefix = Prison.color(c.getString("server-prefix") + " &r");
            checkUpdates = c.getBoolean("check-updates");
            optOut = c.getBoolean("opt-out");
            enableMines = c.getBoolean("enable.mines");
            enableRanks = c.getBoolean("enable.ranks");
            enableAutosmelt = c.getBoolean("enable.autosmelt");
            enableAutoblock = c.getBoolean("enable.autoblock");
            resetTime = c.getInt("reset-time");
            resetWarnings = c.getIntegerList("reset-warnings");
            fillMode = c.getBoolean("fill-mode");
            asyncReset = c.getBoolean("async-reset");
            resetWarningMessage = Prison.color(c.getString("reset-warning-message"));
            resetBroadcastMessage = Prison.color(c.getString("reset-broadcast-message"));
            worlds = c.getStringList("world-list");
            enableMultiworld = c.getBoolean("multiworld");
            fireworksOnRankup = c.getBoolean("fireworks-on-rankup");
            flamesOnAutosmelt = c.getBoolean("flames-on-autosmelt");
        } catch (NullPointerException e) {
            Prison.l.severe(
                "Your configuration is missing a setting or two. Try deleting it and reloading the server.");
        }
    }

}
