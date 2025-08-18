package org.finetree.fineharvest.skills;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import org.finetree.fineharvest.BuildCheck;
import org.finetree.fineharvest.FineHarvest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AureliumSkills {

    public static void aureliumAddXP(Player ply, Material mat) {
            AuraSkillsApi.get().getUser(ply.getUniqueId()).addSkillXp(Skills.FARMING,
                    FineHarvest.getAuraSources().getDouble("sources." + mat.toString().toLowerCase() + ".xp"));
    }

}
