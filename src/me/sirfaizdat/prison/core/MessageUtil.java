/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class MessageUtil {

	private static HashMap<String, String> messages = new HashMap<String, String>();

	public MessageUtil() {
		// Core
		add("general.cmdNotFound",
				"&cThat command could not be found. Type &6%0 &cfor help.");
		add("general.noPermission",
				"&cYou don't have permission to run this command.");
		add("general.mustBePlayer",
				"&cYou must be in game to use this command.");
		add("general.notEnoughArgs",
				"&cNot enough arguments. &6Proper usage: %0");
		add("general.noCmdPassed", "&cPlease specify a command.");
		add("general.updateAvailable",
				"&2There is a new version of Prison available (%0)! Go download it from the Bukkit page.");
		add("general.versionString",
				"&6Prison v"
						+ Core.i().getDescription().getVersion()
						+ "\n&cAuthor: SirFaizdat\n&bWebsite: http://mcprison.netne.net/\n&7(C) 2014 SirFaizdat All Rights Reserved.");
		add("general.reloaded", "&2Reloaded.");
		add("general.itemManagerNotLoaded", "&6Please wait for the item manager to load.");
		
		// Mines
		add("mines.noMinesLoaded", "&cThere are no mines loaded.");
		add("mines.resetSuccess", "&2Successfully reset mine &6%0&2.");
		add("mines.created", "&2Successfully created a new mine called &6%0.");
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
				"&2Successfully removed block &6%0&2 from mine &6%1&2.");
		add("mines.mineFull",
				"&cThat percentage might be too high, or the mine may be already full.");
		add("mines.deletedMine", "&2Mine successfully deleted.");
		add("mines.mustSpecifyID",
				"&cYou must use the block's item ID. (For example, Stone is 1 and Birch wood is 5:2)");
		add("mines.invalidPercent", "&cThat percentage does not exist.");
		add("mines.addSuccess", "&2Mine &6%0&2 is now &6%1&2 of block &6%2&2.");

		// Ranks
		add("ranks.noRanksLoaded", "&6No ranks are loaded.");
		add("ranks.highestRank", "&cYou have already reached the highest rank!");
		add("ranks.highestRank.other",
				"&cThat player has already reached the highest rank!");
		add("ranks.notEnoughMoney",
				"&cYou still need &d$%0&c to rank up to &r%1&r&c.");
		add("ranks.rankedUp", "&2You have ranked up to %0!");
		add("ranks.rankedUpBroadcast", "&d%0 &2ranked up to %1&r&2!");
		add("ranks.notARank", "&cThat rank does not exist!");
		add("ranks.alreadyLoaded", "&cThat rank is already loaded!");
		add("ranks.invalidPrice", "&cThat price is invalid!");
		add("ranks.addSuccess", "&2Successfully added rank %0&2!");
		add("ranks.addFail", "&cFailed to add rank %0&c.");
		add("ranks.removeSuccess", "&2Successfully removed rank %0&2!");
		add("ranks.removeFail", "&cFailed to remove rank %0&c.");
		add("ranks.lowestRank", "&cAlready at the lowest rank!");
		add("ranks.demoteSuccess",
				"&2Successfully demoted &6%0&2 to rank %1&2!");
		add("ranks.priceTooHigh", "&cPrices cannot exceed &6$999,999,999.99&c."); // Unused

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
		add("shops.createSuccess", "&2Successfully created shop!");
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
