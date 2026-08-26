package org.finetree.fineharvest.quests;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.finetree.fineharvest.FineHarvest;

/**
 * Compatibility bridge for the Quests farming quest type.
 *
 * <p>FineHarvest replaces a normal crop break with an in-place harvest, so
 * Quests never sees the Bukkit event it uses to progress farming objectives.
 * Publishing the equivalent event lets Quests process the harvest through its
 * supported listener path.</p>
 */
public final class Quests {

    private Quests() {}

    public static boolean publishHarvest(Player player, Block crop) {
        BlockBreakEvent event = new BlockBreakEvent(crop, player);
        FineHarvest.getPlugin().getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
}
