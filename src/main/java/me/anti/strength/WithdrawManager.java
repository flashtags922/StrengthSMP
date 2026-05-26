package me.anti.strength;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WithdrawManager {

    public static void giveStrengthOrb(Player player, int amount) {

        ItemStack orb = new ItemStack(Material.NETHER_STAR);

        ItemMeta meta = orb.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ChatColor.RED + "✦ Strength Orb"
            );

            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GRAY + "Strength Amount: "
                            + ChatColor.RED + "+" + amount
            ));

            meta.addEnchant(
                    Enchantment.UNBREAKING,
                    1,
                    true
            );

            meta.addItemFlags(
                    ItemFlag.HIDE_ENCHANTS
            );

            orb.setItemMeta(meta);
        }

        player.getInventory().addItem(orb);
    }
}
