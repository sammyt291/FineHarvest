package org.finetree.fineharvest.protection;

import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Plot2 {

    public static boolean canPlotSquared(Player ply, Block b){
        PlotPlayer<Player> plotply = PlotPlayer.from(ply);
        Plot plot = plotply.getCurrentPlot();

        //Are they a member?
        HashSet<UUID> members = plot.getMembers();
        if(members.contains(plotply.getUUID())){
            //ply.sendMessage("PlotSquared Member");
            return true;
        }
        //Or an Owner perhaps?
        Set<UUID> owners = plot.getOwners();
        if(owners.contains(plotply.getUUID())){
            //ply.sendMessage("PlotSquared Owner");
            return true;
        }

        return false;
    }

}
