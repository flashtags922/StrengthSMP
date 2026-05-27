package me.anti.strength;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class StrengthListener implements Listener {

    private final StrengthSMP plugin;

    public StrengthListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }

    // ================= HIT DETECTION =================
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

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

        // ================= +3 PASSIVES =================
        if (strength >= 3) {

            // ================= SWORD =================
            if (weapon.equalsIgnoreCase("SWORD")) {

                int combo =
                        ComboManager.combo
                                .getOrDefault(id, 0);

                if (combo % 3 == 0) {

                    e.setDamage(
                            e.getDamage() * 1.5
                    );

                    attacker.getWorld()
                            .spawnParticle(
                                    Particle.CRIT,
                                    victim.getLocation(),
                                    15
                            );
                }
            }

            // ================= AXE =================
            if (weapon.equalsIgnoreCase("AXE")) {

                boolean crit =
                        attacker.getFallDistance() > 0
                                && !attacker.isOnGround();

                if (crit) {

                    victim.getWorld()
                            .spawnParticle(
                                    Particle.CRIT,
                                    victim.getLocation(),
                                    20
                            );
                }
            }

            // ================= BOW =================
            if (weapon.equalsIgnoreCase("BOW")) {

                int combo =
                        ComboManager.combo
                                .getOrDefault(id, 0);

                if (combo % 3 == 0) {

                    e.setDamage(
                            e.getDamage() * 2
                    );
                }
            }

            // ================= TRIDENT =================
            if (weapon.equalsIgnoreCase("TRIDENT")) {

                victim.getWorld()
                        .strikeLightningEffect(
                                victim.getLocation()
                        );
            }

            // ================= CROSSBOW =================
            if (weapon.equalsIgnoreCase("CROSSBOW")) {

                int combo =
                        ComboManager.combo
                                .getOrDefault(id, 0);

                if (combo % 5 == 0) {

                    victim.teleport(
                            attacker.getLocation()
                    );
                }
            }
        }
    }
}
