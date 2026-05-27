package me.anti.strength;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ComboManager {

    // PLAYER COMBOS
    public static final Map<UUID, Integer> combo =
            new HashMap<>();

    // ULT READY
    public static final Map<UUID, Boolean> ready =
            new HashMap<>();

    // ================= ADD HIT =================
    public static void addHit(
            StrengthSMP plugin,
            Player player
    ) {

        UUID id = player.getUniqueId();

        int hits =
                combo.getOrDefault(id, 0);

        hits++;

        combo.put(id, hits);

        String weapon =
                plugin.weapon.getOrDefault(
                        id,
                        "NONE"
                );

        int strength =
                plugin.strength.getOrDefault(
                        id,
                        0
                );

        // MUST HAVE +5 STRENGTH
        if (strength < 5) {
            return;
        }

        // ALREADY READY
        if (ready.getOrDefault(id, false)) {
            return;
        }

        int needed = getRequiredHits(weapon);

        if (hits >= needed) {

            ready.put(id, true);

            player.sendMessage(
                    ChatColor.GREEN +
                            getWeaponSymbol(weapon) +
                            " Ready!"
            );
        }
    }

    // ================= RESET =================
    public static void resetCombo(Player player) {

        UUID id = player.getUniqueId();

        combo.put(id, 0);

        ready.put(id, false);
    }

    // ================= REQUIRED HITS =================
    private static int getRequiredHits(
            String weapon
    ) {

        switch (weapon.toUpperCase()) {

            case "SWORD":
                return 5;

            case "AXE":
                return 7;

            case "TRIDENT":
                return 3;

            case "BOW":
                return 10;

            case "CROSSBOW":
                return 5;

            case "SHIELD":
                return 1;
        }

        return 5;
    }

    // ================= SYMBOLS =================
    private static String getWeaponSymbol(
            String weapon
    ) {

        switch (weapon.toUpperCase()) {

            case "SWORD":
                return "⚔";

            case "AXE":
                return "🪓";

            case "BOW":
                return "🏹";

            case "TRIDENT":
                return "🔱";

            case "CROSSBOW":
                return "➹";

            case "SHIELD":
                return "🛡";
        }

        return "⚔";
    }
}
