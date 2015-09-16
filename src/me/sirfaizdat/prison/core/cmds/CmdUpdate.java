/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.core.Updater;
import me.sirfaizdat.prison.core.Updater.UpdateResult;
import me.sirfaizdat.prison.core.Updater.UpdateType;

/**
 * @author SirFaizdat
 */
public class CmdUpdate extends Command {

	public CmdUpdate() {
		super("update");
	}

	@Override
	protected void execute() {
		if (Prison.i().config.checkUpdates) {
			if(Prison.i().getDescription().getVersion().contains("dev")) {
				sender.sendMessage(MessageUtil.get("general.devBuild"));
				return;
			}
			Updater updater = new Updater(Prison.i(), 76155, Prison.i().file,
					UpdateType.DEFAULT, true);
			if (updater.getResult() == UpdateResult.NO_UPDATE) {
				sender.sendMessage(MessageUtil.get("general.noUpdate"));
			} else if (updater.getResult() == UpdateResult.SUCCESS) {
				sender.sendMessage(MessageUtil.get("general.updated",
						updater.getLatestName()));
			} else {
				sender.sendMessage(MessageUtil.get("general.updateFailed"));
			}
		} else {
			sender.sendMessage(MessageUtil.get("general.notAllowedToCheck"));
		}
	}

	@Override
	public String description() {
		return "Downloads the latest version of Prison.";
	}

}
