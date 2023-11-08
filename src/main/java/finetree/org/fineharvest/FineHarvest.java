package finetree.org.fineharvest;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.config.ConfigManager;

public final class FineHarvest extends JavaPlugin {

    private static FineHarvest plugin;
    private static boolean isMCMMO = false;

    private static String tag = "[" + ChatColor.GOLD + "Fine" + ChatColor.DARK_GREEN + "Harvest" + ChatColor.RESET + "]";

    @Override
    public void onEnable() {
        //Initialize plugin getter
        plugin = this;

        //Check for mcMMO
        if (getServer().getPluginManager().getPlugin("mcMMO") != null) {
            isMCMMO = true;
            getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + " mcMMO support enabled");
        }

        //Initialize Config
        ConfigManager config = ConfigManager.create(this);
        config.target(Config.class).saveDefaults().load();

        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(tag + ChatColor.GREEN + " enabled");

        //event listener
        getServer().getPluginManager().registerEvents(new Events(), this);

        //bStats init
        Metrics metrics = new Metrics(this, 20222);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(tag + ChatColor.RED + " disabled");
    }

    public static FineHarvest getPlugin() {
        return plugin;
    }

    public static boolean isMCMMO() {
        return isMCMMO;
    }
}
