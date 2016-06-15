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

    // Fake a component for Prison so that the AbstractCommandManager is happy
    public PrisonCommandManager() {
        super(new Component() {
            boolean enabled = true;

            @Override
            public boolean isEnabled() {
                return enabled;
            }

            @Override
            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
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

            public void disable() {
            }
        }, "prison");
    }

    @Override
    public void registerCommands() {
        commands.put("reload", new CmdReload());
        commands.put("update", new CmdUpdate());
        commands.put("version", new CmdVersion());
    }

}
