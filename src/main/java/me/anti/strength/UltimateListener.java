package me.anti.strength;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class UltimateListener implements Listener {

    private final StrengthSMP plugin;

    public UltimateListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Action action = e.getAction();

        // ONLY RIGHT CLICK
        if (action != Action.RIGHT_CLICK_AIR &&
                action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = e.getPlayer();

        UUID id = player.getUniqueId();

        // MUST BE READY
        if (!ComboManager.ready.getOrDefault(id, false)) {
            return;
        }

        Material hand = player
                .getInventory()
                .getItemInMainHand()
                .getType();

        String weapon = plugin.weapon.getOrDefault(
                id,
                "NONE"
        );

        // ================= SWORD =================
        if (weapon.equalsIgnoreCase("SWORD")) {

            if (!hand.name().contains("SWORD")) {
                return;
            }

            AbilityManager.useSwordAbility(player);

            ComboManager.resetCombo(player);
            return;
        }

        // ================= AXE =================
        if (weapon.equalsIgnoreCase("AXE")) {

            if (!hand.name().contains("AXE")) {
                return;
            }

            AbilityManager.useAxeAbility(player);

            ComboManager.resetCombo(player);
            return;
        }

        // ================= BOW =================
        if (weapon.equalsIgnoreCase("BOW")) {

            if (hand != Material.BOW) {
                return;
            }

            AbilityManager.useBowAbility(player);

            ComboManager.resetCombo(player);
            return;
        }

        // ================= TRIDENT =================
        if (weapon.equalsIgnoreCase("TRIDENT")) {

            if (hand != Material.TRIDENT) {
                return;
            }

            AbilityManager.useTridentAbility(player);

            ComboManager.resetCombo(player);
            return;
        }

        // ================= CROSSBOW =================
        if (weapon.equalsIgnoreCase("CROSSBOW")) {

            if (hand != Material.CROSSBOW) {
                return;
            }

            AbilityManager.useCrossbowAbility(player);

            ComboManager.resetCombo(player);
            return;
        }

        // ================= SHIELD =================
        if (weapon.equalsIgnoreCase("SHIELD")) {

            if (hand != Material.SHIELD) {
                return;
            }

            AbilityManager.useShieldAbility(player);

            ComboManager.resetCombo(player);
        }
    }
}
