package me.anti.strength;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class UltimateListener implements Listener {

    private final StrengthSMP plugin;

    public UltimateListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }
    
Action action = e.getAction();

if (action.name().contains("LEFT")) {
    return;
}
    
    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        UUID id = player.getUniqueId();

        // MUST BE READY
        if (!ComboManager.ready.getOrDefault(id, false)) {
            return;
        }

        String weapon =
                plugin.weapon.getOrDefault(
                        id,
                        "NONE"
                );

        Material hand =
                player.getInventory()
                        .getItemInMainHand()
                        .getType();

        // ================= SWORD =================
        if (weapon.equalsIgnoreCase("SWORD")) {

            if (!hand.name().contains("SWORD")) {
                return;
            }

            AbilityManager.useSwordAbility(player);
        }

        // ================= AXE =================
        if (weapon.equalsIgnoreCase("AXE")) {

            if (!hand.name().contains("AXE")) {
                return;
            }

            AbilityManager.useAxeAbility(player);
        }

        // ================= BOW =================
        if (weapon.equalsIgnoreCase("BOW")) {

            if (hand != Material.BOW) {
                return;
            }

            AbilityManager.useBowAbility(player);
        }

        // ================= TRIDENT =================
        if (weapon.equalsIgnoreCase("TRIDENT")) {

            if (hand != Material.TRIDENT) {
                return;
            }

            AbilityManager.useTridentAbility(player);
        }

        // ================= CROSSBOW =================
        if (weapon.equalsIgnoreCase("CROSSBOW")) {

            if (hand != Material.CROSSBOW) {
                return;
            }

            AbilityManager.useCrossbowAbility(player);
        }

        // ================= SHIELD =================
        if (weapon.equalsIgnoreCase("SHIELD")) {

            if (hand != Material.SHIELD) {
                return;
            }

            AbilityManager.useShieldAbility(player);
        }

        // RESET AFTER USE
        ComboManager.resetCombo(player);
    }
}
