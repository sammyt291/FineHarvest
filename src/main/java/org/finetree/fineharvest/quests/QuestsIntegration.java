package org.finetree.fineharvest.quests;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.finetree.fineharvest.BuildCheck;
import org.finetree.fineharvest.FineHarvest;

/**
 * Compatibility bridge for the LMBishop and PikaMug Quests farming task types.
 *
 * <p>FineHarvest replaces a normal crop break with an in-place harvest, so
 * Quests never sees the Bukkit event it uses to progress farming objectives.
 * Publishing the equivalent event lets Quests process the harvest through its
 * supported listener path.</p>
 */
public final class QuestsIntegration {

    private static final String LMBISHOP_MAIN_PACKAGE = "com.leonardobishop.quests.";
    private static final String PIKAMUG_MAIN_PACKAGE = "me.pikamug.quests.";

    private QuestsIntegration() {}

    /**
     * Enables the bridge only for the two supported plugins which share the
     * Bukkit plugin name "Quests".
     */
    public static boolean isEnabled() {
        Plugin plugin = BuildCheck.getPlugin("Quests");
        return plugin != null
                && plugin.isEnabled()
                && supportsMainClass(plugin.getDescription().getMain());
    }

    static boolean supportsMainClass(String mainClass) {
        return mainClass.startsWith(LMBISHOP_MAIN_PACKAGE)
                || mainClass.startsWith(PIKAMUG_MAIN_PACKAGE);
    }

    public static boolean publishHarvest(Player player, Block crop) {
        BlockBreakEvent event = new BlockBreakEvent(crop, player);
        FineHarvest.getPlugin().getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
}
