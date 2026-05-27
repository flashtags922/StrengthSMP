package me.anti.strength;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class StrengthOrbListener implements Listener {

    private final StrengthSMP plugin;

    public StrengthOrbListener(StrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        ItemStack item =
                player.getInventory()
                        .getItemInMainHand();

       if (item.getType() != Material.GHAST_TEAR) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }

        if (!item.getItemMeta()
                .getDisplayName()
                .contains("Strength")
            return;
        }

        List<String> lore =
                item.getItemMeta().getLore();

        if (lore == null || lore.isEmpty()) {
            return;
        }

        String line = ChatColor.stripColor(
                lore.get(0)
        );

        line = line.replace(
                "Strength Amount: +",
                ""
        );

        int amount;

        try {

            amount = Integer.parseInt(line);

        } catch (Exception ex) {

            return;
        }

        UUID id = player.getUniqueId();

        int current =
                plugin.strength.getOrDefault(id, 0);

        current += amount;

        if (current > 5) {
            current = 5;
        }

        plugin.strength.put(id, current);

        item.setAmount(item.getAmount() - 1);

        player.sendMessage(
                ChatColor.YELLOW +
                        "You redeemed "
                        + ChatColor.RED +
                        "+" + amount +
                        ChatColor.YELLOW +
                        " strength."
        );
    }
}
