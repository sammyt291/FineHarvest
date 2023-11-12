package finetree.org.fineharvest.protection;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EssentialsAB {

    public static boolean canEssentialsAntiBuild(Player ply, Block b){

        Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        User user = essentials.getUser(ply);

        String blockPerm = "essentials.build.break." + b.getType();
        return user.isAuthorized(blockPerm);

    }

}
