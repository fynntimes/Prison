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

/**
 * Copyright 2014 GalaxinaRealms
 */
package tech.mcprison.prison.ranks;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.core.Prison;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RanksConfig {

    private FileConfiguration config = null;
    private File configFile = null;

    public void reload() {
        if (configFile == null) {
            configFile = new File(Prison.i().getDataFolder(), "ranks.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = Prison.i().getResource("ranks.yml");
        if (defConfigStream != null) {
            @SuppressWarnings("deprecation") YamlConfiguration defConfig =
                YamlConfiguration.loadConfiguration(defConfigStream);
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
            Prison.l.severe("Could not save ranks.yml to " + configFile + ".");
            return false;
        }
        return true;
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(Prison.i().getDataFolder(), "ranks.yml");
        }
        if (!configFile.exists()) {
            Prison.i().saveResource("ranks.yml", false);
        }
    }

}
