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

package me.sirfaizdat.prison.mines.entities;

import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.SerializableMine;
import me.sirfaizdat.prison.mines.events.MineResetEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Represents an individual mine.
 *
 * @author SirFaizdat
 */
public class Mine {

    public String name;
    public World world;
    public int minX, minY, minZ, maxX, maxY, maxZ;
    public HashMap<String, Block> blocks = new HashMap<>();
    public ArrayList<String> ranks;
    public boolean worldMissing = false;
    public File mineFile;
    private String worldName;
    private volatile List<CompositionEntry> cachedCompositionMap;

    public Mine(String name, String worldName, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, ArrayList<String> ranks) {
        this.name = name;
        this.worldName = worldName;
        this.world = Prison.i().getServer().getWorld(worldName);
        if (world == null) {
            worldMissing = true;
        }
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        mineFile = new File(Prison.i().getDataFolder(), "/mines/" + name + ".mine");
        this.ranks = ranks;
    }

    private static ArrayList<CompositionEntry> mapComposition(Map<String, Block> compositionIn) {
        ArrayList<CompositionEntry> probabilityMap = new ArrayList<CompositionEntry>();
        Map<String, Block> composition = new HashMap<String, Block>(compositionIn);
        double max = 0;
        for (Map.Entry<String, Block> entry : composition.entrySet()) {
            max += entry.getValue().getChance();
        }
        if (max < 1) {
            Block air = new Block(0);
            air.setChance(1 - max);
            composition.put(air.toString(), air);
            max = 1;
        }
        double i = 0;
        for (Map.Entry<String, Block> entry : composition.entrySet()) {
            double v = entry.getValue().getChance() / max;
            i += v;
            probabilityMap.add(new CompositionEntry(entry.getValue(), i));
        }
        return probabilityMap;
    }

    public void save() {
        SerializableMine sm = new SerializableMine();
        sm.name = name;
        sm.world = worldName;
        sm.minX = minX;
        sm.minY = minY;
        sm.minZ = minZ;
        sm.maxX = maxX;
        sm.maxY = maxY;
        sm.maxZ = maxZ;
        sm.blocks = blocks;
        for (String s : ranks) {
            sm.ranks.add(s);
        }
        if (mineFile.exists()) {
            mineFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(Prison.i().getDataFolder(), "/mines/" + name + ".mine"));
            ObjectOutputStream oOut = new ObjectOutputStream(out);
            oOut.writeObject(sm);
            oOut.close();
            out.close();
        } catch (IOException e) {
            Prison.l.warning("Failed to save mine " + name + ".");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public boolean reset() {
        if (worldMissing) {
            Prison.l.warning("Mine " + name + " was not reset because the world it was created in (" + worldName + ") can not be found.");
            return false;
        }
        if (cachedCompositionMap == null) cachedCompositionMap = mapComposition(blocks);
        if (cachedCompositionMap.size() == 0) {
            Prison.l.warning("Mine " + name + " could not regenerate because it has no composition.");
            return false;
        }

        for (Player p : Bukkit.getServer().getOnlinePlayers())
            if (withinMine(p.getLocation()))
                p.teleport(p.getLocation().add(0, (Math.max(minY, maxY) - p.getLocation().getBlockY()) + 3, 0));

        Random r = new Random();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (Prison.i().config.fillMode) {
                        if (shouldBeReplaced(world.getBlockAt(x, y, z).getType())) {
                            double chance = r.nextDouble();
                            for (CompositionEntry ce : cachedCompositionMap) {
                                if (chance <= ce.getChance()) {
                                    world.getBlockAt(x, y, z).setTypeIdAndData(ce.getBlock().getId(), (byte) ce.getBlock().getData(), false);
                                    break;
                                }
                            }
                        }
                    } else {
                        // Reset all blocks
                        double chance = r.nextDouble();
                        for (CompositionEntry ce : cachedCompositionMap) {
                            if (chance <= ce.getChance()) {
                                try {
                                    world.getBlockAt(x, y, z).setTypeIdAndData(ce.getBlock().getId(), (byte) ce.getBlock().getData(), false);
                                } catch (NullPointerException e) {
                                    Prison.l.severe("The world " + worldName + " could not be found! Mine " + name + " was not reset.");
                                    return false;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Get the next composition ready
        if (Prison.i().config.asyncReset)
            Prison.i().getServer().getScheduler().runTaskAsynchronously(Prison.i(), new Runnable() {
                @Override
                public void run() {
                    cachedCompositionMap = mapComposition(blocks);
                }
            });

        MineResetEvent event = new MineResetEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return true;
    }

    private boolean shouldBeReplaced(Material material) {
        for (Block block : blocks.values()) if (block.getId() == material.getId()) return false;
        return true;
    }

    // Composition Utilities

    public boolean withinMine(Location l) {
        if (!l.getWorld().getName().equals(world.getName())) {
            return false;
        }

        int x1 = minX - 1;
        int x2 = maxX + 1;
        int z1 = minZ - 1;
        int z2 = maxZ + 1;
        int y1 = minY - 1;
        int y2 = maxY + 1;

        double px = l.getX();
        double pz = l.getZ();
        double py = l.getY();

        if ((px >= x1 && px <= x2) || (px <= x1 && px >= x2)) {
            if ((pz >= z1 && pz <= z2) || (pz <= z1 && pz >= z2)) {
                if ((py >= y1 && py <= y2) || (py <= y1 && py >= y2)) return true;
            }
        }
        return false;
    }

    // FIXES BUG
    void setWorld(World world) {
        this.world = world;
    }

    public void addBlock(Block block, double chance) {
        block.setChance(chance);
        blocks.put(block.toString(), block);
    }

    public static class CompositionEntry {
        private Block block;
        private double chance;

        public CompositionEntry(Block block, double chance) {
            this.block = block;
            this.chance = chance;
        }

        public Block getBlock() {
            return block;
        }

        public double getChance() {
            return chance;
        }
    }

}
