package org.artaphy.artaphyCore.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private static CooldownManager instance;
    private final Map<String, Map<UUID, Long>> cooldowns;

    private CooldownManager() {
        cooldowns = new HashMap<>();
    }

    public static CooldownManager getInstance() {
        if (instance == null) {
            instance = new CooldownManager();
        }
        return instance;
    }

    public boolean isOnCooldown(String command, UUID uuid, int cooldownTime) {
        if (!cooldowns.containsKey(command)) {
            cooldowns.put(command, new HashMap<>());
        }

        Map<UUID, Long> commandCooldowns = cooldowns.get(command);

        if (!commandCooldowns.containsKey(uuid)) return false;

        long timePassed = (System.currentTimeMillis() - commandCooldowns.get(uuid)) / 1000;
        return timePassed < cooldownTime;
    }

    public long getRemainingCooldown(String command, UUID uuid, int cooldownTime) {
        if (!cooldowns.containsKey(command)) return 0;

        Map<UUID, Long> commandCooldowns = cooldowns.get(command);
        long timePassed = (System.currentTimeMillis() - commandCooldowns.get(uuid)) / 1000;
        return cooldownTime - timePassed;
    }

    public void setCooldown(String command, UUID uuid) {
        if (!cooldowns.containsKey(command)) {
            cooldowns.put(command, new HashMap<>());
        }
        cooldowns.get(command).put(uuid, System.currentTimeMillis());
    }
}
