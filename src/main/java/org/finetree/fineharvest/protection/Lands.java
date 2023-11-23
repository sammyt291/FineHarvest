package org.finetree.fineharvest.protection;

import org.finetree.fineharvest.FineHarvest;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Lands {
    public static boolean canLands(Player ply, Block b) {
        LandsIntegration api = LandsIntegration.of(FineHarvest.getPlugin());
        LandWorld world = api.getWorld(b.getWorld());
        if(world !=null){ // Lands is enabled in this world
            LandPlayer landPlayer = api.getLandPlayer(ply.getUniqueId());
            if (landPlayer != null) {
                return world.hasRoleFlag(landPlayer, b.getLocation(), me.angeschossen.lands.api.flags.type.Flags.BLOCK_BREAK, b.getType(), true);
            }
        }
        return false;
    }
}
