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

package tech.mcprison.prison.core;

/**
 * Base for a feature component.
 *
 * @author SirFaizdat
 */
public interface Component {

    /**
     * Get the name of the component.
     *
     * @return The name of the component.
     */
    String getName();

    /**
     * Get the base command for this component.
     *
     * @return The base command.
     */
    String getBaseCommand();

    /**
     * Check if the component is enabled.
     *
     * @return true if the component is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Enable or disable the component. Only works at startup.
     *
     * @param enabled true or false
     */
    void setEnabled(boolean enabled);

    /**
     * Called on a reload.
     */
    void reload();

    /**
     * Called after the component is deemed compatible and enabled.
     */
    void enable() throws FailedToStartException;

    /**
     * Called when the plugin disables.
     */
    void disable();

}
