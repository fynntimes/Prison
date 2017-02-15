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
package me.sirfaizdat.prison.core;

import me.sirfaizdat.prison.core.cmds.CmdAutoSmelt;
import me.sirfaizdat.prison.core.cmds.CmdBlock;
import me.sirfaizdat.prison.core.cmds.PrisonCommandManager;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.mines.entities.Mine;
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

import java.io.File;
import java.io.IOException;

//import me.sirfaizdat.prison.mines.PlayerGUI;


/**
 * @author SirFaizdat
 */
public class Prison extends JavaPlugin implements Listener {

    public static PrisonLogger l = new PrisonLogger();
    // Instance of Core
    private static Prison i = null;
    public Mines mines;
    public Ranks ranks;
    public PlayerList playerList;
    public Config config;
    public ItemManager im;
    public Updater updater;
    private Economy economy;
    private Permission permissions;
    private boolean updateAvailable = false;
    private String updateLatestName;

    public static Prison i() {
        return i;
    }

    // Utility Methods
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    // BEGIN ENABLE STUFF

    public void onEnable() {
        long startTime = System.currentTimeMillis();
        i = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);

        bootstrap();
        initComponents();
        initCommands();

        initMetrics();

        l.info("&7Enabled &3Prison v" + getDescription().getVersion()
            + "&7. Made with <3 by &3The Prison Team&7.");
        long endTime = System.currentTimeMillis();
        l.info("&8Enabled in " + (endTime - startTime) + " milliseconds.");

        // Post-enable tasks
        verifyJavaVersion();
        updateCheck();
        populateItemManagerLater();
    }

    private void bootstrap() {
        config = new Config();
        im = new ItemManager();
        new MessageUtil();
        playerList = new PlayerList();
        getServer().getPluginManager().registerEvents(playerList, this);
        updater = new Updater();
    }

    private void initComponents() {
        mines = new Mines();
        ranks = new Ranks();
        mines.setEnabled(config.enableMines);
        ranks.setEnabled(config.enableRanks);

        initEconomy();
        initPermissions();
        checkCompatibility();
        enableMines();
        enableRanks();
    }

    private void initCommands() {
        if (config.enableAutosmelt) {
            new CmdAutoSmelt();
        }
        if (config.enableAutoblock) {
            new CmdBlock();
        }
        getCommand("prison").setExecutor(new PrisonCommandManager());
    }

    private void initMetrics() {
        if (config.optOut) {
            return;
        }
        Metrics metrics = new Metrics(this);
    }

    private void verifyJavaVersion() {
        String version = System.getProperty("java.version");

        if (version.charAt(2) <= '7') {
            l.warning(
                "Prison-3 will only be compatible with Java 8. Please update your Java version, or find a better shared hosting company. ");
        }
    }

    private void updateCheck() {
        if (config.checkUpdates && !getDescription().getVersion().contains("-SNAPSHOT")) {
            if (updater._SYNC_checkForUpdates(false).getUpdate().isNew(getDescription().getVersion())) {
                updateLatestName = updater.getUpdate().name;
                l.info(MessageUtil.get("general.updateAvailable", updateLatestName));
                this.updateAvailable = true;
                String msg = MessageUtil.get("general.updateAvailable", updateLatestName);
                if (updateLatestName.equalsIgnoreCase("Prison 3 v1.0.0")) {
                    msg =
                        "&3Prison &7> &fPrison 3 is out! To upgrade, simply type &b/prison update. All your files will be converted!";
                }
                for (Player p : getServer().getOnlinePlayers()) {
                    if (p.isOp() || p.hasPermission("prison.manage")) {
                        p.sendMessage(msg);
                    }
                }
            }
        }
    }

    private void populateItemManagerLater() {
        Bukkit.getScheduler().runTaskLater(Prison.i(), new Runnable() {

            @Override public void run() {
                try {
                    im.populateLists();
                } catch (IOException e) {
                    l.severe("Could not load item list. Will now only support Item IDs.");
                    e.printStackTrace();
                }
            }
        }, 10L);
    }

    private void enableMines() {
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

    private void enableRanks() {
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

    private void initEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
            .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return;
        }
        economy = null;
    }

    private void initPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager()
            .getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permissions = permissionProvider.getProvider();
            return;
        }
        permissions = null;
    }

    private void checkCompatibility() {
        if (!hasPlugin("Vault")) {
            ranks.setEnabled(false);
            l.warning("Could not enable Ranks because Vault is not loaded.");
        }
        if (!hasPlugin("WorldEdit")) {
            mines.setEnabled(false);
            l.warning("Could not enable Mines because WorldEdit is not loaded.");
        }
    }

    private boolean hasPlugin(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
    }

    // END ENABLE STUFF

    public void onDisable() {
        if (mines.isEnabled()) {
            for (Mine m : mines.mm.mines.values()) {
                m.save();
            }
        }
    }

    public void reload() {
        config.reload();
        MessageUtil.reload();
        playerList = new PlayerList();

        mines.disable();
        mines = new Mines();
        enableMines();

        ranks.disable();
        ranks = new Ranks();
        enableRanks();
    }

    public Permission getPermissions() {
        return permissions;
    }

    public Economy getEconomy() {
        return economy;
    }

    public File getFile() {
        return super.getFile();
    }

    // Listeners
    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        if (updateAvailable) {
            Player p = e.getPlayer();
            if (p.isOp() || p.hasPermission("prison.manage")) {
                p.sendMessage(MessageUtil.get("general.updateAvailable", updateLatestName));
            }
        }
    }
}
