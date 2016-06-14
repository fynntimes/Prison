/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * Supports Bukkit worlds and Multiverse worlds.
 *
 * @author SirFaizdat
 */
public class PWorldManager {

    HashMap<String, World> worlds = new HashMap<String, World>();

    WorldManager wm;

    public PWorldManager() {
        loadWorlds();
        if (getMultiverseCore() != null) {
            wm = new WorldManager(getMultiverseCore());
            loadMultiverseWorlds();
        }
    }

    private void loadWorlds() {
        for (World world : Bukkit.getWorlds()) {
            if (worlds.get(world.getName().toLowerCase()) == null) {
                worlds.put(world.getName().toLowerCase(), world);
            }
        }
    }

    private void loadMultiverseWorlds() {
        if (wm == null) {
            return;
        }
        for (MultiverseWorld world : wm.getMVWorlds()) {
            if (getWorld(world.getName()) == null) {
                worlds.put(world.getName(), world.getCBWorld());
            }
        }
    }

    public World getWorld(String world) {
        return worlds.get(world.toLowerCase());
    }

    private MultiverseCore getMultiverseCore() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }
        return null;
    }

}
