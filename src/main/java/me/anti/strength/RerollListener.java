package me.anti.strength;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class RerollListener implements Listener {

    private final StrengthSMP plugin;

    private final Random random =
            new Random();

    private final String[] weapons = {
            "SWORD",
            "AXE",
            "BOW",
            "TRIDENT",
            "CROSSBOW",
            "SHIELD"
    };

    public RerollListener(
            StrengthSMP plugin
    ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(
            PlayerInteractEvent e
    ) {

        Player player =
                e.getPlayer();

        ItemStack item =
                player.getInventory()
                        .getItemInMainHand();

        if (item.getType() != Material.BOOK) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        if (!item.getItemMeta()
                .hasDisplayName()) {
            return;
        }

        if (!ChatColor.stripColor(
                item.getItemMeta()
                        .getDisplayName()
        ).equalsIgnoreCase(
                "Reroll Book"
        )) {
            return;
        }

        UUID id =
                player.getUniqueId();

        String newWeapon =
                weapons[random.nextInt(
                        weapons.length
                )];

        plugin.weapon.put(
                id,
                newWeapon
        );

        item.setAmount(
                item.getAmount() - 1
        );

        player.sendMessage(
                ChatColor.GOLD +
                        "𝚆𝚎𝚊𝚙𝚘𝚗: " +
                        MessageManager.formatWeapon(
                                newWeapon
                        )
        );
    }
}
