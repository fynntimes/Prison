/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

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

import java.io.File;
import java.io.IOException;

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

    private Economy economy;
    private Permission permissions;

    public Updater updater;
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

        l.info("&7Enabled &3Prison v" + getDescription().getVersion() + "&7. Made with <3 by &3SirFaizdat&7.");
        long endTime = System.currentTimeMillis();
        l.info("&8Enabled in " + (endTime - startTime) + " milliseconds.");

        // Post-enable tasks
        updateCheck();
        populateItemManagerLater();
    }

    private void bootstrap() {
        config = new Config();
        im = new ItemManager();
        new MessageUtil();
        playerList = new PlayerList();
        getServer().getPluginManager().registerEvents(playerList, this);
        updater = new Updater(this, 76155, this.getFile(), UpdateType.NO_DOWNLOAD, true);
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
        if(config.enableAutosmelt) new AutoSmelt();
        if(config.enableAutoblock) new BlockCommand();
        getCommand("prison").setExecutor(new PrisonCommandManager());
    }

    private void updateCheck() {
        if (config.checkUpdates && !getDescription().getVersion().contains("dev") && !getDescription().getVersion().contains("-SNAPSHOT")) {
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
    }

    private void populateItemManagerLater() {
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
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return;
        }
        economy = null;
    }

    private void initPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
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
