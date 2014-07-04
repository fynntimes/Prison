/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.scoreboards;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;

/**
 * @author SirFaizdat
 */
public class Scoreboards implements Component {

	boolean enabled = true; 
	
	public String getName() {
		return "Scoreboards";
	}

	public String getBaseCommand() {
		return "scoreboards";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void reload() {
	}

	public void enable() throws FailedToStartException {
		
	}

	
	
}
