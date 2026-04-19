package com.ancientcore.bloodtier.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AntiFarmManager {

    private final Map<String, Long> lastKillMap = new HashMap<>();

    public boolean canKill(Player killer, Player victim, int cooldownSeconds) {

        String key = killer.getUniqueId() + ":" + victim.getUniqueId();

        long currentTime = System.currentTimeMillis();

        if (!lastKillMap.containsKey(key)) {
            lastKillMap.put(key, currentTime);
            return true;
        }

        long lastTime = lastKillMap.get(key);

        if ((currentTime - lastTime) < cooldownSeconds * 1000L) {
            return false;
        }

        lastKillMap.put(key, currentTime);
        return true;
    }
}