package me.anti.strength;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Random;
public class StrengthSMP extends JavaPlugin implements Listener {

    private final String[] CLASSES = {
            "sword", "axe", "trident", "bow", "crossbow", "shield"
    };

    @Override
public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    setupRerollRecipe();
    getLogger().info("StrengthSMP ENABLED");
}
    

    // ===== JOIN MESSAGE (CHAT ONLY) =====
  @EventHandler
public void onJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage("TEST JOIN EVENT WORKS");
}
    // ===== REROLL BOOK USE =====
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.KNOWLEDGE_BOOK) return;
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        if (!meta.getDisplayName().equals(ChatColor.GREEN + "Reroll Guide")) return;

        event.setCancelled(true);

        Random random = new Random();
        String rolled = CLASSES[random.nextInt(CLASSES.length)];

        getConfig().set(player.getUniqueId().toString() + ".class", rolled);
        saveConfig();

        item.setAmount(item.getAmount() - 1);

        player.sendMessage(ChatColor.GOLD + "You rolled: " + ChatColor.YELLOW + rolled.toUpperCase());
    }

    // ===== REROLL RECIPE =====
    private void setupRerollRecipe() {

        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Reroll Guide");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to reroll your class",
                ChatColor.DARK_GRAY + "Consumes on use"
        ));

        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "reroll_book");

        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(
                "IGI",
                "GDG",
                "IGI"
        );

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);

        getServer().addRecipe(recipe);
    }
}
