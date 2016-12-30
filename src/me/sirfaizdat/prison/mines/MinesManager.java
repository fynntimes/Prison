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
package me.sirfaizdat.prison.mines;

import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.json.JsonMine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.gson.Gson;

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

    public HashMap<String, Mine> mines = new HashMap<>();
    int autoResetID = -1;
    private int resetTimeCounter;
    private int resetTime;

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
                p.sendMessage(Prison.color(s));
            }
        }
    }

    public void load() {
    	boolean convert = Prison.i().config.useJson;
    	if (!convert){
    		Prison.l.warning(ChatColor.RED+"The old mine saving system is deprecated and will is likely to be removed in a future release. Please upgrade to JSON as soon as possible ("+ChatColor.YELLOW+"In the config set "+ChatColor.AQUA+"json "+ChatColor.YELLOW+"to "+ChatColor.AQUA+" true"+ChatColor.RED+")");}
    	for (File mine : getAllNewMineFiles())
        {
        	FileReader fr;
			try {
				fr = new FileReader(mine);
			} catch (FileNotFoundException e) {
                Prison.l.warning("There was an error in loading file " + mine.getName() + ".");
                e.printStackTrace();
                continue; // Skip this one
			}
        	JsonMine jm = new Gson().fromJson(fr, JsonMine.class);
        	HashMap<String, Block> blocks = new HashMap<>();
        	for (Map.Entry<String, Double> entry : jm.blocks.entrySet())
        	{
        		Block b = new Block(Integer.parseInt(entry.getKey().split(":")[0]),Short.parseShort(entry.getKey().split(":")[1]));
        		b.setChance(entry.getValue());
        		blocks.put(entry.getKey(), b);
        	}
        	Mine m = new Mine(jm.name, jm.world, jm.minX, jm.minY, jm.minZ,
                    jm.maxX, jm.maxY, jm.maxZ, jm.ranks == null ? new ArrayList<String>() : jm.ranks);
        	if (blocks != null && blocks.size() != 0) {
                transferComposition(m, blocks);
            }
        	mines.put(jm.name, m);
        }
    	for (File file : getAllMineFiles()) { // Backwards compatability
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
            Mine m = null;
            if (convert) {
            	Prison.l.info("Converting " + sm.name + " to JSON...");
                m = new Mine(sm.name, sm.world, sm.minX, sm.minY, sm.minZ,
                        sm.maxX, sm.maxY, sm.maxZ, sm.ranks == null ? new ArrayList<String>() : sm.ranks);
                m.save(); // Run the 2.3 save function
            	file.renameTo(new File(Prison.i().getDataFolder(),"/mines/"+file.getName().replace(".mine", ".oldmine")));
            }else{
            m = new Mine(sm.name, sm.world, sm.minX, sm.minY, sm.minZ,
                    sm.maxX, sm.maxY, sm.maxZ, sm.ranks == null ? new ArrayList<String>() : sm.ranks);
            if (sm.blocks != null && sm.blocks.size() != 0) {
                transferComposition(m, sm.blocks);
            }}

            mines.put(sm.name, m);
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
    private File[] getAllNewMineFiles() {
        File folder = new File(Prison.i().getDataFolder(), "/mines/");
        return folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
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
                        Bukkit.getServer().broadcastMessage(Prison.color(warnMsg));
                    else for (String s : Prison.i().config.worlds) broadcastToWorld(warnMsg, Bukkit.getWorld(s));
                }
            }
            if (resetTimeCounter == 0) {
                for (Mine mine : mines.values()) {
                    if (!mine.worldMissing) {
                        mine.reset();
                    } else {
                        Prison.l.warning("Did not reset mine "
                                + mine.name + " because the world it is in could not be found.");
                    }
                }
                if (!Prison.i().config.enableMultiworld)
                    Bukkit.getServer().broadcastMessage(Prison.color(Prison.i().config.resetBroadcastMessage));
                else for (String s : Prison.i().config.worlds)
                    broadcastToWorld(Prison.i().config.resetBroadcastMessage, Bukkit.getWorld(s));
                resetTimeCounter = resetTime;
            }
        }
    }

}
