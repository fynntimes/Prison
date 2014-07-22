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

import me.sirfaizdat.prison.core.Core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
	public World world;
	public int minX, minY, minZ, maxX, maxY, maxZ;

	public HashMap<String, Block> blocks = new HashMap<String, Block>();

	File mineFile;

	public Mine(String name, World world, int minX, int minY, int minZ,
			int maxX, int maxY, int maxZ) {
		m = Mines.i;
		this.name = name;
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		mineFile = new File(Core.i().getDataFolder(), "/mines/" + name
				+ ".mine");
	}

	public void save() {
		SerializableMine sm = new SerializableMine();
		sm.name = name;
		sm.world = world.getName();
		sm.minX = minX;
		sm.minY = minY;
		sm.minZ = minZ;
		sm.maxX = maxX;
		sm.maxY = maxY;
		sm.maxZ = maxZ;
		sm.blocks = blocks;
		if (mineFile.exists()) {
			mineFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(Core.i()
					.getDataFolder(), "/mines/" + name + ".mine"));
			ObjectOutputStream oOut = new ObjectOutputStream(out);
			oOut.writeObject(sm);
			oOut.close();
			out.close();
		} catch (IOException e) {
			Core.l.warning("Failed to save mine " + name + ".");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void reset() {
		List<CompositionEntry> probabilityMap = mapComposition(blocks);
		if (probabilityMap.size() == 0) {
			Core.l.warning("Mine " + name
					+ " could not regenerate because it has no composition.");
			return;
		}
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			Location l = p.getLocation();
			if (withinMine(l)) {
				if (maxY > minY) {
					p.teleport(new Location(world, l.getX(), maxY + 1, l.getZ()));
				} else if (minY > maxY) {
					p.teleport(new Location(world, l.getX(), minY + 1, l.getZ()));
				}

			}
		}
		Random r = new Random();
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (Core.i().config.fillMode) {
						// Only fill in air blocks.
						if (world.getBlockAt(x, y, z).getType() == Material.AIR
								|| world.getBlockAt(x, y, z).getType() == Material.TORCH
								|| world.getBlockAt(x, y, z).getType() == Material.REDSTONE_TORCH_ON
								|| world.getBlockAt(x, y, z).getType() == Material.REDSTONE_TORCH_OFF) {
							double c = r.nextDouble();
							for (CompositionEntry ce : probabilityMap) {
								if (c <= ce.getChance()) {
									world.getBlockAt(x, y, z).setTypeIdAndData(
											ce.getBlock().getId(),
											ce.getBlock().getData(), false);
									break;
								}
							}

						} else {
							// Just reset the entire thing
							double c = r.nextDouble();
							for (CompositionEntry ce : probabilityMap) {
								if (c <= ce.getChance()) {
									world.getBlockAt(x, y, z).setTypeIdAndData(
											ce.getBlock().getId(),
											ce.getBlock().getData(), false);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean withinMine(Location l) {
		return l.getWorld().equals(world)
				&& (l.getX() + 1 >= minX && l.getX() + 1 <= maxX)
				&& (l.getY() + 1 >= minY && l.getY() + 1 <= maxY)
				&& (l.getZ() + 1 >= minZ && l.getZ() + 1 <= maxZ);
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

	public static ArrayList<CompositionEntry> mapComposition(
			Map<String, Block> compositionIn) {
		ArrayList<CompositionEntry> probabilityMap = new ArrayList<CompositionEntry>();
		Map<String, Block> composition = new HashMap<String, Block>(
				compositionIn);
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

	public void addBlock(Block block, double chance) {
		block.setChance(chance);
		blocks.put(block.toString(), block);
	}
}
