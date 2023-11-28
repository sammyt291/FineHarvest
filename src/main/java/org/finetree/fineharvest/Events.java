package org.finetree.fineharvest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.finetree.fineharvest.skills.AureliumSkills;
import org.finetree.fineharvest.skills.mcMMO;

import java.io.File;
import java.util.Random;

import static org.finetree.fineharvest.Config.*;
import static org.finetree.fineharvest.FineHarvest.skillMods;
import static org.finetree.fineharvest.FineHarvest.tag;


public class Events implements Listener  {
    @EventHandler(priority= EventPriority.HIGH)
    public void onUse(PlayerInteractEvent e) {

        //Using permission and player doesn't have it, just do nothing.
        Player ply = e.getPlayer();
        if(usePermissions && !ply.hasPermission("fineharvest.use")){
            return;
        }

        //Get the item in hand
        ItemStack item = e.getItem();
        if(item == null){ return; }
        Material type = item.getType();

        //Check if the used item a Hoe?
        if (!isHoe(type)) { return; }

        //Check we Right-Clicked?
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) { return; }

        //Check we Right-Clicked a block?
        Block clickedBlock = e.getClickedBlock();
        if(clickedBlock == null){ return; }

        //Check supported plugins if we can build here
        if(!BuildCheck.canBuild(ply, clickedBlock)){ return; }

        //Check it a crop we clicked?
        Material mat = clickedBlock.getType();
        if (!isCrop(mat)) { return; }

        //Check the crop is ripe
        Ageable age = (Ageable) clickedBlock.getBlockData();
        if(!isRipe(mat, age.getAge())) { return; }

        Sounds.popSound(clickedBlock, harvestVolume);

        //"Replant" the crop
        age.setAge(0);
        clickedBlock.setBlockData(age);

        //Drop harvested items
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null){return;}
        dropSeeds(mat, clickedBlock, item);

        //Delay durability damage with Unbreaking level.
        //Should be average random percent chance. im making it hard percent difference. Deal with it, I suppose. (Might affect it by a few % either way)
        int unbreaking = 1;
        if(!ignoreUnbreaking) {
            unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY) + 1;
        }

        //Count harvests
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        NamespacedKey countKey = new NamespacedKey(FineHarvest.getPlugin(), "harvestCount");

        Integer current = data.getOrDefault(countKey, PersistentDataType.INTEGER, 0);
        int next = current + 1;

        //Check enough harvests to tick durability ?
        if (next >= (usesPerDurability * unbreaking)) {
            next = 0;

            //Set used durability
            Damageable dmg = (Damageable) itemMeta;
            dmg.setDamage(dmg.getDamage() + decrementDurabilityBy);
            item.setItemMeta(dmg);

            //Check if we broke it?
            if (item.getType().getMaxDurability() <= dmg.getDamage()) {
                if (item.getAmount() == 1) {
                    //Break SFX
                    Sounds.breakSound(ply, 1);
                }
                item.setAmount(item.getAmount() - 1);
            }
        }

        //Set harvest count and new durability
        data.set(countKey, PersistentDataType.INTEGER, next);
        item.setItemMeta(itemMeta);

        //API McMMO Compat for Herbalism XP
        if(mcMMOSupport && skillMods.getOrDefault("mcMMO", false)){
            mcMMO.mcmmoAddXP(ply, mat);
        }

        //API AureliumSkills Compat for Farming XP
        if(aureliumSkillsSupport && skillMods.getOrDefault("AureliumSkills", false)){
            AureliumSkills.aureliumAddXP(ply, mat);
        }

    } //onUse

    private boolean isHoe(Material material) {
        switch (material) {
            case WOODEN_HOE:
            case STONE_HOE:
            case IRON_HOE:
            case GOLDEN_HOE:
            case DIAMOND_HOE:
            case NETHERITE_HOE:
                return true;
            default:
                return false;
        }
    }

    private boolean isCrop(Material material) {
        switch (material) {
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
            case NETHER_WART:
                return true;
            default:
                return false;
        }
    }

    private boolean isRipe(Material material, int age) {
        switch (material) {
            case WHEAT:
            case CARROTS:
            case POTATOES:
                return age == 7;
            case BEETROOTS:
                return age == 4;
            case NETHER_WART:
                return age == 3;
            default:
                return false;
        }
    }

    private void dropSeeds(Material mat, Block blk, ItemStack hoe) {
        //Add fortune level to max drop.
        int fortune = 0;
        if(!ignoreFortune) {
            fortune = hoe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        }

        ItemStack drops = new ItemStack(Material.WHEAT_SEEDS, rand(minWheatSeeds,maxWheatSeeds + fortune));
        ItemStack harvest = new ItemStack(Material.WHEAT, rand(minWheat, maxWheat));

        switch (mat) {
            case WHEAT:
                if(drops.getAmount() > 0) {
                    blk.getWorld().dropItemNaturally(blk.getLocation(), drops);
                }
                if(harvest.getAmount() > 0) {
                    blk.getWorld().dropItemNaturally(blk.getLocation(), harvest);
                }
                break;
            case CARROTS:
                drops.setType(Material.CARROT);
                drops.setAmount(rand(minCarrots, maxCarrots + fortune));
                if(drops.getAmount() > 0) {
                    blk.getWorld().dropItemNaturally(blk.getLocation(), drops);
                }
                break;
            case POTATOES:
                drops.setType(Material.POTATO);
                drops.setAmount(rand(minPotatos, maxPotatos + fortune));
                if(drops.getAmount() > 0){
                    blk.getWorld().dropItemNaturally(blk.getLocation(), drops);
                }
                break;
            case BEETROOTS:
                drops.setType(Material.BEETROOT_SEEDS);
                drops.setAmount(rand(minBeetrootSeeds, maxBeetrootSeeds + fortune));
                harvest.setType(Material.BEETROOT);
                harvest.setAmount(rand(minBeetroot, maxBeetroot));
                if(drops.getAmount() > 0) {
                    blk.getWorld().dropItemNaturally(blk.getLocation(), drops);
                }
                if(harvest.getAmount() > 0){
                    blk.getWorld().dropItemNaturally(blk.getLocation(), harvest);
                }
                break;
            case NETHER_WART:
                drops.setType(Material.NETHER_WART);
                drops.setAmount(rand(minNetherWart, maxNetherWart + fortune));
                if(drops.getAmount() > 0) {
                    blk.getWorld().dropItemNaturally(blk.getLocation(), drops);
                }
                break;
        }
    }

    public static int rand(int a, int b) {
        Random random = new Random();
        return a + random.nextInt(b - a + 1);
    }

    //If AureliumSkills reloads we can re-grab its config incase it changed.
    @EventHandler
    public void plugEnable(PluginEnableEvent e) {
        Plugin plugin = e.getPlugin();
        switch( plugin.getDescription().getName() ){
            case "AureliumSkills":
                if(!aureliumSkillsSupport){ break; }
                skillMods.put( "AureliumSkills", true );
                plugin.getServer().getConsoleSender().sendMessage( tag
                        + ChatColor.GREEN + "AureliumSkills support enabled"
                        + ChatColor.RESET + " - Reloading Aurelium XP Values." );
                File file = new File("plugins/AureliumSkills/sources_config.yml");
                FineHarvest.AureliumSources = YamlConfiguration.loadConfiguration(file);
                break;
            case "mcMMO":
                if(!mcMMOSupport){ break; }
                skillMods.put( "mcMMO", true );
                plugin.getServer().getConsoleSender().sendMessage( tag
                        + ChatColor.GREEN + "mcMMO support enabled" );
                break;
        }
    }
    @EventHandler
    public void plugDisable(PluginDisableEvent e) {
        Plugin plugin = e.getPlugin();
        switch( plugin.getDescription().getName() ){
            case "AureliumSkills":
                if(!aureliumSkillsSupport){ break; }
                skillMods.put( "AureliumSkills", false );
                plugin.getServer().getConsoleSender().sendMessage( tag
                        + ChatColor.RED + "AureliumSkills support disabled" );
                break;
            case "mcMMO":
                if(!mcMMOSupport){ break; }
                skillMods.put( "mcMMO", false );
                plugin.getServer().getConsoleSender().sendMessage( tag
                        + ChatColor.RED + "mcMMO support disabled" );
                break;
        }
    }
}
