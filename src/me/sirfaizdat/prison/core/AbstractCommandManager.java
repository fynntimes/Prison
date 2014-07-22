/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
	public boolean onCommand(CommandSender sender,
			org.bukkit.command.Command command, String label, String[] args) {
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
				sender.sendMessage(MessageUtil.get("general.cmdNotFound", "/"
						+ baseCommand + " help"));
				return true;
			}
			c.run(sender, args);
		}
		return true;
	}

	public abstract void registerCommands();

	public String generateHelpMessage() {
		StringBuilder b = new StringBuilder();
		b.append("&6==============&c[&2" + c.getName()
				+ "&c]&6==============\n");
		b.append("&7<> = Required argument    [] = Optional argument\n");
		for (Map.Entry<String, Command> cmd : commands.entrySet()) {
			String cmdString = cmd.getValue().usage() + " &2-&c "
					+ cmd.getValue().description();
			b.append(cmdString + "\n");
		}
		return Core.colorize(b.toString());
	}

	private void componentize() {
		for (Map.Entry<String, Command> c : commands.entrySet()) {
			c.getValue().setComponent(this.c);
		}
	}

}
