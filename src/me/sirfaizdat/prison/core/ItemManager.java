/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author SirFaizdat
 */
public class ItemManager {

	HashMap<String, ItemSet> items;
	ArrayList<Bundle> names;
	
	boolean loaded = false;

	public ItemManager() {
		items = new HashMap<String, ItemSet>();
		names = new ArrayList<ItemManager.Bundle>();
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
			Core.l.severe("Could not read item list! This may cause errors.");
			e.printStackTrace();
		}
		setLoaded(true);
	}

	public ItemSet getItem(String name) {
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
		for(int i = 0; i < names.size(); i++) {
			ItemSet localSet = names.get(i).set;
			if(localSet.id == set.id) {
				if(localSet.data == set.data) {
					return names.get(i).name;
				}
			}
		}
		return null;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
