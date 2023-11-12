package finetree.org.fineharvest;

//import com.bekvon.bukkit.residence.Residence;
//import com.bekvon.bukkit.residence.containers.ResidencePlayer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import static finetree.org.fineharvest.FineHarvest.noProtWarn;
import static finetree.org.fineharvest.FineHarvest.warnNoProtection;
import static finetree.org.fineharvest.protection.GriefDef.canGriefDefender;
import static finetree.org.fineharvest.protection.GriefPrevention.canGriefPrev;
import static finetree.org.fineharvest.protection.Lands.canLands;
import static finetree.org.fineharvest.protection.Plot2.canPlotSquared;
import static finetree.org.fineharvest.protection.ProtStones.canProtectionStones;
import static finetree.org.fineharvest.protection.Towny.canTowny;
import static finetree.org.fineharvest.protection.WG.canWorldGuard;
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
            if(canProtectionStones(ply, b)){//Skip protectionstones check if no region, leave it up to WG global regions.
                return true;
            }
        }
        if(hasPlugin("PlotSquared")){
            return canPlotSquared(ply, b);
        }
        if(hasPlugin("GriefDefender")){
            return canGriefDefender(ply, b);
        }
        /*if(hasPlugin("Residence")) {
            ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(ply);
            return rPlayer.canBreakBlock(b, true);
        }*/
        if(hasPlugin("WorldGuard")) {
            return canWorldGuard(ply, b);
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
