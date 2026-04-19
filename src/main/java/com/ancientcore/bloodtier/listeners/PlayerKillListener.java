package com.ancientcore.bloodtier.listeners;

import com.ancientcore.bloodtier.BloodTierPlugin;
import com.ancientcore.bloodtier.managers.AntiFarmManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import com.ancientcore.bloodtier.managers.BloodManager;
import com.ancientcore.bloodtier.managers.TierManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerKillListener implements Listener {
    private final BloodManager bloodManager;
    private final TierManager tierManager;

    private final BloodTierPlugin plugin;
    private final AntiFarmManager antiFarmManager;

    public PlayerKillListener(BloodManager bloodManager, TierManager tierManager, AntiFarmManager antiFarmManager, BloodTierPlugin plugin) {
        this.bloodManager = bloodManager;
        this.tierManager = tierManager;
        this.antiFarmManager = antiFarmManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;

        Player killer = victim.getKiller();
        if (killer == null) return;

        // Get values from config
        int bp = plugin.getConfig().getInt("blood-system.kill-points");
        int deathLoss = plugin.getConfig().getInt("blood-system.death-loss");

        boolean antiFarmEnabled = plugin.getConfig().getBoolean("anti-farm.enabled");
        int cooldown = plugin.getConfig().getInt("anti-farm.cooldown-seconds");

        if (antiFarmEnabled && !antiFarmManager.canKill(killer, victim, cooldown)) {
            killer.sendMessage("§cYou cannot farm this player yet!");
            return;
        }

        // Add BP to killer
        bloodManager.addBP(killer, bp);

        int newBP = bloodManager.getBP(killer);

        // Check tier upgrade
        tierManager.checkTier(killer, newBP);

        killer.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent("§c+" + bp + " BP ⚔")
        );

        // Remove BP from victim
        bloodManager.removeBP(victim, deathLoss);

        victim.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent("§4-" + deathLoss + " BP 💀")
        );
    }


}