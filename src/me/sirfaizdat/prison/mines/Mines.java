/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
	
	public void disable() {
		mm.mines.clear();
		Bukkit.getScheduler().cancelTask(mm.autoResetID);
	}

	public WorldEditPlugin getWE() {
		return worldEdit;
	}

	@Override
	public String getBaseCommand() {
		return "mines";
	}
	
}
