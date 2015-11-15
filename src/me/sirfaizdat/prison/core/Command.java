/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Abstract command class
 * 
 * @author SirFaizdat
 */
public abstract class Command {

	String name;
	ArrayList<String> requiredArgs, optionalArgs;
	String permission;
	boolean mustBePlayer = false;
	
	Component c;

	public Command(String name) {
		this.name = name;
		requiredArgs = new ArrayList<String>();
		optionalArgs = new ArrayList<String>();
		// Default permission
		permission = "prison." + name;
	}

	public void addRequiredArg(String name) {
		requiredArgs.add(name);
	}

	public void addOptionalArg(String name) {
		optionalArgs.add(name);
	}

	public void setComponent(Component c) {
		if (c == null)
			return;
		this.c = c;
		permission = "prison." + c.getName().toLowerCase() + "." + name;
	}

	public void mustBePlayer(boolean mustBePlayer) {
		this.mustBePlayer = mustBePlayer;
	}

	protected CommandSender sender;
	protected String[] args;
	protected int amountOfOptionalArgs;

	public void run(CommandSender sender, String[] args) {
		if (mustBePlayer && !(sender instanceof Player)) {
			sender.sendMessage(MessageUtil.get("general.mustBePlayer"));
			return;
		}
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(MessageUtil.get("general.noPermission"));
			return;
		}
		int length = args.length - 1;
		if (length < requiredArgs.size()) {
			sender.sendMessage(MessageUtil
					.get("general.notEnoughArgs", usage()));
			return;
		}
		amountOfOptionalArgs = length - requiredArgs.size();
		this.sender = sender;
		this.args = args;

		execute();
	}

	protected abstract void execute();

	public String usage() {
		StringBuilder usage = new StringBuilder();
		usage.append("&6/" + c.getBaseCommand() + " " + name + " &6");
		for (String s : requiredArgs) {
			usage.append("<" + s + "> ");
		}
		for (String s : optionalArgs) {
			usage.append("[" + s + "] ");
		}
		return usage.toString();
	}

	public abstract String description();

}
