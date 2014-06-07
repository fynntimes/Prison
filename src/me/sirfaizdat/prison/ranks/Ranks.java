/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;

/**
 * Manages the ranks component.
 * 
 * @author SirFaizdat
 */
public class Ranks implements Component {

	private boolean enabled = true;

	public String getName() {
		return "Ranks";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() throws FailedToStartException {

	}

}
