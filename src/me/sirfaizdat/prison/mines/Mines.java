/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.FailedToStartException;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * Manages the Mines component.
 * 
 * @author SirFaizdat
 */
public class Mines implements Component {

	public static Mines i;
	private boolean enabled = true;
	Core core = Core.i();

	MinesCommandManager mcm;
	public MinesManager mm;
	WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	
	public String getName() {
		return "Mines";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() throws FailedToStartException {
		i = this;
		mm = new MinesManager();
		mcm = new MinesCommandManager(this);
		core.getCommand("mines").setExecutor(mcm);
	}
	
	public void reload() {
		mm.mines.clear();
		mm.load();
		Bukkit.getScheduler().cancelTask(mm.autoResetID);
		mm.timer();
	}

	public WorldEditPlugin getWE() {
		return worldEdit;
	}

	@Override
	public String getBaseCommand() {
		return "mines";
	}
	
}
