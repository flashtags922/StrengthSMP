package me.anti.strength;

import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.HashMap;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;
import java.util.UUID;

public class StrengthListener implements Listener {

    private final StrengthSMP plugin;

    private final Random random = new Random();

    public static final java.util.Map<UUID, Boolean> axeUltimate =
        new java.util.HashMap<>();

    public StrengthListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }

    // ================= HIT DETECTION =================
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        // ================= PLAYER MELEE =================
        if (e.getDamager() instanceof Player
                && e.getEntity() instanceof Player) {

            Player attacker =
                    (Player) e.getDamager();

            Player victim =
                    (Player) e.getEntity();

            UUID id =
                    attacker.getUniqueId();

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

            // ================= COMBO =================
            ComboManager.addHit(
                    plugin,
                    attacker
            );

            int combo =
                    ComboManager.combo
                            .getOrDefault(id, 0);

            // ================= NEED +3 =================
            if (strength < 3) {
                return;
            }

            // =========================================================
            // SWORD
            // =========================================================
            if (weapon.equalsIgnoreCase("SWORD")) {

                // EVERY 3 HITS = CRIT
                if (combo % 3 == 0) {

                    e.setDamage(
                            e.getDamage() * 1.5
                    );

                    victim.getWorld().spawnParticle(
                            Particle.CRIT,
                            victim.getLocation(),
                            20
                    );
                }

                // ULTIMATE READY
                if (strength >= 5) {

                    if (combo == 5) {

                        attacker.sendMessage(
                                "§c⚔ §6Ready!"
                        );
                    }
                }
            }

            // =========================================================
            // AXE
            // =========================================================
            if (weapon.equalsIgnoreCase("AXE")) {

                boolean crit =
                        attacker.getFallDistance() > 0
                                && !attacker.isOnGround();

                if (crit) {

                    victim.getWorld().spawnParticle(
                            Particle.CRIT,
                            victim.getLocation(),
                            20
                    );
                }

                // ULTIMATE READY
                if (strength >= 5) {

                    if (combo == 5) {

                        attacker.sendMessage(
                                "§4🪓 §6Ready!"
                        );
                    }
                }
            }

            // =========================================================
            // TRIDENT
            // =========================================================
            if (weapon.equalsIgnoreCase("TRIDENT")) {

                victim.getWorld().strikeLightningEffect(
                        victim.getLocation()
                );

                // ULTIMATE READY
                if (strength >= 5) {

                    if (combo == 5) {

                        attacker.sendMessage(
                                "§b🔱 §6Ready!"
                        );
                    }
                }
            }

            // =========================================================
            // SHIELD
            // =========================================================
            if (weapon.equalsIgnoreCase("SHIELD")) {

                // ULTIMATE READY
                if (strength >= 5) {

                    if (combo == 5) {

                        attacker.sendMessage(
                                "§a🛡 §6Ready!"
                        );
                    }
                }
            }
        }

        // =========================================================
        // BOW + CROSSBOW PROJECTILES
        // =========================================================
        if (e.getDamager() instanceof Arrow
                && e.getEntity() instanceof Player) {

            Arrow arrow =
                    (Arrow) e.getDamager();

            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }

            Player attacker =
                    (Player) arrow.getShooter();

            Player victim =
                    (Player) e.getEntity();

            UUID id =
                    attacker.getUniqueId();

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

            ComboManager.addHit(
                    plugin,
                    attacker
            );

            int combo =
                    ComboManager.combo
                            .getOrDefault(id, 0);

            // NEED +3
            if (strength < 3) {
                return;
            }

            // =========================================================
            // BOW
            // =========================================================
            if (weapon.equalsIgnoreCase("BOW")) {

                // EVERY 3RD SHOT DOUBLE DAMAGE
                if (combo % 3 == 0) {

                    e.setDamage(
                            e.getDamage() * 2
                    );

                    victim.getWorld().spawnParticle(
                            Particle.CRIT,
                            victim.getLocation(),
                            20
                    );
                }

                // EVERY 10TH SHOT TRIPLE DAMAGE
                if (strength >= 5) {

                    if (combo % 10 == 0) {

                        e.setDamage(
                                e.getDamage() * 3
                        );

                        attacker.sendMessage(
                                "§2🏹 §6Ready!"
                        );
                    }
                }
            }

            // =========================================================
            // CROSSBOW
            // =========================================================
            if (weapon.equalsIgnoreCase("CROSSBOW")) {

                // 33% PULL CHANCE
                if (random.nextInt(100) < 33) {

                    victim.teleport(
                            attacker.getLocation()
                    );
                }

                // 10TH SHOT = 4X DAMAGE
                if (strength >= 5) {

                    if (combo % 10 == 0) {

                        e.setDamage(
                                e.getDamage() * 4
                        );

                        attacker.sendMessage(
                                "§9➹ §6Ready!"
                        );
                    }
                }
            }
        }

        // =========================================================
        // TRIDENT THROW
        // =========================================================
        if (e.getDamager() instanceof Trident
                && e.getEntity() instanceof Player) {

            Trident trident =
                    (Trident) e.getDamager();

            if (!(trident.getShooter() instanceof Player)) {
                return;
            }

            Player attacker =
                    (Player) trident.getShooter();

            Player victim =
                    (Player) e.getEntity();

            UUID id =
                    attacker.getUniqueId();

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

            if (!weapon.equalsIgnoreCase("TRIDENT")) {
                return;
            }

            if (strength < 3) {
                return;
            }

            victim.getWorld().strikeLightningEffect(
                    victim.getLocation()
            );
        }
    }
}
