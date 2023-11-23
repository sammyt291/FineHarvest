package org.finetree.fineharvest.protection;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.espi.protectionstones.PSRegion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ProtStones {

    public static boolean canProtectionStones(Player ply, Block b){
        //PSPlayer player = PSPlayer.fromPlayer(ply);
        PSRegion region = PSRegion.fromLocation(b.getLocation());

        //No Protection zone, so must be unprotected area.
        if(region != null) {

            //Is building generally allowed?
            StateFlag.State build = region.getWGRegion().getFlag(Flags.BUILD);
            if (build == StateFlag.State.ALLOW) {
                //ply.sendMessage("ProtectionStones BUILD");
                return true;
            }

            //Perhaps just block breaking?
            StateFlag.State blockbreak = region.getWGRegion().getFlag(Flags.BLOCK_BREAK);
            if (blockbreak == StateFlag.State.ALLOW) {
                //ply.sendMessage("ProtectionStones BLOCKBREAK");
                return true;
            }

            //Perhaps an Owner?
            DefaultDomain owners = region.getWGRegion().getOwners();
            if (owners.contains(ply.getUniqueId())) {
                //ply.sendMessage("ProtectionStones Owner");
                return true;
            }

            //Are they a member?
            DefaultDomain members = region.getWGRegion().getMembers();
            if (members.contains(ply.getUniqueId())) {
                //ply.sendMessage("ProtectionStones Member");
                return true;
            }

            //Default
            return false;

        }

        return false;
    }

}
