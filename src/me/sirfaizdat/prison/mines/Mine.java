/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.sirfaizdat.prison.core.Prison;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents an individual mine.
 *
 * @author SirFaizdat
 */
public class Mine {

    Mines m;

    public String name;
    public String worldName;
    public World world;
    public int minX, minY, minZ, maxX, maxY, maxZ;

    public int spawnX, spawnY, spawnZ;
    private Location mineSpawn;

    public HashMap<String, Block> blocks = new HashMap<String, Block>();

    public ArrayList<String> ranks;

    File mineFile;

    public boolean worldMissing = false;

    public Mine(String name, String worldName, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int spawnX, int spawnY, int spawnZ, ArrayList<String> ranks) {
        m = Mines.i;
        this.name = name;
        this.worldName = worldName;
        this.world = Prison.i().wm.getWorld(worldName);
        if (world == null) {
            worldMissing = true;
        }
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        mineSpawn = new Location(world, spawnX, spawnY, spawnZ);
        mineFile = new File(Prison.i().getDataFolder(), "/mines/" + name + ".mine");
        this.ranks = ranks;
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
        sm.spawnX = spawnX;
        sm.spawnY = spawnY;
        sm.spawnZ = spawnZ;
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
        List<CompositionEntry> probabilityMap = mapComposition(blocks);
        if (probabilityMap.size() == 0) {
            Prison.l.warning("Mine " + name + " could not regenerate because it has no composition.");
            return false;
        }

        for (Player p : Bukkit.getServer().getOnlinePlayers()) if (withinMine(p.getLocation())) p.teleport(mineSpawn);

        Random r = new Random();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (Prison.i().config.fillMode) {
                        boolean empty = false;
                        try {
                            for (Block blockId : blocks.values()) {
                                // Only if this block is not supposed to be in the mine.
                                if (world.getBlockAt(x, y, z).getTypeId() != blockId.getId()) {
                                    empty = true;
                                    break;
                                }
                            }
//							empty = world.getBlockAt(x, y, z).isEmpty();
                        } catch (NullPointerException e) {
                            Prison.l.severe("The world " + worldName + " could not be found! Mine " + name + " was not reset.");
                            return false;
                        }
                        if (empty) {
                            double chance = r.nextDouble();
                            for (CompositionEntry ce : probabilityMap) {
                                if (chance <= ce.getChance()) {
                                    world.getBlockAt(x, y, z).setTypeIdAndData(ce.getBlock().getId(), (byte) ce.getBlock().getData(), false);
                                    break;
                                }
                            }
                        }
                    } else {
                        // Reset all blocks
                        double chance = r.nextDouble();
                        for (CompositionEntry ce : probabilityMap) {
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
        return true;
    }

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

    // Composition Utilities

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

    public static ArrayList<CompositionEntry> mapComposition(Map<String, Block> compositionIn) {
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

    // FIXES BUG
    void setWorld(World world) {
        this.world = world;
    }

    public void addBlock(Block block, double chance) {
        block.setChance(chance);
        blocks.put(block.toString(), block);
    }

}
