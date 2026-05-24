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

    private final Random random = new Random();

    private final String[] CLASSES = {
            "sword", "axe", "trident", "bow", "crossbow", "shield"
    };

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        setupRerollRecipe();

        getLogger().info("StrengthSMP ENABLED");
    }

    // =========================
    // JOIN SYSTEM
    // =========================
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        // create default data
        if (!getConfig().isSet(uuid + ".class")) {
            getConfig().set(uuid + ".class", "none");
            getConfig().set(uuid + ".strength", getConfig().getInt("def_strength"));
            saveConfig();
        }

        String weapon = getConfig().getString(uuid + ".class", "none");
        int strength = getConfig().getInt(uuid + ".strength");

        player.sendMessage("§a====================");
        player.sendMessage("§6Strength: §e" + strength + "+");
        player.sendMessage("§6Weapon: §e" + weapon.toUpperCase());
        player.sendMessage("§a====================");
    }

    // =========================
    // REROLL SYSTEM (CHANGES WEAPON = CLASS)
    // =========================
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

        String uuid = player.getUniqueId().toString();

        String rolledClass = CLASSES[random.nextInt(CLASSES.length)];

        getConfig().set(uuid + ".class", rolledClass);
        saveConfig();

        item.setAmount(item.getAmount() - 1);

        player.sendMessage("§6You rolled weapon: §f" + rolledClass.toUpperCase());
    }

    // =========================
    // REROLL RECIPE
    // =========================
    private void setupRerollRecipe() {

        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Reroll Guide");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to reroll weapon",
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
