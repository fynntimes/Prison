/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.scoreboard;

import org.bukkit.ChatColor;

/**
 * @author SirFaizdat
 */
public class ScoreboardEntry {

	private String display;
	private String value;
	
	public ScoreboardEntry(String display, String value) {
		this.display = display;
		this.value = value;
	}
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		if(display.length() > 16) {
			display = ChatColor.stripColor(display);
			if(display.length() > 16) {
				display.substring(0, Math.min(display.length(), 16));
			}
		}
		this.display = display;
	}
	public String getValue() {
		if(value.length() > 16) {
			value = ChatColor.stripColor(value);
			if(value.length() > 16) {
				value.substring(0, Math.min(value.length(), 16));
			}
		}
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
