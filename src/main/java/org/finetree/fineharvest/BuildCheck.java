package org.finetree.fineharvest;

//import com.bekvon.bukkit.residence.Residence;
//import com.bekvon.bukkit.residence.containers.ResidencePlayer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.finetree.fineharvest.protection.GriefDef;
import org.finetree.fineharvest.protection.Plot2;
import org.finetree.fineharvest.protection.ProtStones;
import org.finetree.fineharvest.protection.SuperiorSkyblock2;

import static org.finetree.fineharvest.FineHarvest.warnNoProtection;
import static org.finetree.fineharvest.protection.EssentialsAB.canEssentialsAntiBuild;
import static org.finetree.fineharvest.protection.GriefPrevention.canGriefPrev;
import static org.finetree.fineharvest.protection.Lands.canLands;
import static org.finetree.fineharvest.protection.Towny.canTowny;
import static org.finetree.fineharvest.protection.WG.canWorldGuard;
import static org.bukkit.Bukkit.getServer;

public class BuildCheck {

    /**
     * The result of a build check. Some fallback checks publish a
     * {@link BlockBreakEvent}; integrations must know this so they do not
     * publish the same harvest twice.
     */
    public record BuildResult(boolean allowed, boolean blockBreakEventFired) {}

    public static boolean canBuild(Player ply, Block b) {
        return checkBuild(ply, b).allowed();
    }

    public static BuildResult checkBuild(Player ply, Block b) {
        if(hasPlugin("Towny")) {
            return new BuildResult(canTowny(ply, b), false);
        }
        if(hasPlugin("Lands")){
            return new BuildResult(canLands(ply, b), false);
        }
        if(hasPlugin("GriefPrevention")) {
            return new BuildResult(canGriefPrev(ply, b), false);
        }
        if(hasPlugin("ProtectionStones")){
            if(ProtStones.canProtectionStones(ply, b)){//Skip protectionstones check if no region, leave it up to WG global regions.
                return new BuildResult(true, false);
            }
        }
        if(hasPlugin("PlotSquared")){
            return new BuildResult(Plot2.canPlotSquared(ply, b), false);
        }
        if(hasPlugin("GriefDefender")){
            return new BuildResult(GriefDef.canGriefDefender(ply, b), false);
        }
        if (hasPlugin("SuperiorSkyblock2")) {
            if (SuperiorSkyblock2.canSuperiorSkyblock2(ply, b)) {//Skip SuperSkyblock2 check if no island, leave it up to WG global regions.
                return new BuildResult(true, false);
            }
        }
        /*if(hasPlugin("Residence")) {
            ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(ply);
            return rPlayer.canBreakBlock(b, true);
        }*/
        if(hasPlugin("WorldGuard")) {
            return new BuildResult(canWorldGuard(ply, b), false);
        }
        //Check for both, as people be silly.
        if(hasPlugin("EssentialsAntiBuild") && hasPlugin("Essentials")){
            return new BuildResult(canEssentialsAntiBuild(ply, b), false);
        }

        warnNoProtection();

        //Player can BlockBreak at this location?
        BlockBreakEvent e = new BlockBreakEvent(b, ply);
        FineHarvest.getPlugin().getServer().getPluginManager().callEvent(e);
        return new BuildResult(!e.isCancelled(), true);
    }

    public static boolean hasPlugin(String plugin){
        return getServer().getPluginManager().getPlugin(plugin) != null;
    }

    public static Plugin getPlugin(String plugin) {
        return getServer().getPluginManager().getPlugin(plugin);
    }
}
