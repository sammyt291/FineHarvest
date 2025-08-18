package org.finetree.fineharvest;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.config.ConfigManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.finetree.fineharvest.Config.*;
import static org.finetree.fineharvest.UpdateChecker.isVersionGreater;

public final class FineHarvest extends JavaPlugin {

    private static FineHarvest plugin;
    public static YamlConfiguration AuraSources;

    public static String tag = "[" + ChatColor.GOLD + "Fine" + ChatColor.DARK_GREEN + "Harvest" + ChatColor.RESET + "] ";

    // Lookup table for skill plugins
    public static Map<String, Boolean> skillMods = new HashMap<>() {{
        put("mcMMO", false);
        put("AuraSkills", false);
    }};

    @Override
    public void onEnable() {
        //Initialize plugin getter
        plugin = this;

        //Initialize Config
        ConfigManager config = ConfigManager.create(this);
        config.target(Config.class).saveDefaults().load();

        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "enabled");

        //event listener
        getServer().getPluginManager().registerEvents(new Events(), this);

        //Check for mcMMO
        if (BuildCheck.hasPlugin("mcMMO")) {
            skillMods.put("mcMMO", true);
            if(mcMMOSupport) {
                getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "mcMMO support enabled");
            }else{
                getServer().getConsoleSender().sendMessage(tag + "Ignoring mcMMO as it was disabled by config!");
            }
        }
        //Check for AuraSkills
        if (BuildCheck.hasPlugin("AuraSkills")) {
            skillMods.put("AuraSkills", true);
            if(aureliumSkillsSupport) {
                getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "AuraSkills support enabled");
            }else{
                getServer().getConsoleSender().sendMessage(tag + "Ignoring AuraSkills as it was disabled by config!");
            }

            File file = new File("plugins/AuraSkills/sources/farming.yml");
            AuraSources = YamlConfiguration.loadConfiguration(file);
        }

        //bStats init
        Metrics metrics = new Metrics(this, 20222);

        //Update checker
        new UpdateChecker(this, 113415).getVersion(version -> {
            if (isVersionGreater(version, this.getDescription().getVersion())) {
                getServer().getConsoleSender().sendMessage(tag + ChatColor.RED + "There is a new update available ("+version+"): " + ChatColor.GOLD + "https://www.spigotmc.org/resources/fineharvest.113415/");
            } else {
                getServer().getConsoleSender().sendMessage(tag + "Up to date!");
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(tag + ChatColor.RED + "disabled");
    }

    public static boolean noProtWarn = false;
    public static void warnNoProtection() {
        if(!noProtWarn){
            noProtWarn = true;
            plugin.getServer().getConsoleSender().sendMessage( tag + ChatColor.BLUE + " faking block breaks to check build rights" );
            plugin.getServer().getConsoleSender().sendMessage( ChatColor.BLUE + "please install a land protection plugin!" );
        }
    }

    public static FineHarvest getPlugin() {
        return plugin;
    }
    public static YamlConfiguration getAuraSources() {
        return AuraSources;
    }


}
