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

package tech.mcprison.prison.core;

import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class ItemManager {

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
            return items.get(Material.matchMaterial(name.toLowerCase()).toString().toLowerCase()
                .replaceAll("_", ""));
        }
        return items.get(name);
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

    public static class ItemSet {

        public int id;
        public short data;

        public ItemSet(int id, short data) {
            this.id = id;
            this.data = data;
        }

    }


    class Bundle {
        public ItemSet set;
        public String name;

        public Bundle(ItemSet set, String name) {
            this.set = set;
            this.name = name;
        }
    }

}
