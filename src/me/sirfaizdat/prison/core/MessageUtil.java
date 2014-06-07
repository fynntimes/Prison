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
		add("general.cmdNotFound", "&cThat command could not be found. Type &6%0 &cfor help.");
		add("general.noPermission", "&cYou don't have permission to run this command.");
		add("general.mustBePlayer", "&cYou must be in game to use this command.");
		add("general.notEnoughArgs", "&cNot enough arguments. &6Proper usage: %0");
		add("general.noCmdPassed", "&cPlease specify a command.");

		// Mines
		MessageUtil.add("mines.resetSuccess", "&2Successfully reset mine &6%0&2.");
		MessageUtil.add("mines.created", "&2Successfully created a new mine called &6%0.");
		MessageUtil.add("mines.makeWESel", "&cPlease make a WorldEdit selection first.");
		MessageUtil.add("mines.alreadyExists", "&cA mine with that name already exists.");
		MessageUtil.add("mines.failedToCreate", "&cCould not create a new mine. Check console for details.");
		MessageUtil.add("mines.notFound", "&cThat mine does not exist.");
		MessageUtil.add("mines.blockNotExist", "&cThat block does not exist.");
		MessageUtil.add("mines.notABlock", "&cThat item is not a block.");
		MessageUtil.add("mines.invalidId", "&cThat ID is invalid.");
		MessageUtil.add("mines.alreadyInMine", "&cThat block is already in this mine.");
		MessageUtil.add("mines.mineDoesntHaveThisBlock", "&cThe mine doesn't contain that block.");
		MessageUtil.add("mines.removedBlock", "&2Successfully removed block &6%0&2 from mine &6%1&2.");
		MessageUtil.add("mines.mineFull", "&cThat percentage might be too high, or the mine may be already full.");
		MessageUtil.add("mines.deletedMine", "&2Mine successfully deleted.");
		MessageUtil.add("mines.mustSpecifyID",
				"&cYou must use the block's item ID. (For example, Stone is 1 and Birch wood is 5:2)");
		MessageUtil.add("mines.invalidPercent",
				"&cThat percentage does not exist.");
		MessageUtil
				.add("mines.addSuccess",
						"&2Mine &6%0&2 is now &6%1&2 of block &6%2&2.");
	}
	
	public static void add(String key, String value){
		messages.put(key, Config.serverPrefix + Core.colorize(value));
	}
	
	public static String get(String key) {
		return messages.get(key);
	}
	
	public static String get(String key, String... replace) {
		String returnVal = messages.get(key);
		for(int i = 0; i < replace.length; i++) {
			returnVal = returnVal.replaceAll("%" + i, Core.colorize(replace[i]));
		}
		return returnVal;
	}
	
}
