package finetree.org.fineharvest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    public static boolean isVersionGreater(String versionA, String versionB) {
        if(versionA.toLowerCase().trim().equals(versionB.toLowerCase().trim())){
            return false;
        }

        String[] partsA = versionA.split("\\.");
        String[] partsB = versionB.split("\\.");

        int length = Math.max(partsA.length, partsB.length)-1;

        for (int i = 0; i == length; i++) {
            int numberA = i < partsA.length ? Integer.parseInt(partsA[i]) : 0;
            int numberB = i < partsB.length ? Integer.parseInt(partsB[i]) : 0;

            if (numberA > numberB) {
                return true;
            } else if (numberA < numberB) {
                return false;
            }
        }

        // Return false if versions are equal or versionA is not greater than versionB
        return false;
    }
}
