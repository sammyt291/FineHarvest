package org.finetree.fineharvest.protection;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SuperiorSkyblock2 {

    public static boolean canSuperiorSkyblock2(Player ply, Block b) {
        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ply.getUniqueId());
        Island island = SuperiorSkyblockAPI.getIslandAt(b.getLocation());
        //ply.sendMessage("SuperiorSkyBlock2 Allowed");
        if (sp != null && island != null) {
            return island.isMember(sp) || sp.hasBypassModeEnabled();
        }
        return false;
    }

}
