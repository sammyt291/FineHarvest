package org.finetree.fineharvest;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

import static org.finetree.fineharvest.Config.*;

public class Sounds {

    public static void popSound(Block b, float mul){
        World w = b.getLocation().getWorld();
        if(w == null){return;}
        float pitchAdj = 0;
        if(harvestVaried){
            pitchAdj = rand(-harvestVariance, harvestVariance);
        }

        //If Version is 1.14.X then choose another default sound (Bees didn't exist until 1.15)
        if(Bukkit.getVersion().contains("1.14")){
            if( harvestSound.equals("BLOCK_BEEHIVE_EXIT") ){
                harvestSound = "ENTITY_ITEM_PICKUP";
            }
        }

        w.playSound(b.getLocation(), Sound.valueOf(harvestSound), mul, harvestPitch + pitchAdj);
    }

    /**
     * Plays the item break sound effect at the player's location.
     * <p>
     * This method is called when a tool breaks
     *
     * @param p   The player at whose location the sound will be played.
     * @param mul The volume multiplier for the sound effect.
     */
    public static void breakSound(Player p, float mul){
        World w = p.getLocation().getWorld();
        if(w == null){return;}
        w.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, mul, 1F + rand(-0.5F, 0.5F));
    }

    // Generates a random float between A and B
    public static float rand(float a, float b) {
        Random random = new Random();
        return a + random.nextFloat() * (b - a);
    }

}
