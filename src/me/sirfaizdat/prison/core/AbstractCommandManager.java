/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author SirFaizdat
 */
public abstract class AbstractCommandManager implements CommandExecutor {

	public LinkedHashMap<String, Command> commands = new LinkedHashMap<String, Command>();
	protected Component c;
	String baseCommand;
	String helpMessage;

	public AbstractCommandManager(Component c, String baseCommand) {
		this.c = c;
		this.baseCommand = baseCommand;
		registerCommands();
		componentize();
		helpMessage = generateHelpMessage();
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		try {
			if (command.getName().equalsIgnoreCase(baseCommand)) {
				if (args.length < 1) {
					sender.sendMessage(MessageUtil.get("general.noCmdPassed"));
					sender.sendMessage(helpMessage);
					return true;
				}
				if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(helpMessage);
					return true;
				}
				Command c = commands.get(args[0].toLowerCase());
				if (c == null) {
					sender.sendMessage(MessageUtil.get("general.cmdNotFound", "/" + baseCommand + " help"));
					return true;
				}
				c.run(sender, args);
			}
		} catch (Exception e) {
			// Only place where a command returns false - might help discover the error.
			Prison.l.severe("There was an error handling command " + baseCommand + ". Reason: " + e.getMessage());
			Prison.l.warning("More info: ");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public abstract void registerCommands();

	public String generateHelpMessage() {
		StringBuilder b = new StringBuilder();
		b.append("&6==============&c[&2" + c.getName() + "&c]&6==============\n");
		b.append("&7<> = Required argument    [] = Optional argument\n");
		for (Map.Entry<String, Command> cmd : commands.entrySet()) {
			String cmdString = cmd.getValue().usage() + " &2-&c " + cmd.getValue().description();
			b.append(cmdString + "\n");
		}
		return Prison.colorize(b.toString());
	}

	private void componentize() {
		for (Map.Entry<String, Command> c : commands.entrySet()) {
			c.getValue().setComponent(this.c);
		}
	}

}
