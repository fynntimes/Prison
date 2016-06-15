/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

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
