package org.finetree.fineharvest;

import redempt.redlib.config.annotations.Comment;

public class Config {

    @Comment("")
    @Comment("=====================")
    @Comment("=== MAIN SETTINGS ===")
    @Comment("=====================")

    @Comment("")
    @Comment("How many crops should be harvested before the Hoe takes 1 durability damage?")
    @Comment("Default: 4")
    public static int usesPerDurability = 4;

    @Comment("")
    @Comment("How much damage should be done per above number of uses?")
    @Comment("Default: 1")
    public static int decrementDurabilityBy = 1;

    @Comment("")
    @Comment("Should the Fortune enchant be ignored when harvesting? (Reduces how OP this plugin becomes)")
    @Comment("Default: true")
    public static boolean ignoreFortune = true;

    @Comment("")
    @Comment("Should the Unbreaking enchant be ignored when harvesting? (Reduces how OP this plugin becomes)")
    @Comment("Default: true")
    public static boolean ignoreUnbreaking = true;

    @Comment("")
    @Comment("Should the plugin require the player to have the \"fineharvest.use\" permission?")
    @Comment("Default: true")
    public static boolean usePermissions = true;

    @Comment("")
    @Comment("======================")
    @Comment("=== SOUND SETTINGS ===")
    @Comment("======================")

    @Comment("")
    @Comment("What sound should harvesting make?")
    @Comment("Default: BLOCK_CROP_BREAK")
    public static String harvestSound = "BLOCK_CROP_BREAK";

    @Comment("")
    @Comment("How loud should the harvest sound be?")
    @Comment("Default: 0.15")
    public static float harvestVolume = 0.15F;

    @Comment("")
    @Comment("What pitch should the harvest sound be?")
    @Comment("Default: 1.0")
    public static float harvestPitch = 1.0F;

    @Comment("")
    @Comment("Should the harvest sound be varied pitch?")
    @Comment("Default: true")
    public static boolean harvestVaried = true;

    @Comment("")
    @Comment("How much should the pitch variance be?")
    @Comment("Default: 0.5")
    public static float harvestVariance = 0.5F;

    @Comment("")
    @Comment("=======================")
    @Comment("=== COMPAT SETTINGS ===")
    @Comment("=======================")

    @Comment("")
    @Comment("Enable AureliumSkills support?")
    @Comment("Default: true")
    public static boolean aureliumSkillsSupport = true;

    @Comment("")
    @Comment("Enable mcMMO support?")
    @Comment("Default: true")
    public static boolean mcMMOSupport = true;

    @Comment("")
    @Comment("=====================")
    @Comment("=== DROP SETTINGS ===")
    @Comment("=====================")
    @Comment("Note: all values below are after the plugin has planted a crop")
    @Comment("E.g. min value of 0 will still replant, but not drop any more.")

    @Comment("")
    @Comment("WHEAT seeds")
    @Comment("Default: Min 0 | Max 1")
    public static int minWheatSeeds = 0;
    public static int maxWheatSeeds = 1;

    @Comment("")
    @Comment("BEETROOT seeds")
    @Comment("Default: Min 0 | Max 1")
    public static int minBeetrootSeeds = 0;
    public static int maxBeetrootSeeds = 1;

    @Comment("")
    @Comment("BEETROOT")
    @Comment("Default: Min 1 | Max 3")
    public static int minBeetroot = 1;
    public static int maxBeetroot = 3;

    @Comment("")
    @Comment("CARROTS")
    @Comment("Default: Min 0 | Max 2")
    public static int minCarrots = 0;
    public static int maxCarrots = 2;

    @Comment("")
    @Comment("POTATOS")
    @Comment("Default: Min 0 | Max 2")
    public static int minPotatos = 0;
    public static int maxPotatos = 2;

    @Comment("")
    @Comment("WHEAT")
    @Comment("Default: Min 1 | Max 1")
    public static int minWheat = 1;
    public static int maxWheat = 1;

    @Comment("")
    @Comment("NETHER WART")
    @Comment("Default: Min 2 | Max 3")
    public static int minNetherWart = 1;
    public static int maxNetherWart = 3;
}
