package org.finetree.fineharvest.protection;

import com.griefdefender.api.User;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GriefDef {

    public static boolean canGriefDefender(Player ply, Block b){
        User user = (User) ply;
        return user.canBreak(b.getLocation());
    }

}
