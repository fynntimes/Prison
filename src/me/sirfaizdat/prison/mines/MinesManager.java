/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.sirfaizdat.prison.core.Core;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

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
		File mineRoot = new File(Core.i().getDataFolder(), "/mines/");
		if (!mineRoot.exists()) {
			mineRoot.mkdir();
		}
		load();
		timer();
		
	}
	
	public void timer() {
		resetTime = Core.i().config.resetTime;
		resetTimeCounter = resetTime;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		ResetClock rs = new ResetClock();
		autoResetID = scheduler.scheduleSyncRepeatingTask(Core.i(), rs, 1200L, 1200L);
	}
	
	private class ResetClock implements Runnable {
		public void run() {
			if(mines.size() == 0) return;
			if(resetTime == 0) return;
			if(resetTimeCounter > 0) resetTimeCounter--;
			for(int warning: Core.i().config.resetWarnings) {
				if(warning == resetTimeCounter) {
					String warnMsg = Core.i().config.resetWarningMessage;
					warnMsg = warnMsg.replaceAll("<mins>", warning + "");
					Bukkit.broadcastMessage(warnMsg);
				}
			}
			if(resetTimeCounter == 0) {
//				for(Map.Entry<String, Mine> entry : mines.entrySet()) {
//					entry.getValue().reset();
//				}
				for(Mine mine : mines.values()) {
					if(mine.world == null) {
						mine.setWorld(Bukkit.getWorld(mine.world.getName()));
						mine.save();
					}
					mine.reset();
				}
				Bukkit.broadcastMessage(Core.i().config.resetBroadcastMessage);
				resetTimeCounter = resetTime;
			}
		}
	}

	public void load() {
		ArrayList<String> files = getAllMineFiles();
		if (files.size() == 0 || files == null) {
			Core.l.info("&2Loaded 0 mines! (no mines found)");
			return;
		}
		for (String name : files) {
			SerializableMine sm = null;
			try {
				FileInputStream fileIn = new FileInputStream(new File(Core.i()
						.getDataFolder(), "/mines/" + name));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				sm = (SerializableMine) in.readObject();
				in.close();
				fileIn.close();
			} catch (ClassNotFoundException e) {
				Core.l.severe("An unexpected error occured. Check to make sure your copy of the plugin is not corrupted.");
			} catch (IOException e) {
				Core.l.warning("There was an error in loading file " + name
						+ ".");
			}
			Mine m = new Mine(sm.name, sm.world, sm.minX,
					sm.minY, sm.minZ, sm.maxX, sm.maxY, sm.maxZ);
			if(sm.blocks != null && sm.blocks.size() != 0) {
				transferComposition(m, sm.blocks);
			}
			mines.put(sm.name, m);
		}
		Core.l.info("&2Loaded " + mines.size() + " mines.");
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
		for(String s : mines.keySet()) {
			if(name.equalsIgnoreCase(s)) {
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
	
	public ArrayList<String> getAllMineFiles() {
		ArrayList<String> returnVal = new ArrayList<String>();
		File folder = new File(Core.i().getDataFolder(), "/mines/");
		File[] files = folder.listFiles();
		if (files == null || files.length == 0) {
			return new ArrayList<String>();
		}
		for (File file : files) {
			if (file.isFile()) { // Make sure it isn't directory
				if (file.getName().endsWith(".mine")) {
					returnVal.add(file.getName());
				}
			}
		}
		return returnVal;
	}

}
