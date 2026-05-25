package me.anti.strength;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrengthListener implements Listener {

    private final StrengthSMP plugin;

    public StrengthListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Integer> comboMap = new HashMap<>();
    private final Map<UUID, Integer> axeCrits = new HashMap<>();
    private final Map<UUID, Boolean> axeUltimate = new HashMap<>();

    // ================= HIT DETECTION =================
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        UUID id = attacker.getUniqueId();

        String weapon = plugin.weapon.get(id);

        if (weapon == null) return;

        int combo = comboMap.getOrDefault(id, 0) + 1;
        comboMap.put(id, combo);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            comboMap.put(id, 0);
        }, 40L);

        // ================= SWORD =================
        if (weapon.equals("SWORD")) {

            // passive every 3 hits crit
            if (plugin.strength.get(id) >= 3) {

                if (combo % 3 == 0) {
                    e.setDamage(e.getDamage() * 1.5);

                    attacker.sendMessage(ChatColor.RED + "⚔ CRITICAL HIT");
                }
            }

            // ultimate ready
            if (plugin.strength.get(id) >= 5) {

                if (combo % 5 == 0) {
                    attacker.sendMessage(ChatColor.RED + "⚔ Ready");
                }
            }
        }

        // ================= AXE =================
        if (weapon.equals("AXE")) {

            boolean crit = attacker.getFallDistance() > 0 && !attacker.isOnGround();

            if (crit) {

                int crits = axeCrits.getOrDefault(id, 0) + 1;
                axeCrits.put(id, crits);

                attacker.sendMessage(ChatColor.GOLD + "🪓 Crit " + crits + "/5");

                // stun after 5 crits
                if (crits >= 5) {

                    axeCrits.put(id, 0);

                    stunPlayer(victim);

                    attacker.sendMessage(ChatColor.DARK_RED + "🪓 Ready");
                }
            }

            // axe ultimate
            if (axeUltimate.getOrDefault(id, false)) {

                e.setDamage(10);

                axeUltimate.put(id, false);

                attacker.sendMessage(ChatColor.RED + "BERSERKER HIT!");
            }
        }

        // ================= BOW =================
        if (weapon.equals("BOW")) {

            if (combo % 3 == 0) {

                e.setDamage(e.getDamage() * 2);

                attacker.sendMessage(ChatColor.GREEN + "🏹 Double Damage");
            }

            if (combo % 10 == 0) {

                attacker.sendMessage(ChatColor.GREEN + "🏹 Ready");
            }
        }

        // ================= TRIDENT =================
        if (weapon.equals("TRIDENT")) {

            victim.getWorld().strikeLightningEffect(victim.getLocation());

            attacker.sendMessage(ChatColor.AQUA + "🔱 Lightning Strike");

            if (combo % 5 == 0) {

                attacker.sendMessage(ChatColor.AQUA + "🔱 Ready");
            }
        }

        // ================= CROSSBOW =================
        if (weapon.equals("CROSSBOW")) {

            if (combo % 5 == 0) {

                victim.teleport(attacker.getLocation());

                attacker.sendMessage(ChatColor.BLUE + "➹ Pulled Player");
            }

            if (combo % 10 == 0) {

                e.setDamage(e.getDamage() * 4);

                attacker.sendMessage(ChatColor.BLUE + "➹ Ready");
            }
        }

        // ================= SHIELD =================
        if (weapon.equals("SHIELD")) {

            attacker.addPotionEffect(new PotionEffect(
                    PotionEffectType.RESISTANCE,
                    100,
                    0
            ));

            if (combo % 5 == 0) {

                attacker.sendMessage(ChatColor.GRAY + "🛡 Ready");
            }
        }
    }

    // ================= STUN =================
    private void stunPlayer(Player p) {

        new BukkitRunnable() {

            int ticks = 0;

            @Override
            public void run() {

                if (ticks >= 20) {
                    cancel();
                    return;
                }

                ticks++;

                p.setVelocity(new org.bukkit.util.Vector(0, 0, 0));

                p.addPotionEffect(new PotionEffect(
                        PotionEffectType.SLOWNESS,
                        5,
                        255,
                        false,
                        false
                ));

                p.addPotionEffect(new PotionEffect(
                        PotionEffectType.NAUSEA,
                        5,
                        1,
                        false,
                        false
                ));

                Location loc = p.getLocation();

                loc.setYaw(loc.getYaw() + 10);

                p.teleport(loc);
            }

        }.runTaskTimer(plugin, 0L, 1L);
    }
}
