package me.anti.strength;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AbilityManager {

    // ================= SWORD =================
    public static void useSwordAbility(Player player) {

        player.getWorld().spawnParticle(
                Particle.SWEEP_ATTACK,
                player.getLocation(),
                20
        );

        player.playSound(
                player.getLocation(),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP,
                1,
                1
        );
    }

    // ================= AXE =================
    public static void useAxeAbility(Player player) {

        player.getWorld().spawnParticle(
                Particle.EXPLOSION,
                player.getLocation(),
                5
        );

        player.playSound(
                player.getLocation(),
                Sound.ENTITY_GENERIC_EXPLODE,
                1,
                1
        );
    }

    // ================= BOW =================
    public static void useBowAbility(Player player) {

        player.getWorld().spawnParticle(
                Particle.END_ROD,
                player.getEyeLocation(),
                30
        );

        player.playSound(
                player.getLocation(),
                Sound.ENTITY_ENDER_DRAGON_SHOOT,
                1,
                1
        );
    }

    // ================= TRIDENT =================
    public static void useTridentAbility(Player player) {

        Vector vec =
                player.getLocation()
                        .getDirection()
                        .multiply(2);

        player.setVelocity(vec);

        player.getWorld().spawnParticle(
                Particle.WATER_SPLASH,
                player.getLocation(),
                50
        );

        player.playSound(
                player.getLocation(),
                Sound.ITEM_TRIDENT_RIPTIDE_3,
                1,
                1
        );
    }

    // ================= CROSSBOW =================
    public static void useCrossbowAbility(Player player) {

        player.getWorld().spawnParticle(
                Particle.PORTAL,
                player.getLocation(),
                50
        );

        player.playSound(
                player.getLocation(),
                Sound.ITEM_CROSSBOW_SHOOT,
                1,
                1
        );
    }

    // ================= SHIELD =================
    public static void useShieldAbility(Player player) {

        player.getWorld().spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                player.getLocation(),
                20
        );

        player.playSound(
                player.getLocation(),
                Sound.ITEM_TOTEM_USE,
                1,
                1
        );
    }
}
