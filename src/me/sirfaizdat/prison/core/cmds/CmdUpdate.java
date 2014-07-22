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
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.MessageUtil;
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
		if (Core.i().config.checkUpdates) {
			if(Core.i().getDescription().getVersion().contains("dev")) {
				sender.sendMessage(MessageUtil.get("general.devBuild"));
				return;
			}
			Updater updater = new Updater(Core.i(), 76155, Core.i().file,
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
