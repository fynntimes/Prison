/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.io.File;
import java.io.IOException;

import me.sirfaizdat.prison.core.Updater.UpdateResult;
import me.sirfaizdat.prison.core.Updater.UpdateType;
import me.sirfaizdat.prison.core.cmds.PrisonCommandManager;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Ranks;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/** @author SirFaizdat */
// Considered a component, but not implementing due to class hierarchy.
public class Prison extends JavaPlugin implements Listener {

	// Instance of Core
	private static Prison i = null;

	public static Prison i() {
		return i;
	}

	public static PrisonLogger l = new PrisonLogger();

	public Mines mines;
	public Ranks ranks;

	Economy economy;
	Permission permissions;

	public PlayerList playerList;
	public Config config;
	public ItemManager im;
	public PWorldManager wm;
	boolean updateAvailable = false;
	String updateLatestName;

	public File file;

	public void onEnable() {
		long startTime = System.currentTimeMillis();
		i = this;
		this.saveDefaultConfig();
		config = new Config();
		im = new ItemManager();
		wm = new PWorldManager();
		new MessageUtil();
		playerList = new PlayerList();
		getServer().getPluginManager().registerEvents(playerList, this);
		mines = new Mines();
		ranks = new Ranks();
		initEconomy();
		initPermissions();
		checkCompatibility();
		enableMines();
		enableRanks();
		file = getFile();
		getCommand("prison").setExecutor(new PrisonCommandManager());
		new AutoSmelt();
		new BlockCommand();
		getServer().getPluginManager().registerEvents(this, this);
		l.info("&2Enabled Prison &6v" + getDescription().getVersion() + "&2. Made by &6SirFaizdat&2.");
		long endTime = System.currentTimeMillis();
		l.info("&6Enabled in " + (endTime - startTime) + "ms.");
		if (config.checkUpdates && !getDescription().getVersion().contains("dev")) {
			Updater updater = new Updater(this, 76155, this.getFile(), UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				updateLatestName = updater.getLatestName();
				l.info(MessageUtil.get("general.updateAvailable", updateLatestName));
				this.updateAvailable = true;
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.isOp() || p.hasPermission("prison.manage")) {
						p.sendMessage(MessageUtil.get("general.updateAvailable", updateLatestName));
					}
				}
			}
		}
		for (Player p : getServer().getOnlinePlayers()) {
			if (p.isOp() || p.hasPermission("prison.manage")) {
				p.sendMessage("§aThe Prison auto-updater is disabled in this version. Be sure to check the SPIGOT update page for the latest updates! §bhttp://www.spigotmc.org/resources/prison.1223/");
			}
		}
		Bukkit.getScheduler().runTaskLater(Prison.i(), new Runnable() {

			@Override
			public void run() {
				try {
					im.populateLists();
				} catch (IOException e) {
					l.severe("Could not load item list. Will now only support Item IDs.");
					e.printStackTrace();
				}
			}
		}, 10L);
	}

	public void onDisable() {
		if (mines.isEnabled()) {
			for (Mine m : mines.mm.mines.values()) {
				m.save();
			}
		}
	}

	public void reload() {
		config.reload();
		playerList = new PlayerList();
		mines.disable();
		mines = new Mines();
		enableMines();
		ranks.disable();
		ranks = new Ranks();
		enableRanks();
	}

	// Initialization
	public void enableMines() {
		if (mines.isEnabled()) {
			try {
				mines.enable();
			} catch (FailedToStartException e) {
				l.severe("Could not start mines.");
				return;
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
				return;
			}
			l.info("&2Ranks enabled.");
		}
	}

	public void initEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			return;
		}
		economy = null;
	}

	public void initPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permissions = permissionProvider.getProvider();
			return;
		}
		permissions = null;
	}

	public void checkCompatibility() {
		if (!hasPlugin("Vault")) {
			ranks.setEnabled(false);
			l.warning("Could not enable Ranks because Vault is not loaded.");
		}
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
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static boolean hasPlugin(String name) {
		return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
	}

	// Listeners
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (updateAvailable) {
			Player p = e.getPlayer();
			if (p.isOp() || p.hasPermission("prison.manage")) {
				p.sendMessage(MessageUtil.get("general.updateAvailable", updateLatestName));
			}
		}
	}
}
