/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Ranks;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author SirFaizdat
 */
// Considered a component, but not implementing due to class hierarchy.
public class Core extends JavaPlugin {

	// Instance of Core
	private static Core i = null;

	public static Core i() {
		return i;
	}

	public static CoreLogger l = new CoreLogger();

	Mines mines;
	Ranks ranks;

	Economy economy;
	Permission permissions;

	public void onEnable() {
		long startTime = System.currentTimeMillis();
		i = this;
		this.saveDefaultConfig();
		new Config();
		new MessageUtil();
		getServer().getPluginManager().registerEvents(new PlayerList(), this);
		mines = new Mines();
		ranks = new Ranks();
		initEconomy();
		initPermissions();
		checkCompatibility();
		enableMines();
		enableRanks();
		l.info("&2Enabled Prison &6v" + getDescription().getVersion() + "&2. Made by &6SirFaizdat&2." );
		long endTime = System.currentTimeMillis();
		l.info("&6Enabled in " + (endTime - startTime) + "ms.");
	}

	// Initialization
	public void enableMines() {
		if (mines.isEnabled()) {
			try {
				mines.enable();
			} catch (FailedToStartException e) {
				l.severe("Could not start mines.");
			}
			l.info("&2Mines enabled.");
		}
	}

	public void enableRanks() {
		if (ranks.isEnabled()) {
			try {
				ranks.enable();
			} catch (FailedToStartException e) {
				l.severe("Could not start ranks.");
			}
			l.info("&2Ranks enabled.");
		}
	}

	public void initEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			return;
		}
		economy = null;
	}

	public void initPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permissions = permissionProvider.getProvider();
			return;
		}
		permissions = null;
	}

	public void checkCompatibility() {
		if (!hasPlugin("WorldEdit")) {
			mines.setEnabled(false);
			l.warning("Could not enable Mines because WorldEdit is not loaded.");
		}
	}

	public Permission getPermissions() {
		return permissions;
	}
	
	public Economy getEconomy() {
		return economy;
	}
	
	// Utility Methods
	public static String colorize(String text) {
		return text.replaceAll("&", "¤");
	}

	public static boolean hasPlugin(String name) {
		return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
	}

	@Deprecated
	public static Player getPlayer(String name) {
		UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(name));
		Map<String, UUID> response = null;
		try {
			response = fetcher.call();
		} catch (Exception e) {
			Core.l.warning("Could not find UUID for player " + name + ".");
			return null;
		}
		return Bukkit.getPlayer(response.get(name));
	}

}
