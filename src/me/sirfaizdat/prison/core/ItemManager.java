/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;

/**
 * @author SirFaizdat
 */
public class ItemManager {

	public static class ItemSet {

		public int id;
		public short data;

		public ItemSet(int id, short data) {
			this.id = id;
			this.data = data;
		}

	}

	HashMap<String, ItemSet> items;
	ArrayList<Bundle> names;

	boolean loaded = false;

	File backup;

	public ItemManager() {
		items = new HashMap<String, ItemSet>();
		names = new ArrayList<ItemManager.Bundle>();
		
		backup = new File(Prison.i().getDataFolder(), "localitems.csv");
		
		Prison.i().saveResource("localitems.csv", true);
	}

	public void populateLists() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(backup));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			if (!inputLine.startsWith("#")) {
				String[] array = inputLine.split(",");
				String itemName = array[0];
				int id = Integer.parseInt(array[1]);
				short data = Short.parseShort(array[2]);
				items.put(itemName, new ItemSet(id, data));
				names.add(new Bundle(new ItemSet(id, data), itemName));
			}
		}
		in.close();

		setLoaded(true);
	}

	public ItemSet getItem(String name) {
		if (isAnInt(name.replaceAll(":", ""))) {
			return items.get(Material.matchMaterial(name.toLowerCase()).toString().toLowerCase().replaceAll("_", ""));
		}
		return items.get(name);
	}

	class Bundle {
		public ItemSet set;
		public String name;

		public Bundle(ItemSet set, String name) {
			this.set = set;
			this.name = name;
		}
	}

	public String getName(ItemSet set) {
		for (int i = 0; i < names.size(); i++) {
			ItemSet localSet = names.get(i).set;
			if (localSet.id == set.id) {
				if (localSet.data == set.data) {
					return names.get(i).name;
				}
			}
		}
		return set.data != 0 ? set.id + ":" + set.data : set.id + "";
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean isAnInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
