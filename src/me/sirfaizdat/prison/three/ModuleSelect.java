/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2017  SirFaizdat
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

package me.sirfaizdat.prison.three;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class ModuleSelect implements Listener {
    private boolean open = false;
    private Player openPlayer;
    private Consumer<? super HashMap<String, Boolean>> callback;
    private static ModuleSelect s_instance;
    private Inventory inventory = null;
    private String[] modules;
    private static List<ItemStack> moduleStacks;
    private ModuleSelect(){}
    public static ModuleSelect initializeForPlayer(Player player, Consumer<? super HashMap<String, Boolean>> callback, String[] modules){
        if (s_instance != null){
            if (s_instance.open){
                return null;
            }
            s_instance.openPlayer = player;
            s_instance.callback = callback;
            s_instance.inventory = null;
            s_instance.modules = modules;
            return s_instance;
        }
        ModuleSelect instance = new ModuleSelect();
        instance.openPlayer = player;
        instance.callback = callback;
        instance.modules = modules;
        s_instance = instance;
        return instance;
    }
    public static void build(){
        if (s_instance == null){
            return;
        }
        moduleStacks = new ArrayList<>();
        Inventory inventory = Bukkit.createInventory(s_instance.openPlayer,18,ChatColor.DARK_BLUE+"Prison 3 Upgrade"+ChatColor.RESET+": &4Select modules");
        for (String module : s_instance.modules){
            if (module.equalsIgnoreCase("Prison.jar") || module.equalsIgnoreCase("Prison-Spigot.jar") || module.equalsIgnoreCase("PrisonSpigot.jar")){
                continue;
            }
            Wool w = new Wool();
            w.setColor(DyeColor.LIME);
            ItemStack stack = w.toItemStack();
            stack.getItemMeta().setDisplayName(ChatColor.GOLD +module);
            moduleStacks.add(stack);
        }
    }
    public void open(){
        if (inventory == null){
            build();
        }
        s_instance.openPlayer.openInventory(s_instance.inventory);
    }
}
