package org.finetree.fineharvest.skills;

import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.gmail.nossr50.api.ExperienceAPI.addXP;

public class mcMMO {

    public static void mcmmoAddXP(Player ply, Material mat) {
        addXP(ply, "HERBALISM", ExperienceConfig.getInstance().getXp(PrimarySkillType.HERBALISM, mat), "PVE");
    }

}
