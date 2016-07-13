package me.sirfaizdat.prison.ranks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;
import org.bukkit.Bukkit;

import java.text.NumberFormat;

/**
 * Support for MVdWPlaceholderAPI, which is a popular one.
 *
 * @author SirFaizdat
 * @since 2.2.1
 */
public class PlaceholderSupport {

    public void init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) return;
        Prison.l.info("Detected MVdWPlaceholder API. Registering placeholders...");

        PlaceholderAPI.registerPlaceholder(Prison.i(), "currentRank", rankReplacer());
        PlaceholderAPI.registerPlaceholder(Prison.i(), "nextRank", nextRankReplacer());
        PlaceholderAPI.registerPlaceholder(Prison.i(), "amountNeeded", amountNeededReplacer());
    }

    private PlaceholderReplacer rankReplacer() {
        return new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getPlayer() == null) return "Player needed";
                Rank currentRank = Ranks.i.getUserInfo(placeholderReplaceEvent.getPlayer().getName()).getCurrentRank();
                if (currentRank == null) return "No rank";
                return currentRank.getPrefix();
            }
        };
    }

    private PlaceholderReplacer nextRankReplacer() {
        return new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getPlayer() == null) return "Player needed";
                Rank nextRank = Ranks.i.getUserInfo(placeholderReplaceEvent.getPlayer().getName()).getNextRank();
                if (nextRank == null) return "Highest rank";
                return nextRank.getPrefix();
            }
        };
    }

    private PlaceholderReplacer amountNeededReplacer() {
        return new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                if (placeholderReplaceEvent.getPlayer() == null) return "Player needed";
                Rank nextRank = Ranks.i.getUserInfo(placeholderReplaceEvent.getPlayer().getName()).getNextRank();
                if (nextRank == null) return "Highest rank";
                double amountNeededD = nextRank.getPrice() - Ranks.i.eco.getBalance(placeholderReplaceEvent.getPlayer());
                return NumberFormat.getCurrencyInstance().format(amountNeededD);
            }
        };
    }


}
