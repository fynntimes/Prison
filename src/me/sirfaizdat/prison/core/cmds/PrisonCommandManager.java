/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.AbstractCommandManager;
import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;

/**
 * @author SirFaizdat
 */
public class PrisonCommandManager extends AbstractCommandManager {

	public PrisonCommandManager() {
		super(new Component() {
			boolean enabled = true;
			@Override
			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
			
			@Override
			public boolean isEnabled() {
				return enabled;
			}
			
			@Override
			public String getName() {
				return "Prison";
			}
			
			@Override
			public String getBaseCommand() {
				return "prison";
			}
			
			@Override
			public void enable() throws FailedToStartException {
			}

			@Override
			public void reload() {
			}
		}, "prison");
	}

	@Override
	public void registerCommands() {
		commands.put("version", new CmdVersion());
	}

}
