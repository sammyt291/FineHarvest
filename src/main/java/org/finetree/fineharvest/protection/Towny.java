package org.finetree.fineharvest.protection;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Towny {

    public static boolean canTowny(Player ply, Block b) {
        boolean canDestroy = PlayerCacheUtil.getCachePermission(ply, b.getLocation(), b.getType(), TownyPermission.ActionType.DESTROY);
        if (canDestroy) {
            //ply.sendMessage("Towny Allowed");
            return true;
        } else {
            return false;
        }
    }
}
