package com.ancientcore.bloodtier.managers;

import com.ancientcore.bloodtier.BloodTierPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TierManager {

    private final BloodTierPlugin plugin;
    private final BloodManager bloodManager;

    public TierManager(BloodTierPlugin plugin, BloodManager bloodManager) {
        this.plugin = plugin;
        this.bloodManager = bloodManager;
    }

    public void checkTier(Player player, int bp) {

        int currentTier = bloodManager.getTier(player);

        for (String key : plugin.getConfig().getConfigurationSection("tiers").getKeys(false)) {

            int tier = Integer.parseInt(key);
            int required = plugin.getConfig().getInt("tiers." + key + ".required-bp");

            if (bp >= required && tier > currentTier) {

                // Update tier
                bloodManager.setTier(player, tier);

                // Run commands
                List<String> commands = plugin.getConfig().getStringList("tiers." + key + ".commands");

                for (String cmd : commands) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            cmd.replace("%player%", player.getName())
                    );
                }

                player.sendMessage("§6🔥 Tier " + tier + " unlocked!");

            }
        }
    }

    public int getNextTierRequirement(int currentTier) {

        int nextTier = currentTier + 1;

        if (!plugin.getConfig().contains("tiers." + nextTier)) {
            return -1; // max tier reached
        }

        return plugin.getConfig().getInt("tiers." + nextTier + ".required-bp");
    }
}