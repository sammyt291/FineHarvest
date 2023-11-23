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

import static org.finetree.fineharvest.FineHarvest.warnNoProtection;
import static org.finetree.fineharvest.protection.EssentialsAB.canEssentialsAntiBuild;
import static org.finetree.fineharvest.protection.GriefPrevention.canGriefPrev;
import static org.finetree.fineharvest.protection.Lands.canLands;
import static org.finetree.fineharvest.protection.Towny.canTowny;
import static org.finetree.fineharvest.protection.WG.canWorldGuard;
import static org.bukkit.Bukkit.getServer;

public class BuildCheck {

    public static boolean canBuild(Player ply, Block b) {
        if(hasPlugin("Towny")) {
            return canTowny(ply, b);
        }
        if(hasPlugin("Lands")){
            return canLands(ply, b);
        }
        if(hasPlugin("griefPrevention")) {
            return canGriefPrev(ply, b);
        }
        if(hasPlugin("ProtectionStones")){
            if(ProtStones.canProtectionStones(ply, b)){//Skip protectionstones check if no region, leave it up to WG global regions.
                return true;
            }
        }
        if(hasPlugin("PlotSquared")){
            return Plot2.canPlotSquared(ply, b);
        }
        if(hasPlugin("GriefDefender")){
            return GriefDef.canGriefDefender(ply, b);
        }
        /*if(hasPlugin("Residence")) {
            ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(ply);
            return rPlayer.canBreakBlock(b, true);
        }*/
        if(hasPlugin("WorldGuard")) {
            return canWorldGuard(ply, b);
        }
        //Check for both, as people be silly.
        if(hasPlugin("EssentialsAntiBuild") && hasPlugin("Essentials")){
            return canEssentialsAntiBuild(ply, b);
        }

        warnNoProtection();

        //Player can BlockBreak at this location?
        BlockBreakEvent e = new BlockBreakEvent(b, ply);
        FineHarvest.getPlugin().getServer().getPluginManager().callEvent(e);
        return !e.isCancelled();
    }

    public static boolean hasPlugin(String plugin){
        return getServer().getPluginManager().getPlugin(plugin) != null;
    }

    public static Plugin getPlugin(String plugin) {
        return getServer().getPluginManager().getPlugin(plugin);
    }
}
