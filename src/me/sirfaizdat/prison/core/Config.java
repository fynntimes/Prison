/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

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
            resetWarningMessage = Prison.color(c
                    .getString("reset-warning-message"));
            resetBroadcastMessage = Prison.color(c
                    .getString("reset-broadcast-message"));
            worlds = c.getStringList("world-list");
            enableMultiworld = c.getBoolean("multiworld");
            fireworksOnRankup = c.getBoolean("fireworks-on-rankup");
            flamesOnAutosmelt = c.getBoolean("flames-on-autosmelt");
        } catch (NullPointerException e) {
            Prison.l.severe("Your configuration is missing a setting or two. Try deleting it and reloading the server.");
        }
    }

}
