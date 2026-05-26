package me.anti.strength;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static final Map<UUID, Long> swordCooldown =
            new HashMap<>();

    private static final Map<UUID, Long> axeCooldown =
            new HashMap<>();

    private static final Map<UUID, Long> bowCooldown =
            new HashMap<>();

    private static final Map<UUID, Long> tridentCooldown =
            new HashMap<>();

    private static final Map<UUID, Long> crossbowCooldown =
            new HashMap<>();

    private static final Map<UUID, Long> shieldCooldown =
            new HashMap<>();

    // ================= SET =================
    public static void setCooldown(
            UUID uuid,
            String weapon,
            int seconds
    ) {

        long time =
                System.currentTimeMillis()
                        + (seconds * 1000L);

        switch (weapon.toUpperCase()) {

            case "SWORD":
                swordCooldown.put(uuid, time);
                break;

            case "AXE":
                axeCooldown.put(uuid, time);
                break;

            case "BOW":
                bowCooldown.put(uuid, time);
                break;

            case "TRIDENT":
                tridentCooldown.put(uuid, time);
                break;

            case "CROSSBOW":
                crossbowCooldown.put(uuid, time);
                break;

            case "SHIELD":
                shieldCooldown.put(uuid, time);
                break;
        }
    }

    // ================= CHECK =================
    public static boolean onCooldown(
            UUID uuid,
            String weapon
    ) {

        long current =
                System.currentTimeMillis();

        switch (weapon.toUpperCase()) {

            case "SWORD":
                return swordCooldown.containsKey(uuid)
                        && swordCooldown.get(uuid) > current;

            case "AXE":
                return axeCooldown.containsKey(uuid)
                        && axeCooldown.get(uuid) > current;

            case "BOW":
                return bowCooldown.containsKey(uuid)
                        && bowCooldown.get(uuid) > current;

            case "TRIDENT":
                return tridentCooldown.containsKey(uuid)
                        && tridentCooldown.get(uuid) > current;

            case "CROSSBOW":
                return crossbowCooldown.containsKey(uuid)
                        && crossbowCooldown.get(uuid) > current;

            case "SHIELD":
                return shieldCooldown.containsKey(uuid)
                        && shieldCooldown.get(uuid) > current;
        }

        return false;
    }

    // ================= REMAINING =================
    public static long getRemaining(
            UUID uuid,
            String weapon
    ) {

        long current =
                System.currentTimeMillis();

        long end = 0;

        switch (weapon.toUpperCase()) {

            case "SWORD":
                end = swordCooldown.getOrDefault(uuid, 0L);
                break;

            case "AXE":
                end = axeCooldown.getOrDefault(uuid, 0L);
                break;

            case "BOW":
                end = bowCooldown.getOrDefault(uuid, 0L);
                break;

            case "TRIDENT":
                end = tridentCooldown.getOrDefault(uuid, 0L);
                break;

            case "CROSSBOW":
                end = crossbowCooldown.getOrDefault(uuid, 0L);
                break;

            case "SHIELD":
                end = shieldCooldown.getOrDefault(uuid, 0L);
                break;
        }

        return Math.max(
                0,
                (end - current) / 1000
        );
    }
}
