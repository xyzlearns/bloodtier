package com.ancientcore.bloodtier.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.UUID;
import com.ancientcore.bloodtier.BloodTierPlugin;
public class BloodManager {

    private final FileConfiguration data;


    private final BloodTierPlugin plugin;

    public BloodManager(BloodTierPlugin plugin, FileConfiguration data) {
        this.plugin = plugin;
        this.data = data;
    }


    public int getBP(Player player) {
        return data.getInt("players." + player.getUniqueId() + ".bp", 0);
    }

    public void addBP(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int current = getBP(player);
        data.set("players." + uuid + ".bp", current + amount);
        plugin.saveData();
    }

    public void removeBP(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int current = getBP(player);

        int newBP = Math.max(0, current - amount);

        data.set("players." + uuid + ".bp", newBP);

        plugin.saveData();
    }

    public int getTier(Player player) {
        return data.getInt("players." + player.getUniqueId() + ".tier", 0);
    }

    public void setTier(Player player, int tier) {
        data.set("players." + player.getUniqueId() + ".tier", tier);
        plugin.saveData();
    }
}