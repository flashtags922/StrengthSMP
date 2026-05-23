package me.anti.strength;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemMeta;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class StrengthSMP extends JavaPlugin implements Listener {

    // ===== CLASSES =====
    private final String[] CLASSES = {
            "sword",
            "axe",
            "trident",
            "bow",
            "crossbow",
            "shield"
    };

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("StrengthSMP enabled");

        setupRerollRecipe();

        saveDefaultConfig();
    }

    // ===== PLAYER JOIN =====
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (getConfig().get(uuid + ".class") == null) {
            getConfig().set(uuid + ".class", "none");
            getConfig().set(uuid + ".strength", 0);
            saveConfig();
        }
    }

    // ===== REROLL BOOK INTERACTION =====
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.BOOK) return;

        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;

        if (!item.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Reroll Book"))
            return;

        event.setCancelled(true);

        Random random = new Random();
        String rolledClass = CLASSES[random.nextInt(CLASSES.length)];

        setClass(player, rolledClass);

        item.setAmount(item.getAmount() - 1);

        player.sendMessage(ChatColor.GOLD + "You rolled: " + ChatColor.YELLOW + rolledClass.toUpperCase());
    }

    // ===== CLASS SYSTEM =====
    private void setClass(Player player, String clazz) {
        String uuid = player.getUniqueId().toString();
        getConfig().set(uuid + ".class", clazz);
        saveConfig();
    }

    private String getClass(Player player) {
        return getConfig().getString(player.getUniqueId().toString() + ".class", "none");
    }

    private int getStrength(Player player) {
        return getConfig().getInt(player.getUniqueId().toString() + ".strength", 0);
    }

    // ===== REROLL RECIPE =====
   private void setupRerollRecipe() {

    ItemStack reroll = new ItemStack(Material.BOOK);
    ItemMeta meta = reroll.getItemMeta();
    meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Reroll Book");
    reroll.setItemMeta(meta);

    NamespacedKey key = new NamespacedKey(this, "reroll_book");

    ShapedRecipe recipe = new ShapedRecipe(key, reroll);

    recipe.shape(
            "I G I",
            "G D G",
            "I G I"

    );

    recipe.setIngredient('I', Material.IRON_BLOCK);
    recipe.setIngredient('G', Material.GOLD_BLOCK);
    recipe.setIngredient('D', Material.DIAMOND_BLOCK); // not used in this version but kept for safety

    Bukkit.addRecipe(recipe);
  }
}
