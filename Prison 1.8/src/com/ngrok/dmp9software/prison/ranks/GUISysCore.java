package com.ngrok.dmp9software.prison.ranks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.austinv11.GUIAPI.GUIAPI;
import io.github.austinv11.GUIAPI.Menu;
import io.github.austinv11.GUIAPI.MenuInteractEvent;
import io.github.austinv11.GUIAPI.Button;

public class GUISysCore {
	public Inventory inv;

	DMP9SettingsMgr settings = new DMP9SettingsMgr();
	@SuppressWarnings("deprecation")
	public void openGUI(Player player, int mode){
		int i = 9;
		int itemsAdded = 0;
		int itemsTotal = settings.getConfig().getInt("warps.total");
		String warpName = "";
		String thingsDone = ",";
		String things = settings.getConfig().getString("dmp9core.warpdata.dmp9coredatacode");
		String things2 = "";
		if (mode == 1){i = 9;}
		if (mode == 2){i = 18;}
		if (mode == 3){i = 27;}
		//Creating a new Menu object:
		Menu warp = new Menu(player, settings.getConfig().getString("prison-warp-gui-title"), i);
		while (itemsAdded != itemsTotal)
		{
		
		List<String> description = new ArrayList<String>();
		description.add(ChatColor.GOLD+"Teleport to prison arena "+ChatColor.AQUA+settings.getConfig().getString("warps." + warpName + ".name"));
		//Adding a button to the menu:
		warp.addButton(Material.getMaterial(settings.getConfig().getInt("warps." + warpName + ".item")), settings.getConfig().getString("warps." + warpName + ".name"), description);
		//Opening the menu for the player:
		}
		warp.openMenu();
	}
}
