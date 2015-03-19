package com.ngrok.dmp9software.prison.ranks;

import me.sirfaizdat.prison.core.PrisonLogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class DMP9Core {
	PluginManager pm = Bukkit.getServer().getPluginManager();
	PrisonLogger l = new PrisonLogger();

	public void coreEnable(){
		if (pm.getPlugin("GUIAPI").isEnabled()) {
			l.info("[DMP9 Software] DMP9 Core: The plugin GUIAPI was found!");
			l.info("[DMP9 Software] DMP9 Core: The GUI, /pwarps, has been enabled");
		}
		else
		{
			l.warning("[DMP9 Software] DMP9 Core: The plugin GUIAPI was not found!");
			l.warning("[DMP9 Software] DMP9 Core: The GUI, /pwarps, has been disabled");
		}
	}
}
