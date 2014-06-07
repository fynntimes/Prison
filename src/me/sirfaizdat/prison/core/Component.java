/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

/**
 * Base for a feature component.
 * @author SirFaizdat
 */
public interface Component {

	/**
	 * Get the name of the component.
	 * @return The name of the component.
	 */
	public String getName();
	
	/**
	 * Check if the component is enabled.
	 * @return true if the component is enabled, false otherwise.
	 */
	public boolean isEnabled();
	
	/**
	 * Enable or disable the component. Only works at startup.
	 * @param enabled true or false
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Called after the component is deemed compatible and enabled.
	 */
	public void enable() throws FailedToStartException;
	
}
