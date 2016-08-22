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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A custom configuration class that allows the creation of custom configuration
 * files.
 *
 * @author SirFaizdat
 */
public abstract class Configuration {

    private String name;
    private FileConfiguration config = null;
    private File configFile = null;

    /**
     * @param name The file name of the configuration.
     */
    public Configuration(String name) {
        this.name = name;
    }

    /**
     * Reload the configuration
     */
    public void reload() {
        // If the config does not yet exist
        if (configFile == null) {
            configFile = new File(Prison.i().getDataFolder(), name);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Copy defaults
        InputStream defConfigStream = Prison.i().getResource(name);
        if (defConfigStream != null) {
            @SuppressWarnings("deprecation") YamlConfiguration defConfig =
                YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    /**
     * Get the FileConfiguration object.
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    /**
     * Save the config if it exists.
     */
    public void save() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            Prison.l.severe("Could not save file " + name + " to disk.");
        }
    }

    /**
     * Saves a copy of the default config.
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(Prison.i().getDataFolder(), name);
        }
        if (!configFile.exists()) {
            Prison.i().saveResource(name, false);
        }
    }

}
