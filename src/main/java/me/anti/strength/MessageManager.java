package me.anti.strength;

import org.bukkit.ChatColor;

public class MessageManager {

    public static String formatWeapon(String weapon) {

        switch (weapon.toUpperCase()) {

            case "SWORD":
                return ChatColor.RED + "⚔ Sword";

            case "AXE":
                return ChatColor.GOLD + "🪓 Axe";

            case "BOW":
                return ChatColor.GREEN + "🏹 Bow";

            case "TRIDENT":
                return ChatColor.AQUA + "🔱 Trident";

            case "CROSSBOW":
                return ChatColor.BLUE + "➹ Crossbow";

            case "SHIELD":
                return ChatColor.GRAY + "🛡 Shield";
        }

        return ChatColor.WHITE + weapon;
    }
}
