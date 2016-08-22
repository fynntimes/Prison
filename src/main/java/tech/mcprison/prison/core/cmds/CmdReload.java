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

package tech.mcprison.prison.core.cmds;

import tech.mcprison.prison.core.Command;
import tech.mcprison.prison.core.MessageUtil;
import tech.mcprison.prison.core.Prison;

/**
 * @author SirFaizdat
 */
public class CmdReload extends Command {

    public CmdReload() {
        super("reload");
    }

    @Override protected void execute() {
        Prison.i().reload();
        sender.sendMessage(MessageUtil.get("general.reloadSuccessful"));
    }

    @Override public String description() {
        return "Reload Prison's configuration and all components.";
    }

}
