package finetree.org.fineharvest;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class BuildCheck {

    public static boolean canBuild(Player ply, Block b) {
        if(hasPlugin("WorldGuard")) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(ply);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            if(query.testState(BukkitAdapter.adapt(b.getLocation()), localPlayer, Flags.BLOCK_BREAK)) {
                return true;
            }
        }
        if(hasPlugin("Towny")) {
            boolean canDestroy = PlayerCacheUtil.getCachePermission(ply, b.getLocation(), b.getType(), TownyPermission.ActionType.DESTROY);
            if(canDestroy){
                return true;
            }
        }
        if(hasPlugin("Lands")){
            LandsIntegration api = LandsIntegration.of(FineHarvest.getPlugin());

            LandWorld world = api.getWorld(b.getWorld());
            if (world != null) { // Lands is enabled in this world
                LandPlayer landPlayer = api.getLandPlayer(ply.getUniqueId());
                if(landPlayer != null) {
                    if (world.hasRoleFlag(landPlayer, b.getLocation(), me.angeschossen.lands.api.flags.type.Flags.BLOCK_BREAK, b.getType(), false)) {
                        return true;
                    }
                }
            }
        }
        if(hasPlugin("griefPrevention")){
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(b.getLocation(), true, null);
            if(claim.checkPermission(ply.getUniqueId(), ClaimPermission.Build, null) == null){
                return true;
            }
        }
        return false;
    }

    private static boolean hasPlugin(String plugin){
        return getServer().getPluginManager().getPlugin(plugin) != null;
    }

    public static Plugin getPlugin(String plugin) {
        return getServer().getPluginManager().getPlugin(plugin);
    }
}
