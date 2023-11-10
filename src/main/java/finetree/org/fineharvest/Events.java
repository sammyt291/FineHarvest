package finetree.org.fineharvest;

import com.archyx.aureliumskills.skills.Skills;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

import static com.archyx.aureliumskills.api.AureliumAPI.addXp;
import static com.gmail.nossr50.api.ExperienceAPI.addXP;
import static finetree.org.fineharvest.Config.*;
import static finetree.org.fineharvest.FineHarvest.isAurelium;
import static finetree.org.fineharvest.FineHarvest.isMCMMO;
import static finetree.org.fineharvest.Sounds.breakSound;
import static finetree.org.fineharvest.Sounds.popSound;


public class Events implements Listener  {
    @EventHandler(priority= EventPriority.HIGH)
    public void use(PlayerInteractEvent e) {

        //Using permission and player doesn't have it, just do nothing.
        Player ply = e.getPlayer();
        if(usePermissions && !ply.hasPermission("fineharvest.use")){
            return;
        }

        //Get the item in hand
        ItemStack item = e.getItem();
        if(item == null){return;}
        Material type = item.getType();

        if (isHoe(type)) {

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clickedBlock = e.getClickedBlock();
                if(clickedBlock == null){return;}

                Material mat = clickedBlock.getType();
                if (isCrop(mat)) {

                    //Check supported plugins if we can build here
                    if(!BuildCheck.canBuild(ply, clickedBlock)){return;}

                    Ageable age = (Ageable) clickedBlock.getBlockData();
                    if(isRipe(mat, age.getAge())) {

                        popSound(clickedBlock, harvestVolume);

                        //"Replant"
                        age.setAge(0);
                        clickedBlock.setBlockData(age);

                        //Drop harvest items
                        ItemMeta itemMeta = item.getItemMeta();
                        if(itemMeta == null){return;}
                        dropSeeds(mat, clickedBlock, item);

                        //Delay durability damage with Unbreaking level.
                        //Should be average random percent chance. im making it hard percent difference. Deal with it I suppose. (Might affect it by a few % either way)
                        int unbreaking = 1;
                        if(!ignoreUnbreaking) {
                            unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY) + 1;
                        }

                        //Count harvests
                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                        NamespacedKey countKey = new NamespacedKey(FineHarvest.getPlugin(), "harvestCount");

                        Integer current = data.getOrDefault(countKey, PersistentDataType.INTEGER, 0);
                        int next = current + 1;
                        //ply.sendMessage("Count: " + next);
                        if (next >= (usesPerDurability * unbreaking)) {
                            next = 0;

                            //Use durability
                            Damageable dmg = (Damageable) itemMeta;
                            if (dmg == null) {
                                return;
                            }
                            dmg.setDamage(dmg.getDamage() + decrementDurabilityBy);
                            item.setItemMeta(dmg);
                            if (item.getType().getMaxDurability() <= dmg.getDamage()) {
                                if (item.getAmount() == 1) {
                                    //Break SFX
                                    breakSound(ply, 1);
                                }
                                item.setAmount(item.getAmount() - 1);
                            }
                        }//enough harvests to tick durability ?

                        data.set(countKey, PersistentDataType.INTEGER, next);
                        item.setItemMeta(itemMeta);

                        //McMMO Compat for Herbalism XP
                        if (isMCMMO) {
                            addXP(ply, "HERBALISM", ExperienceConfig.getInstance().getXp(PrimarySkillType.HERBALISM, mat), "PVE");
                        }

                        //AureliumSkills Compat for Farming XP
                        if(isAurelium){
                            //ply.sendMessage("Got: " + AureliumSkills.getSourceRegistry().values(Skills.FARMING)[0]);
                            addXp(ply, Skills.FARMING, FineHarvest.getAureliumSources().getDouble("sources.farming." + mat.toString().toLowerCase()));
                        }

                    } //isRipe
                } //isCrop
            } //isBlock & RClick
        } //isHoe
    } //playerInteract

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
}
