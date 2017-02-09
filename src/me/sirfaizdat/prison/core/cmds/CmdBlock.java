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

package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

/**
 * @author SirFaizdat
 */
public class CmdBlock implements CommandExecutor {

    public CmdBlock() {
        Prison.i().getCommand("block").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label,
        String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.get("general.mustBePlayer"));
            return true;
        }
        if (!sender.hasPermission("prison.block")) {
            sender.sendMessage(MessageUtil.get("general.noPermission"));
            return true;
        }
        Player player = (Player) sender;
        try {
            int amountOfDiamonds = 0;
            int amountOfEmeralds = 0;
            int amountOfIron = 0;
            int amountOfGold = 0;
            int amountOfGlowstone = 0;
            int coal = 0;
            int redstone = 0;
            int lapis = 0;

            int itemsChanged;

            for (ItemStack is : player.getInventory().getContents()) {
                if (is != null) {
                    if (is.getType() == Material.DIAMOND) {
                        player.getInventory().remove(is);
                        amountOfDiamonds += is.getAmount();
                    }
                    if (is.getType() == Material.EMERALD) {
                        amountOfEmeralds += is.getAmount();
                        player.getInventory().remove(is);
                    }
                    if (is.getType() == Material.IRON_INGOT) {
                        player.getInventory().remove(is);
                        amountOfIron += is.getAmount();
                    }
                    if (is.getType() == Material.GLOWSTONE_DUST) {
                        amountOfGlowstone += is.getAmount();
                        player.getInventory().remove(is);
                    }
                    if (is.getType() == Material.GOLD_INGOT) {
                        player.getInventory().remove(is);
                        amountOfGold += is.getAmount();
                    }
                    if (is.getType() == Material.COAL) {
                        player.getInventory().remove(is);
                        coal += is.getAmount();
                    }
                    if (is.getType() == Material.REDSTONE) {
                        redstone += is.getAmount();
                        player.getInventory().remove(is);
                    }
                    if ((is.getType() == Material.INK_SACK) && (((Dye) is.getData()).getColor()
                        == DyeColor.BLUE)) {
                        player.getInventory().remove(is);
                        lapis += is.getAmount();
                    }
                }
            }
            player.updateInventory();

            itemsChanged = amountOfDiamonds + amountOfEmeralds + amountOfGlowstone + amountOfGold
                + amountOfIron + coal + redstone + lapis;

            int diamondsToTransform = amountOfDiamonds / 9;
            int diamondOverflow = amountOfDiamonds % 9;
            int emeraldsToTransform = amountOfEmeralds / 9;
            int emeraldsOverflow = amountOfEmeralds % 9;
            int ironToTransform = amountOfIron / 9;
            int ironOverflow = amountOfIron % 9;
            int goldToTransform = amountOfGold / 9;
            int goldOverflow = amountOfGold % 9;
            int glowstoneToTransform = amountOfGlowstone / 4;
            int glowstoneOverflow = amountOfGlowstone % 4;

            int rT = redstone / 9;
            int rO = redstone % 9;
            int lT = lapis / 9;
            int lO = lapis % 9;
            int cT = coal / 9;
            int cO = coal % 9;

            itemsChanged =
                itemsChanged - (diamondOverflow + emeraldsOverflow + ironOverflow + goldOverflow
                    + glowstoneOverflow + rO + cO + lO);

            player.getInventory().addItem(
                new ItemStack(diamondsToTransform > 0 ? Material.DIAMOND_BLOCK : Material.AIR,
                    diamondsToTransform),
                new ItemStack(emeraldsToTransform > 0 ? Material.EMERALD_BLOCK : Material.AIR,
                    emeraldsToTransform),
                new ItemStack(diamondOverflow > 0 ? Material.DIAMOND : Material.AIR,
                    diamondOverflow),
                new ItemStack(emeraldsOverflow > 0 ? Material.EMERALD : Material.AIR,
                    emeraldsOverflow),
                new ItemStack(ironToTransform > 0 ? Material.IRON_BLOCK : Material.AIR,
                    ironToTransform),
                new ItemStack(goldToTransform > 0 ? Material.GOLD_BLOCK : Material.AIR,
                    goldToTransform),
                new ItemStack(glowstoneToTransform > 0 ? Material.GLOWSTONE : Material.AIR,
                    glowstoneToTransform),
                new ItemStack(ironOverflow > 0 ? Material.IRON_INGOT : Material.AIR, ironOverflow),
                new ItemStack(goldOverflow > 0 ? Material.GOLD_INGOT : Material.AIR, goldOverflow),
                new ItemStack(glowstoneOverflow > 0 ? Material.GLOWSTONE_DUST : Material.AIR,
                    glowstoneOverflow));

            player.getInventory()
                .addItem(new ItemStack(rT > 0 ? Material.REDSTONE_BLOCK : Material.AIR, rT));
            player.getInventory()
                .addItem(new ItemStack(lT > 0 ? Material.LAPIS_BLOCK : Material.AIR, lT));
            player.getInventory()
                .addItem(new ItemStack(cT > 0 ? Material.COAL_BLOCK : Material.AIR, cT));
            player.getInventory()
                .addItem(new ItemStack(rO > 0 ? Material.REDSTONE : Material.AIR, rO));
            player.getInventory().addItem(
                new ItemStack(lO > 0 ? Material.INK_SACK : Material.AIR, lO,
                    DyeColor.BLUE.getDyeData()));

            player.getInventory().addItem(new ItemStack(cO > 0 ? Material.COAL : Material.AIR, cO));

            player.sendMessage(MessageUtil.get("general.blocksCompacted", itemsChanged + "",
                "" + (diamondsToTransform + emeraldsToTransform + glowstoneToTransform
                    + goldToTransform + ironToTransform + lT + rT + cT)));
            player.updateInventory();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }
}
