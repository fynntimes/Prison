/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.sirfaizdat.prison.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;

/**
 * @author SirFaizdat
 */
public class ItemManager {

	public static class ItemSet {

		public int id;
		public byte data;

		public ItemSet(int id, byte data) {
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

		// Create backup
		backup = new File(Core.i().getDataFolder(), "localitems.csv");
		if (!backup.exists()) {
			Core.i().saveResource("localitems.csv", false);
		}
	}

	public void populateLists() {
		try {

			URL news = new URL("http://mcprison.netne.net/res/items.csv");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					news.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				if (!inputLine.startsWith("#")) {
					String[] array = inputLine.split(",");
					String itemName = array[0];
					int id = Integer.parseInt(array[1]);
					byte data = Byte.parseByte(array[2]);
					items.put(itemName, new ItemSet(id, data));
					names.add(new Bundle(new ItemSet(id, data), itemName));
				}
			}

			in.close();
		} catch (Exception e) {
			Core.l.warning("Could not read item list from internet! Attempting to use local items.csv...");
			Core.l.info("While this lets you use item names, the local list is not as up-to-date with the latest Minecraft blocks as the online version is.");

			try {
				BufferedReader in = new BufferedReader(new FileReader(backup));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					if (!inputLine.startsWith("#")) {
						String[] array = inputLine.split(",");
						String itemName = array[0];
						int id = Integer.parseInt(array[1]);
						byte data = Byte.parseByte(array[2]);
						items.put(itemName, new ItemSet(id, data));
						names.add(new Bundle(new ItemSet(id, data), itemName));
					}
				}
				in.close();
			} catch (Exception e1) {
				Core.l.severe("Could not read local item list! This may cause errors.");
				setLoaded(false);
				return;
			}

		}
		setLoaded(true);
	}

	public ItemSet getItem(String name) {
		if(isAnInt(name.replaceAll(":", ""))) {
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
