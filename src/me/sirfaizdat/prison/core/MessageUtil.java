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

    // &c - Errors
    // &a - Success messages
    // &6 - Extra tips or normal; uncategorized.

    public MessageUtil() {
        // Core
        add("general.cmdNotFound", "&cThat command could not be found. Type &6%0 &cfor help.");
        add("general.noPermission", "&cYou don't have permission to use this command.");
        add("general.mustBePlayer", "&cYou must be in game to use this command.");
        add("general.notEnoughArgs", "&cNot enough arguments. &6Proper usage: %0");
        add("general.noCmdPassed", "&cPlease specify a command.");
        add("general.updateAvailable", "&aThere is a new version of Prison available &7(%0)&a! &6Type &c/prison update &6to update.");
        add("general.versionString", "&6Prison v" + Prison.i().getDescription().getVersion() + "\n&cAuthors: TheOneAndOnlyDMP9, SirFaizdat\n&cTester: MattXXXQ\n&bWebsite: " + Prison.i().getDescription().getWebsite() + "\n&7(C) 2015 TheOneAndOnlyDMP9 and SirFaizdat All Rights Reserved.");
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
        add("mines.resetFailed", "&cFailed to reset mine &6%0&c. Check console for details.");
        add("mines.created", "&aSuccessfully created a new mine called &6%0.");
        add("mines.makeWESel", "&cPlease make a WorldEdit selection first.");
        add("mines.alreadyExists", "&cA mine with that name already exists.");
        add("mines.failedToCreate", "&cCould not create a new mine. Check console for details.");
        add("mines.notFound", "&cThat mine does not exist.");
        add("mines.blockNotExist", "&cThat block does not exist.");
        add("mines.notABlock", "&cThat item is not a block.");
        add("mines.invalidId", "&cThat ID is invalid.");
        add("mines.alreadyInMine", "&cThat block is already in this mine.");
        add("mines.mineDoesntHaveThisBlock", "&cThe mine doesn't contain that block.");
        add("mines.removedBlock", "&aSuccessfully removed block &6%0&a from mine &6%1&a.");
        add("mines.mineFull", "&cThat percentage might be too high, or the mine may be already full.");
        add("mines.deletedMine", "&aMine successfully deleted.");
        add("mines.mustSpecifyID", "&cYou must use the block's item ID. (For example, Stone is 1 and Birch wood is 5:2)");
        add("mines.invalidPercent", "&cInvalid percentage.");
        add("mines.addSuccess", "&aMine &6%0&a is now &6%1&a of block &6%2&a.");
        add("mines.addedRank", "&aSuccessfully added rank &6%0&a to mine &6%1&a!");
        add("mines.removedRank", "&aSuccessfully removed rank &6%0&a from mine &6%1&a!");
        add("mines.removeFailure", "&cThat rank is not associated with this mine.");
        add("mines.rankAlreadyAdded", "&6That rank is already associated with this mine.");
        add("mines.redefineSuccess", "&aSuccessfully redefined mine &6%0&a.");
        add("mines.wrongWorld", "&cThis mine is in the world &6%0&c. Please go to that world to set the spawn for this mine.");
        add("mines.missingWorld", "&cThe world for this mine is missing and therefore a spawn cannot be set for it.");
        add("mines.setSpawn", "&aThe spawn for this mine was set!");
        // Ranks
        add("ranks.noRanksLoaded", "&6No ranks are loaded.");
        add("ranks.highestRank", "&cYou have already reached the highest rank!");
        add("ranks.highestRank.other", "&cThat player has already reached the highest rank!");
        add("ranks.notEnoughMoney", "&cYou still need &d$%0&c to rank up to &r%1&r&c.");
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
        add("ranks.demoteSuccess", "&aSuccessfully demoted &6%0&a to rank %1&a!");
        add("ranks.notAPlayer", "&cThat player does not exist or is offline.");
        add("ranks.editSuccess", "&aSuccessfully edited rank!");
        add("ranks.editFail", "&cFailed to edit rank.");
        add("ranks.notAGroup", "&cThat rank is not loaded into your permissions plugin.");

        // Shops
        add("shops.noPermission", "&cYou don't have permission to create a shop.");
        add("shops.serverNoPermission", "&cYou don't have permission to create a server shop.");
        add("shops.othersNoPermission", "&cYou don't have permission to create a shop for someone else.");
        add("shops.userNotFound", "&cThat player could not be found.");
        add("shops.invalidItemAndQuantity", "&cThe item and quantity are invalid.");
        add("shops.invalidQuantity", "&cThe quantity is invalid.");
        add("shops.invalidItem", "&cThe item is invalid.");
        add("shops.invalidPrice", "&cThat price is invalid.");
        add("shops.createSuccess", "&aSuccessfully created shop!");

        // DMP9
        add("warps.warping", "&6Warping...");
        add("warps.tolessargs", "&cToo few arguments! Use the command like this:\n&c/pwarps setwarp {NAME}");
    }

    public static void add(String key, String value) {
        messages.put(key, Prison.i().config.serverPrefix + Prison.colorize(value));
    }

    public static String get(String key) {
        return messages.get(key);
    }

    public static String get(String key, String... replace) {
        String returnVal = messages.get(key);
        if (returnVal == null) {
            return Prison.colorize("&cInvalid message key - &6" + key + "&c.");
        }
        for (int i = 0; i < replace.length; i++) {
            returnVal = returnVal.replaceAll("%" + i, Prison.colorize(replace[i]));
        }
        return returnVal;
    }

}
