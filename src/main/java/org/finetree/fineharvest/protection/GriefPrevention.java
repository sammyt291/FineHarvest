package org.finetree.fineharvest.protection;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GriefPrevention {

    public static boolean canGriefPrev(Player ply, Block b){
        Claim claim = me.ryanhamshire.GriefPrevention.GriefPrevention.instance.dataStore.getClaimAt(b.getLocation(), true, null);
        if (claim != null) {
            return claim.checkPermission(ply.getUniqueId(), ClaimPermission.Build, null) == null;
        }
        return false;
    }

}
