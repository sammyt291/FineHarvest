package finetree.org.fineharvest;

//import com.bekvon.bukkit.residence.Residence;
//import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.griefdefender.api.User;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.espi.protectionstones.PSRegion;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class BuildCheck {

    public static boolean canBuild(Player ply, Block b) {
        if(hasPlugin("Towny")) {
            boolean canDestroy = PlayerCacheUtil.getCachePermission(ply, b.getLocation(), b.getType(), TownyPermission.ActionType.DESTROY);
            if(canDestroy){
                ply.sendMessage("Towny Allowed");
                return true;
            }else{
                return false;
            }
        }
        if(hasPlugin("Lands")){
            LandsIntegration api = LandsIntegration.of(FineHarvest.getPlugin());
            LandWorld world = api.getWorld(b.getWorld());
            if (world != null) { // Lands is enabled in this world
                LandPlayer landPlayer = api.getLandPlayer(ply.getUniqueId());
                if(landPlayer != null) {
                    if (world.hasRoleFlag(landPlayer, b.getLocation(), me.angeschossen.lands.api.flags.type.Flags.BLOCK_BREAK, b.getType(), true)) {
                        ply.sendMessage("Lands Allowed");
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        }
        if(hasPlugin("griefPrevention")){
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(b.getLocation(), true, null);
            if(claim.checkPermission(ply.getUniqueId(), ClaimPermission.Build, null) == null){
                ply.sendMessage("griefPrevention Allowed");
                return true;
            }else{
                return false;
            }
        }
        if(hasPlugin("ProtectionStones")){
            //PSPlayer player = PSPlayer.fromPlayer(ply);
            PSRegion region = PSRegion.fromLocation(b.getLocation());

            //No Protection zone, so must be unprotected area.
            if(region != null) {

                //Is building generally allowed?
                StateFlag.State build = region.getWGRegion().getFlag(Flags.BUILD);
                if (build == StateFlag.State.ALLOW) {
                    ply.sendMessage("ProtectionStones BUILD");
                    return true;
                }

                //Perhaps just block breaking?
                StateFlag.State blockbreak = region.getWGRegion().getFlag(Flags.BLOCK_BREAK);
                if (blockbreak == StateFlag.State.ALLOW) {
                    ply.sendMessage("ProtectionStones BLOCKBREAK");
                    return true;
                }

                //Perhaps an Owner?
                DefaultDomain owners = region.getWGRegion().getOwners();
                if (owners.contains(ply.getUniqueId())) {
                    ply.sendMessage("ProtectionStones Owner");
                    return true;
                }

                //Are they a member?
                DefaultDomain members = region.getWGRegion().getMembers();
                if (members.contains(ply.getUniqueId())) {
                    ply.sendMessage("ProtectionStones Member");
                    return true;
                }

                //Default
                return false;

            }//Skip protectionstones check if no region, leave it up to WG global regions.
        }
        if(hasPlugin("PlotSquared")){
            PlotPlayer<Player> plotply = PlotPlayer.from(ply);
            Plot plot = plotply.getCurrentPlot();

            //Are they a member?
            HashSet<UUID> members = plot.getMembers();
            if(members.contains(plotply.getUUID())){
                ply.sendMessage("PlotSquared Member");
                return true;
            }
            //Or an Owner perhaps?
            Set<UUID> owners = plot.getOwners();
            if(owners.contains(plotply.getUUID())){
                ply.sendMessage("PlotSquared Owner");
                return true;
            }

            //Default
            return false;
        }
        if(hasPlugin("GriefDefender")){
            User user = (User) ply;
            return user.canBreak(b.getLocation());
        }
        /*if(hasPlugin("Residence")) {
            ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(ply);
            return rPlayer.canBreakBlock(b, true);
        }*/
        if(hasPlugin("WorldGuard")) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(ply);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            if(query.testState(BukkitAdapter.adapt(b.getLocation()), localPlayer, Flags.BLOCK_BREAK)) {
                ply.sendMessage("WorldGuard BLOCKBREAK");
                return true;
            }
            if(query.testState(BukkitAdapter.adapt(b.getLocation()), localPlayer, Flags.BUILD)) {
                ply.sendMessage("WorldGuard BUILD");
                return true;
            }
        }
        ply.sendMessage("No Build Perms");
        return false;
    }

    private static boolean hasPlugin(String plugin){
        return getServer().getPluginManager().getPlugin(plugin) != null;
    }

    public static Plugin getPlugin(String plugin) {
        return getServer().getPluginManager().getPlugin(plugin);
    }
}
