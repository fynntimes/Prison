/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author SirFaizdat
 */
public class AutoSmelt implements CommandExecutor, Listener {

    static ArrayList<String> enabledPlayers = new ArrayList<String>();

    public AutoSmelt() {
        Prison.i().getCommand("autosmelt").setExecutor(this);
        Prison.i().getServer().getPluginManager().registerEvents(this, Prison.i());
    }

    public static boolean isEnabled(String s) {
        for (String player : enabledPlayers) {
            if (player.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (command.getName().equalsIgnoreCase("autosmelt")) {
            if (!sender.hasPermission("prison.autosmelt")) {
                sender.sendMessage(MessageUtil.get("general.noPermission"));
                return true;
            }
            if (isEnabled(sender.getName())) {
                for (int i = 0; i < enabledPlayers.size(); i++) {
                    if (enabledPlayers.get(i)
                            .equalsIgnoreCase(sender.getName())) {
                        enabledPlayers.remove(i);
                        sender.sendMessage(MessageUtil.get(
                                "general.autoSmeltToggled", "&cdisabled"));
                        break;
                    }
                }
            } else {
                enabledPlayers.add(sender.getName());
                sender.sendMessage(MessageUtil.get("general.autoSmeltToggled",
                        "&2enabled"));
            }
        }
        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Material m = e.getBlock().getType();
        if (isEnabled(e.getPlayer().getName()) || e.getPlayer().hasPermission("prison.autosmelt.auto")) {
            if (m == Material.IRON_ORE) {
                Block smelted = e.getBlock();
                Location loc = smelted.getLocation();
                smelted.setType(Material.AIR);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0, 50);
                ItemStack ironing = new ItemStack(Material.IRON_INGOT);
                smelted.getWorld().dropItemNaturally(loc, ironing);
            } else if (m == Material.GOLD_ORE) {
                Block smelted = e.getBlock();
                Location loc = smelted.getLocation();
                smelted.setType(Material.AIR);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0, 50);
                ItemStack golding = new ItemStack(Material.GOLD_INGOT);
                smelted.getWorld().dropItemNaturally(loc, golding);
            }
        }
    }

}
