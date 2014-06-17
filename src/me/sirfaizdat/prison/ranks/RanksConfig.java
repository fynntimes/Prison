/**
 * Copyright 2014 GalaxinaRealms
 */
package me.sirfaizdat.prison.ranks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.sirfaizdat.prison.core.Core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RanksConfig {

	private FileConfiguration config = null;
	private File configFile = null;

	public void reload() {
		if (configFile == null) {
			configFile = new File(Core.i().getDataFolder(), "ranks.yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);

		InputStream defConfigStream = Core.i().getResource("ranks.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reload();
		}
		return config;
	}

	public boolean save() {
		if (config == null || configFile == null) {
			return false;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			Core.l.severe("Could not save ranks.yml to " + configFile + ".");
			return false;
		}
		return true;
	}
	
	public void saveDefaultConfig() {
	    if (configFile == null) {
	    	configFile = new File(Core.i().getDataFolder(), "ranks.yml");
	    }
	    if (!configFile.exists()) {            
	         Core.i().saveResource("ranks.yml", false);
	     }
	}

}
