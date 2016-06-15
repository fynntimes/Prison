/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import me.sirfaizdat.prison.core.Prison;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the loading and saving of mines.
 *
 * @author SirFaizdat
 */
public class MinesManager {

    public HashMap<String, Mine> mines = new HashMap<String, Mine>();

    public int resetTimeCounter;
    int resetTime;
    int autoResetID = -1;

    public MinesManager() {
        File mineRoot = new File(Prison.i().getDataFolder(), "/mines/");
        if (!mineRoot.exists()) {
            mineRoot.mkdir();
        }
        load();
        timer();

    }

    public void timer() {
        resetTime = Prison.i().config.resetTime;
        resetTimeCounter = resetTime;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ResetClock rs = new ResetClock();
        autoResetID = scheduler.scheduleSyncRepeatingTask(Prison.i(), rs,
                1200L, 1200L);
    }

    private void broadcastToWorld(String s, World w) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(w.getName())) {
                p.sendMessage(Prison.colorize(s));
            }
        }
    }

    public void run() {
        if (mines.size() == 0)
            return;
        if (resetTime == 0)
            return;
    }

    public void load() {
        File minesUpdated = new File(Prison.i().getDataFolder(),
                "minesUpdatedAgain.txt");

        for (File file : getAllMineFiles()) {
            SerializableMine sm;
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                sm = (SerializableMine) in.readObject();
                in.close();
                fileIn.close();
            } catch (ClassNotFoundException e) {
                Prison.l.severe("An unexpected error occured. Check to make sure your copy of the plugin is not corrupted.");
                e.printStackTrace();
                continue; // Skip this one
            } catch (IOException e) {
                Prison.l.warning("There was an error in loading file " + file.getName() + ".");
                e.printStackTrace();
                continue; // Skip this one
            }
            Mine m = new Mine(sm.name, sm.world, sm.minX, sm.minY, sm.minZ,
                    sm.maxX, sm.maxY, sm.maxZ, sm.ranks == null ? new ArrayList<String>() : sm.ranks);
            if (sm.blocks != null && sm.blocks.size() != 0) {
                transferComposition(m, sm.blocks);
            }

            // Set the spawn
            if (!minesUpdated.exists()) { // Spawnpoint needs to be generated
                int spawnY;
                if (m.minY < m.maxY) {
                    spawnY = m.maxY;
                } else {
                    spawnY = m.minY;
                }
                spawnY += 5;

                m.spawnX = m.minX;
                m.spawnY = spawnY;
                m.spawnZ = m.minZ;
                m.spawnPitch = 0;
                m.spawnYaw = 0;
                m.mineSpawn = new Location(m.world, m.spawnX, m.spawnY, m.spawnZ, m.spawnPitch, m.spawnYaw);
                m.save();
            } else { // Spawnpoint already is in the mine file (usually the case, unless converting)
                m.spawnX = sm.spawnX;
                m.spawnY = sm.spawnY;
                m.spawnZ = sm.spawnZ;
                m.spawnPitch = sm.spawnPitch;
                m.spawnYaw = sm.spawnYaw;
                m.mineSpawn = new Location(m.world, m.spawnX, m.spawnY, m.spawnZ, m.spawnPitch, m.spawnYaw);
            }

            mines.put(sm.name, m);
        }

        try {
            minesUpdated.createNewFile();
            Prison.l.info("Converted mines to new mines spawn system.");
        } catch (IOException e) {
            Prison.l.severe("Could not create the minesUpdatedAgain.txt file. Please manually create a file in the /plugins/Prison folder called minesUpdatedAgain.txt to avoid data loss.");
        }

        Prison.l.info("&2Loaded " + mines.size() + " mines.");
    }

    private void transferComposition(Mine m, HashMap<String, Block> compo) {
        for (Map.Entry<String, Block> i : compo.entrySet()) {
            m.addBlock(i.getValue(), i.getValue().getChance());
        }
    }

    public void addMine(Mine m) {
        mines.put(m.name, m);
    }

    public Mine getMine(String name) {
        for (String s : mines.keySet()) {
            if (name.equalsIgnoreCase(s)) {
                return mines.get(s);
            }
        }
        return null;
    }

    public HashMap<String, Mine> getMines() {
        return mines;
    }

    public void removeMine(String name) {
        File file = mines.get(name).mineFile;
        mines.remove(name);
        file.delete();
    }

    private File[] getAllMineFiles() {
        File folder = new File(Prison.i().getDataFolder(), "/mines/");
        return folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mine");
            }
        });
    }

    private class ResetClock implements Runnable {
        public void run() {
            if (mines.size() == 0) return;
            if (resetTime == 0) return;
            if (resetTimeCounter > 0) resetTimeCounter--;

            for (int warning : Prison.i().config.resetWarnings) {
                if (warning == resetTimeCounter) {
                    String warnMsg = Prison.i().config.resetWarningMessage;
                    warnMsg = warnMsg.replaceAll("<mins>", warning + "");

                    if (!Prison.i().config.enableMultiworld)
                        Bukkit.getServer().broadcastMessage(Prison.colorize(warnMsg));
                    else for (String s : Prison.i().config.rankWorlds) broadcastToWorld(warnMsg, Bukkit.getWorld(s));
                }
            }
            if (resetTimeCounter == 0) {
                for (Mine mine : mines.values()) {
                    if (!mine.worldMissing) {
                        mine.reset();
                    } else {
                        Prison.l.warning("Did not reset mine "
                                + mine.name
                                + " because the world it is in could not be found.");
                    }
                }
                if (!Prison.i().config.enableMultiworld)
                    Bukkit.getServer().broadcastMessage(Prison.colorize(Prison.i().config.resetBroadcastMessage));
                else for (String s : Prison.i().config.rankWorlds)
                    broadcastToWorld(Prison.i().config.resetBroadcastMessage, Bukkit.getWorld(s));
                resetTimeCounter = resetTime;
            }
        }
    }

}
