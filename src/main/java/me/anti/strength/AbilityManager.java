package me.anti.strength;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class AbilityManager {

    // ================= USE ABILITY =================
    public static void useAbility(
            StrengthSMP plugin,
            Player player
    ) {

        UUID id = player.getUniqueId();

        // MUST HAVE +5
        if (plugin.strength.getOrDefault(id, 0) < 5) {
            return;
        }

        String weapon = plugin.weapon.get(id);

        if (weapon == null) {
            return;
        }

        switch (weapon.toUpperCase()) {

            case "SWORD":
                useSwordAbility(player);
                break;

            case "AXE":
                useAxeAbility(player);
                break;

            case "BOW":
                useBowAbility(player);
                break;

            case "TRIDENT":
                useTridentAbility(player);
                break;

            case "CROSSBOW":
                useCrossbowAbility(player);
                break;

            case "SHIELD":
                useShieldAbility(player);
                break;
        }
    }

    // ================= SWORD =================
    public static void useSwordAbility(
            Player player
    ) {

        ItemStack offhand =
                new ItemStack(
                        Material.DIAMOND_SWORD
                );

        player.getInventory()
                .setItemInOffHand(
                        offhand
                );

        player.sendMessage(
                ChatColor.RED +
                        "⚔ Sword Ability Activated!"
        );

        Bukkit.getScheduler()
                .runTaskLater(
                        StrengthSMP.getPlugin(
                                StrengthSMP.class
                        ),
                        () -> {

                            player.getInventory()
                                    .setItemInOffHand(
                                            null
                                    );

                        },
                        20L * 15
                );
    }

    // ================= AXE =================
    public static void useAxeAbility(
            Player player
    ) {

        player.sendMessage(
                ChatColor.DARK_RED +
                        "🪓 Axe Ability Activated!"
        );

        // SIMPLE SAFE EFFECT
        player.setVelocity(
                player.getLocation()
                        .getDirection()
                        .multiply(1.5)
        );
    }

    // ================= BOW =================
    public static void useBowAbility(
            Player player
    ) {

        player.sendMessage(
                ChatColor.GREEN +
                        "🏹 Bow Ability Activated!"
        );
    }

    // ================= TRIDENT =================
    public static void useTridentAbility(
            Player player
    ) {

        Vector dash =
                player.getLocation()
                        .getDirection()
                        .multiply(2);

        player.setVelocity(dash);

        player.sendMessage(
                ChatColor.AQUA +
                        "🔱 Trident Ability Activated!"
        );
    }

    // ================= CROSSBOW =================
    public static void useCrossbowAbility(
            Player player
    ) {

        player.sendMessage(
                ChatColor.BLUE +
                        "➹ Crossbow Ability Activated!"
        );
    }

    // ================= SHIELD =================
    public static void useShieldAbility(
            Player player
    ) {

        player.setHealth(
                Math.min(
                        player.getHealth() + 6,
                        player.getMaxHealth()
                )
        );

        player.sendMessage(
                ChatColor.GREEN +
                        "🛡 Shield Ability Activated!"
        );
    }
}
