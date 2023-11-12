package finetree.org.fineharvest.skills;

import com.archyx.aureliumskills.skills.Skills;
import finetree.org.fineharvest.FineHarvest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.archyx.aureliumskills.api.AureliumAPI.addXp;

public class AureliumSkills {

    public static void aureliumAddXP(Player ply, Material mat) {
        addXp(ply, Skills.FARMING, FineHarvest.getAureliumSources().getDouble("sources.farming." + mat.toString().toLowerCase()));
    }

}
