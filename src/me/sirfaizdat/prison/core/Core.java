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

import java.io.File;

import me.sirfaizdat.prison.core.Updater.UpdateResult;
import me.sirfaizdat.prison.core.Updater.UpdateType;
import me.sirfaizdat.prison.core.cmds.PrisonCommandManager;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Ranks;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author SirFaizdat
 */
// Considered a component, but not implementing due to class hierarchy.
public class Core extends JavaPlugin implements Listener {

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

	public PlayerList playerList;
	public Config config;
	public ItemManager im;
	boolean updateAvailable = false;
	String updateLatestName;
	
	public File file;

	public void onEnable() {
		long startTime = System.currentTimeMillis();
		i = this;
		this.saveDefaultConfig();
		config = new Config();
		im = new ItemManager();
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
//		enableScoreboards();
		file = getFile();
		getCommand("prison").setExecutor(new PrisonCommandManager());
		new AutoSmelt();
		new BlockCommand();
		getServer().getPluginManager().registerEvents(this, this);
		l.info("&2Enabled Prison &6v" + getDescription().getVersion()
				+ "&2. Made by &6SirFaizdat&2.");
		long endTime = System.currentTimeMillis();
		l.info("&6Enabled in " + (endTime - startTime) + "ms.");
		if (config.checkUpdates
				&& !getDescription().getVersion().contains("dev")) {
			Updater updater = new Updater(this, 76155, this.getFile(),
					UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				updateLatestName = updater.getLatestName();
				l.info(MessageUtil.get("general.updateAvailable",
						updateLatestName));
				this.updateAvailable = true;
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.isOp() || p.hasPermission("prison.manage")) {
						p.sendMessage(MessageUtil.get(
								"general.updateAvailable", updateLatestName));
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(Core.i(), new Runnable() {

			@Override
			public void run() {
				im.populateLists();
			}
		}, 10L);
	}

	public void reload() {
		config.reload();
		playerList = new PlayerList();
		mines = new Mines();
		enableMines();
		mines.reload();
		ranks = new Ranks();
		enableRanks();
		ranks.reload();
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

//	public void enableScoreboards() {
//		if (!ranks.isEnabled()) {
//			sbs.setEnabled(false);
//			l.warning("Could not enable scoreboards because Ranks is not enabled.");
//		}
//		if (sbs.isEnabled()) {
//			try {
//				sbs.enable();
//			} catch (FailedToStartException e) {
//				l.severe("Could not start scoreboards");
//				return;
//			}
//		}
//		l.info("&2Scoreboards enabled.");
//	}

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
		if(!hasPlugin("Vault")) {
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
		return text.replaceAll("&", "§");
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
				p.sendMessage(MessageUtil.get("general.updateAvailable",
						updateLatestName));
			}
		}
	}
}
