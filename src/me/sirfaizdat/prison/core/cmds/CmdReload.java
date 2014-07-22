/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.MessageUtil;

/**
 * @author SirFaizdat
 */
public class CmdReload extends Command {

	public CmdReload() {
		super("reload");
	}

	@Override
	protected void execute() {
		Core.i().reload();
		sender.sendMessage(MessageUtil.get("general.reloaded"));
	}

	@Override
	public String description() {
		return "Reloads the config.yml.";
	}
	
	
}
