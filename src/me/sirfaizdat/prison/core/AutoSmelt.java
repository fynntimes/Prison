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
                        "&3enabled"));
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
                Location centerOfBlock = loc.add(0.5, 0.5, 0.5);
                smelted.setType(Material.AIR);
                if(Prison.i().config.flamesOnAutosmelt) loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0, 50);
                ItemStack ironing = new ItemStack(Material.IRON_INGOT);
                smelted.getWorld().dropItemNaturally(centerOfBlock, ironing);
            } else if (m == Material.GOLD_ORE) {
                Block smelted = e.getBlock();
                Location loc = smelted.getLocation();
                Location centerOfBlock = loc.add(0.5, 0.5, 0.5);
                smelted.setType(Material.AIR);
                if(Prison.i().config.flamesOnAutosmelt) loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0, 50);
                ItemStack golding = new ItemStack(Material.GOLD_INGOT);
                smelted.getWorld().dropItemNaturally(centerOfBlock, golding);
            }
        }
    }

}
