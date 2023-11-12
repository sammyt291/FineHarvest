package finetree.org.fineharvest;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.config.ConfigManager;

import java.io.File;

import static finetree.org.fineharvest.BuildCheck.hasPlugin;
import static finetree.org.fineharvest.UpdateChecker.isVersionGreater;

public final class FineHarvest extends JavaPlugin {

    private static FineHarvest plugin;
    public static YamlConfiguration AureliumSources;
    public static boolean noProtWarn = false;

    private static String tag = "[" + ChatColor.GOLD + "Fine" + ChatColor.DARK_GREEN + "Harvest" + ChatColor.RESET + "] ";

    @Override
    public void onEnable() {
        //Initialize plugin getter
        plugin = this;

        //Check for mcMMO
        if (hasPlugin("mcMMO")) {
            getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "mcMMO support enabled");
        }
        //Check for Aurelium
        if (hasPlugin("AureliumSkills")) {
            File file = new File("plugins/AureliumSkills/sources_config.yml");
            AureliumSources = YamlConfiguration.loadConfiguration(file);
            getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "AureliumSkills support enabled");
        }

        //Initialize Config
        ConfigManager config = ConfigManager.create(this);
        config.target(Config.class).saveDefaults().load();

        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + "enabled");

        //event listener
        getServer().getPluginManager().registerEvents(new Events(), this);

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

    public static void warnNoProtection() {
        if(!noProtWarn){
            noProtWarn = true;
            FineHarvest.getPlugin().getServer().getConsoleSender().sendMessage( tag + ChatColor.BLUE + " faking block breaks to check build rights" );
            FineHarvest.getPlugin().getServer().getConsoleSender().sendMessage( ChatColor.BLUE + "please install a land protection plugin!" );
        }
    }

    public static FineHarvest getPlugin() {
        return plugin;
    }
    public static YamlConfiguration getAureliumSources() {
        return AureliumSources;
    }
}
