package org.finetree.fineharvest.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WG {

    public static boolean canWorldGuard(Player ply, Block b){
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(ply);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if(query.testState(BukkitAdapter.adapt(b.getLocation()), localPlayer, Flags.BLOCK_BREAK)) {
            return true;
        }
        if(query.testState(BukkitAdapter.adapt(b.getLocation()), localPlayer, Flags.BUILD)) {
            return true;
        }

        return false;
    }

}
