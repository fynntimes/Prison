package com.ngrok.dmp9software.prison.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import me.sirfaizdat.prison.core.AutoSmelt;
import me.sirfaizdat.prison.core.MessageUtil;

public class WarpSysCore extends Command {
	protected WarpSysCore(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	Player p;
	DMP9SettingsMgr settings = new DMP9SettingsMgr();
	public void setWarp(String[] args)
	{
		if (args[1] == "total") {return;}
        settings.getData().set("warps." + args[1] + ".world", p.getLocation().getWorld().getName());
        settings.getData().set("warps." + args[1] + ".x", p.getLocation().getX());
        settings.getData().set("warps." + args[1] + ".y", p.getLocation().getY());
        settings.getData().set("warps." + args[1] + ".z", p.getLocation().getZ());
        settings.getData().set("warps." + args[1] + ".item", args[2]);
        int oldTotal = settings.getData().getInt("warps.total");
        int newTotal = oldTotal + 1;
        settings.getData().set("warps.total", newTotal);
        settings.saveData();
        p.sendMessage(ChatColor.GREEN + "Set warp " + args[1] + "!");
	}
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("pwarps")) {
			if (!pm.isPluginEnabled("GUIAPI"))
			{
				p.sendMessage(ChatColor.RED + "Error loading warp GUI!");
				p.sendMessage("[DMP9 Software] DMP9 Core: The plugin GUIAPI was not found!");
			}
			else if (args.length != 3)
			{
				p.sendMessage(ChatColor.RED + "Error loading warp GUI!");
				p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "DMP9 Software: ERR_UNIMPLEMENTED_ARG");
			}
			else if (args[0] == "setwarp")
			{
				if (args.length != 4)
				{
					MessageUtil.get("warps.tolessargs");
				}
				else
				{
					setWarp(args);
				}
			}
			else if (args[0] == "warp")
			{
				if (pm.isPluginEnabled("GUIAPI")){p.sendMessage(ChatColor.DARK_GRAY + "Did you know theres a warping GUI? Use it by typing /pwarps!");}
				warp(args);
			}
			return true;
		} 
		return false; 
	}
	public boolean warp(String[] args)
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "/minecraft:give " + p.getName() + " " + settings.getData().getString("warps." + args[1] + ".item"));

        if (args.length == 1) {
                p.sendMessage(ChatColor.RED + "Please specify a name!");
                return true;
        }
        if (settings.getData().getConfigurationSection("warps." + args[1]) == null) {
                p.sendMessage(ChatColor.RED + "Warp " + args[1] + " does not exist!");
                return true;
        }
        World w = Bukkit.getServer().getWorld(settings.getData().getString("warps." + args[1] + ".world"));
        double x = settings.getData().getDouble("warps." + args[1] + ".x");
        double y = settings.getData().getDouble("warps." + args[1] + ".y");
        double z = settings.getData().getDouble("warps." + args[1] + ".z");
        p.teleport(new Location(w, x, y, z));
        p.sendMessage(ChatColor.GREEN + "Teleported to " + args[1] + "!");
        String name = p.getName();
        if (settings.getData().getBoolean("autosmelt-on-warp") && !AutoSmelt.isEnabled(name)) {Bukkit.getServer().dispatchCommand(p, "/as");}
        return true;
	}
	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		return onCommand(arg0, this, arg1, arg2);
	}
}
