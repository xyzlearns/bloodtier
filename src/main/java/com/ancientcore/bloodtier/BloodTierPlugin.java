package com.ancientcore.bloodtier;

import com.ancientcore.bloodtier.managers.AntiFarmManager;
import com.ancientcore.bloodtier.managers.BloodManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ancientcore.bloodtier.listeners.PlayerKillListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ancientcore.bloodtier.listeners.PlayerKillListener;
import com.ancientcore.bloodtier.managers.TierManager;


public final class BloodTierPlugin extends JavaPlugin implements TabCompleter {
    private File dataFile;
    private FileConfiguration dataConfig;
    private BloodManager bloodManager;
    private TierManager tierManager;
    private AntiFarmManager antiFarmManager;


    @Override
    public void onEnable() {
        getLogger().info("BloodTier Plugin Enabled!");
        getCommand("bloodtier").setTabCompleter(this);
        saveDefaultConfig();

        // Create data.yml
        dataFile = new File(getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        // Initialize BloodManager
        bloodManager = new BloodManager(this, dataConfig);
        tierManager = new TierManager(this, bloodManager);
        antiFarmManager = new AntiFarmManager();

        getServer().getPluginManager().registerEvents(
                new PlayerKillListener(bloodManager, tierManager,antiFarmManager,  this),
                this
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("BloodTier Plugin Disabled!");
        saveData();
    }

    public void saveData() {
        if (dataConfig == null || dataFile == null) return;

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("bloodtier")) return false;

        if (!(sender instanceof Player)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage("BloodTier config reloaded!");
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }

        Player player = (Player) sender;

        // 🔒 Permission check
        if (!player.hasPermission("bloodtier.use")) {
            player.sendMessage("§cYou don't have permission.");
            return true;
        }

        // Reload command
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {

            if (!player.hasPermission("bloodtier.admin")) {
                player.sendMessage("§cNo permission.");
                return true;
            }

            reloadConfig();
            player.sendMessage("§aBloodTier config reloaded!");
            return true;
        }

        // Show player stats
        int bp = bloodManager.getBP(player);
        int tier = bloodManager.getTier(player);
        int nextReq = tierManager.getNextTierRequirement(tier);

        player.sendMessage("§c🩸 Blood Tier: §f" + tier);

        if (nextReq == -1) {
            player.sendMessage("§6You reached MAX tier!");
        } else {
            player.sendMessage("§c📊 BP: §f" + bp + " / " + nextReq);
            player.sendMessage("§c⚡ Next Tier: §f" + (tier + 1));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!command.getName().equalsIgnoreCase("bloodtier")) return null;

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();

            if (sender.hasPermission("bloodtier.admin")) {
                suggestions.add("reload");
            }

            return suggestions;
        }

        return Collections.emptyList();
    }
}