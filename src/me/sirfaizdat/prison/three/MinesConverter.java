/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2017  SirFaizdat
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

package me.sirfaizdat.prison.three;

import com.google.gson.GsonBuilder;
import me.sirfaizdat.prison.core.Config;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.entities.Mine;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * A converter that converts Prison 2 Mines to Prison 3 Mines
 */
public class MinesConverter implements Converter<Mine> {
    @Override public String convert(Mine object) {
        ThreeMine prison3 = new ThreeMine();
        prison3.minX = object.minX;
        prison3.minY = object.minY;
        prison3.minZ = object.minZ;
        prison3.maxX = object.maxX;
        prison3.maxY = object.maxY;
        prison3.maxZ = object.maxZ;
        prison3.name = object.name;
        if (!object.worldMissing) {
            prison3.worldName = object.world.getName();
        } else {
            Prison.i().getLogger().warning("The world for mine " + object.name
                + " is missing. It will not be converted to Prison 3 format.");
            return null;
        }
        prison3.spawnX = 0;
        prison3.spawnY = 0;
        prison3.spawnZ = 0;
        prison3.pitch = 0;
        prison3.yaw = 0;
        prison3.blocks = new ArrayList<>();
        for (Map.Entry<String, Block> entry : object.blocks.entrySet()) {
            prison3.blocks.add(new ThreeBlock(BlockType.getBlock(entry.getValue().getId()),
                ((Double) (entry.getValue().getChance() * 100)).intValue()));
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(prison3);
    }

    @Override public void convertAndSave(Mine object) {
        File mineRoot = new File(Prison.i().getDataFolder(), "/Mines/mines/");
        mineRoot.mkdirs();
        try (PrintWriter out = new PrintWriter(new File(mineRoot, object.name + ".json"))) {
            out.println(convert(object));
        } catch (Exception e) {
            Prison.i().getLogger()
                .log(Level.WARNING, "Failed to convert mine " + object.name + " to Prison 3 format",
                    e);
        }
    }

    public class ThreeMine {
        public int minX, minY, minZ, maxX, maxY, maxZ;
        public double spawnX, spawnY, spawnZ;
        public float pitch, yaw;
        public String worldName, name;
        public boolean hasSpawn = false;
        public List<ThreeBlock> blocks;
    }


    public class ThreeBlock {
        public BlockType type;
        public int chance;

        public ThreeBlock(BlockType blockType, int chance) {
            this.type = blockType;
            this.chance = chance;
        }

        public ThreeBlock() {
        } // GSON
    }


    public class ConfigConverter implements Converter<Config> {

        public class ThreeConfig {
            public boolean asyncReset = true;
            public boolean resetMessages = true;
            public boolean multiworld = false;
            public boolean savePlayers = false;
            public int aliveTime = 600;
            public String guiName = "&4Prison";
            public List<String> worlds =
                new ArrayList<>(Arrays.<String>asList(new String[] {"plots", "mines"}));
            public List<Integer> resetWarningTimes =
                new ArrayList<>(Arrays.<Integer>asList(new Integer[] {600, 300, 60}));
            public String resetMessage = "&b[&5Prison&b] &2Mines have been reset!";
            public String resetWarning =
                "&b[&5Prison&b] &2Mines are going to reset in &6%mins% &2mins";
        }

        @Override public String convert(Config object) {
            ThreeConfig config = new ThreeConfig();
            config.aliveTime = object.resetTime;
            config.asyncReset = object.asyncReset;
            config.resetMessage = object.resetBroadcastMessage;
            config.resetWarning = object.resetWarningMessage;
            config.resetWarningTimes = object.resetWarnings;
            config.multiworld = object.enableMultiworld;
            config.worlds = object.worlds;
            return new GsonBuilder().setPrettyPrinting().create().toJson(config);
        }

        @Override public void convertAndSave(Config object) {
            File mineRoot = new File(Prison.i().getDataFolder(), "/Mines/");
            mineRoot.mkdirs();
            try (PrintWriter out = new PrintWriter(new File(mineRoot, "config.json"))) {
                out.println(convert(object));
            } catch (Exception e) {
                Prison.i().getLogger()
                    .log(Level.WARNING, "Failed to create Prison-Mines config", e);
            }
        }
    }
}
