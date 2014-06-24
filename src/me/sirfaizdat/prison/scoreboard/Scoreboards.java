/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.scoreboard;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.FailedToStartException;

import org.bukkit.Bukkit;

/**
 * @author SirFaizdat
 */
public class Scoreboards implements Component{

	boolean enabled = true;
	
	@Override
	public String getName() {
		return "Scoreboards";
	}

	@Override
	public String getBaseCommand() {
		return "scoreboards";
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	SbManager sb;
	
	@Override
	public void enable() throws FailedToStartException {
		// Run it after the server finishes loading (Economy should be loaded by then)
		Bukkit.getScheduler().runTaskLater(Core.i(), new Runnable() {
			@Override
			public void run() {
				sb = new SbManager();
			}
		}, 0);
	}

	

}
