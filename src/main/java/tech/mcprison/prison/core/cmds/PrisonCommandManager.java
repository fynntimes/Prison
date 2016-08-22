/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tech.mcprison.prison.core.cmds;

import tech.mcprison.prison.core.AbstractCommandManager;
import tech.mcprison.prison.core.Component;
import tech.mcprison.prison.core.FailedToStartException;

/**
 * @author SirFaizdat
 */
public class PrisonCommandManager extends AbstractCommandManager {

    // Fake a component for Prison so that the AbstractCommandManager is happy
    public PrisonCommandManager() {
        super(new Component() {
            boolean enabled = true;

            @Override public boolean isEnabled() {
                return enabled;
            }

            @Override public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            @Override public String getName() {
                return "Prison";
            }

            @Override public String getBaseCommand() {
                return "prison";
            }

            @Override public void enable() throws FailedToStartException {
            }

            @Override public void reload() {
            }

            public void disable() {
            }
        }, "prison");
    }

    @Override public void registerCommands() {
        commands.put("reload", new CmdReload());
        commands.put("update", new CmdUpdate());
        commands.put("version", new CmdVersion());
    }

}
