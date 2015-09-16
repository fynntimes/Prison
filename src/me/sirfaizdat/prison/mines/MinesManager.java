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

import me.sirfaizdat.prison.core.Prison;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Manages the loading and saving of mines.
 * 
 * @author SirFaizdat
 */
public class MinesManager {

	public HashMap<String, Mine> mines = new HashMap<String, Mine>();

	public ArrayList<String> worldsWhereMinesArePresent = new ArrayList<String>();

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
				p.sendMessage(s);
			}
		}
	}

	public void run() {
		if (mines.size() == 0)
			return;
		if (resetTime == 0)
			return;
	}

	private class ResetClock implements Runnable {
		public void run() {
			if (mines.size() == 0)
				return;
			if (resetTime == 0)
				return;
			if (resetTimeCounter > 0)
				resetTimeCounter--;
			for (int warning : Prison.i().config.resetWarnings) {
				if (warning == resetTimeCounter) {
					String warnMsg = Prison.i().config.resetWarningMessage;
					warnMsg = warnMsg.replaceAll("<mins>", warning + "");
					for (String s : worldsWhereMinesArePresent) {
						broadcastToWorld(warnMsg, Bukkit.getWorld(s));
					}
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
				Bukkit.broadcastMessage(Prison.i().config.resetBroadcastMessage);
				resetTimeCounter = resetTime;
			}
		}
	}

	public void load() {
		File minesUpdated = new File(Prison.i().getDataFolder(),
				"minesupdated.txt");

		ArrayList<String> files = getAllMineFiles();
		if (files.size() == 0 || files == null) {
			Prison.l.info("&2Loaded 0 mines! (no mines found)");
			return;
		}
		for (String name : files) {
			SerializableMine sm = null;
			try {
				FileInputStream fileIn = new FileInputStream(new File(Prison
						.i().getDataFolder(), "/mines/" + name));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				sm = (SerializableMine) in.readObject();
				in.close();
				fileIn.close();
			} catch (ClassNotFoundException e) {
				Prison.l.severe("An unexpected error occured. Check to make sure your copy of the plugin is not corrupted.");
			} catch (IOException e) {
				Prison.l.warning("There was an error in loading file " + name
						+ ".");
			}
			Mine m = new Mine(sm.name, sm.world, sm.minX, sm.minY, sm.minZ,
					sm.maxX, sm.maxY, sm.maxZ, sm.spawnX, sm.spawnY, sm.spawnZ,
					sm.ranks == null ? new ArrayList<String>() : sm.ranks);
			if (sm.blocks != null && sm.blocks.size() != 0) {
				transferComposition(m, sm.blocks);
			}
			if (!minesUpdated.exists()) {
				int spawnX = m.minX, spawnY, spawnZ = m.minZ;
				if (m.minY < m.maxY) {
					spawnY = m.maxY;
				} else {
					spawnY = m.minY;
				}
				m.spawnX = spawnX;
				m.spawnY = spawnY;
				m.spawnZ = spawnZ;
				m.save();
				try {
					minesUpdated.createNewFile();
				} catch (IOException e) {
					Prison.l.severe("Could not create the minesupdated.txt file. Please manually create a file in the /plugins/Prison folder called minesupdated.txt to avoid data loss.");
				}
				Prison.l.info("Converted mines to new mines spawn system.");
			}
			mines.put(sm.name, m);
			if (!isInList(sm.world)) {
				worldsWhereMinesArePresent.add(sm.world);
			}
		}
		Prison.l.info("&2Loaded " + mines.size() + " mines.");
	}

	private boolean isInList(String name) {
		for (String s : worldsWhereMinesArePresent) {
			if (s.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	private void transferComposition(Mine m, HashMap<String, Block> compo) {
		for (Map.Entry<String, Block> i : compo.entrySet()) {
			m.addBlock(i.getValue(), i.getValue().getChance());
		}
	}

	public void addMine(Mine m) {
		mines.put(m.name, m);
		if (!isInList(m.world.getName())) {
			worldsWhereMinesArePresent.add(m.world.getName());
		}
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

	public ArrayList<String> getAllMineFiles() {
		ArrayList<String> returnVal = new ArrayList<String>();
		File folder = new File(Prison.i().getDataFolder(), "/mines/");
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
