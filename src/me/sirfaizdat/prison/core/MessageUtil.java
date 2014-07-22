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

import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class MessageUtil {

	private static HashMap<String, String> messages = new HashMap<String, String>();

	// &c - Errors
	// &4 - Fatal errors
	// &a - Success messages
	// &6 - Extra tips or normal; uncategorized.
	
	public MessageUtil() {
		// Core
		add("general.cmdNotFound",
				"&cThat command could not be found. Type &6%0 &cfor help.");
		add("general.noPermission",
				"&cYou don't have permission to use this command.");
		add("general.mustBePlayer",
				"&cYou must be in game to use this command.");
		add("general.notEnoughArgs",
				"&cNot enough arguments. &6Proper usage: %0");
		add("general.noCmdPassed", "&cPlease specify a command.");
		add("general.updateAvailable",
				"&aThere is a new version of Prison available &7(%0)&a! &6Type &c/prison update &6to update.");
		add("general.versionString",
				"&6Prison v"
						+ Core.i().getDescription().getVersion()
						+ "\n&cAuthor: SirFaizdat\n&bWebsite: "
						+ Core.i().getDescription().getWebsite()
						+ "\n&7(C) 2014 SirFaizdat All Rights Reserved.");
		add("general.reloaded", "&aReloaded.");
		add("general.itemManagerNotLoaded", "&6Please wait for the item manager to load.");
		add("general.noUpdate", "&aYou are running the latest version!");
		add("general.updated", "&aDownloaded latest version &7(%0)&a! Please reload/restart your server.");
		add("general.updateFailed", "&cFailed to check for update.");
		add("general.devBuild", "&6You are running a development build.");
		add("general.notAllowedToCheck", "&cCould not check for update because update checking is disabled in your config.");
		add("general.autoSmeltToggled", "&6Autosmelt has been %0&6.");
		add("general.blocksCompacted", "&d%0 &aitems compacted into &d%1&a blocks!");
		// Mines
		add("mines.noMinesLoaded", "&cThere are no mines loaded.");
		add("mines.resetSuccess", "&aSuccessfully reset mine &6%0&a.");
		add("mines.created", "&aSuccessfully created a new mine called &6%0.");
		add("mines.makeWESel", "&cPlease make a WorldEdit selection first.");
		add("mines.alreadyExists", "&cA mine with that name already exists.");
		add("mines.failedToCreate",
				"&cCould not create a new mine. Check console for details.");
		add("mines.notFound", "&cThat mine does not exist.");
		add("mines.blockNotExist", "&cThat block does not exist.");
		add("mines.notABlock", "&cThat item is not a block.");
		add("mines.invalidId", "&cThat ID is invalid.");
		add("mines.alreadyInMine", "&cThat block is already in this mine.");
		add("mines.mineDoesntHaveThisBlock",
				"&cThe mine doesn't contain that block.");
		add("mines.removedBlock",
				"&aSuccessfully removed block &6%0&a from mine &6%1&a.");
		add("mines.mineFull",
				"&cThat percentage might be too high, or the mine may be already full.");
		add("mines.deletedMine", "&aMine successfully deleted.");
		add("mines.mustSpecifyID",
				"&cYou must use the block's item ID. (For example, Stone is 1 and Birch wood is 5:2)");
		add("mines.invalidPercent", "&cInvalid percentage.");
		add("mines.addSuccess", "&aMine &6%0&a is now &6%1&a of block &6%2&a.");

		// Ranks
		add("ranks.noRanksLoaded", "&6No ranks are loaded.");
		add("ranks.highestRank", "&cYou have already reached the highest rank!");
		add("ranks.highestRank.other",
				"&cThat player has already reached the highest rank!");
		add("ranks.notEnoughMoney",
				"&cYou still need &d$%0&c to rank up to &r%1&r&c.");
		add("ranks.rankedUp", "&aYou have ranked up to %0!");
		add("ranks.rankedUpBroadcast", "&d%0 &aranked up to %1&r&a!");
		add("ranks.notARank", "&cThat rank does not exist!");
		add("ranks.alreadyLoaded", "&cThat rank is already loaded!");
		add("ranks.invalidPrice", "&cThat price is invalid!");
		add("ranks.addSuccess", "&aSuccessfully added rank %0&a!");
		add("ranks.addFail", "&cFailed to add rank %0&c.");
		add("ranks.removeSuccess", "&aSuccessfully removed rank %0&a!");
		add("ranks.removeFail", "&cFailed to remove rank %0&c.");
		add("ranks.lowestRank", "&cAlready at the lowest rank!");
		add("ranks.demoteSuccess",
				"&aSuccessfully demoted &6%0&a to rank %1&a!");
		add("ranks.notAPlayer",
				"&cThat player does not exist or is offline.");

		// Shops
		add("shops.noPermission",
				"&cYou don't have permission to create a shop.");
		add("shops.serverNoPermission",
				"&cYou don't have permission to create a server shop.");
		add("shops.othersNoPermission",
				"&cYou don't have permission to create a shop for someone else.");
		add("shops.userNotFound", "&cThat player could not be found.");
		add("shops.invalidItemAndQuantity",
				"&cThe item and quantity are invalid.");
		add("shops.invalidQuantity", "&cThe quantity is invalid.");
		add("shops.invalidItem", "&cThe item is invalid.");
		add("shops.invalidPrice", "&cThat price is invalid.");
		add("shops.createSuccess", "&aSuccessfully created shop!");
	}

	public static void add(String key, String value) {
		messages.put(key, Core.i().config.serverPrefix + Core.colorize(value));
	}

	public static String get(String key) {
		return messages.get(key);
	}

	public static String get(String key, String... replace) {
		String returnVal = messages.get(key);
		for (int i = 0; i < replace.length; i++) {
			returnVal = returnVal
					.replaceAll("%" + i, Core.colorize(replace[i]));
		}
		return returnVal;
	}

}
