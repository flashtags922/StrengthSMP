package me.anti.strength;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StrengthSMP extends JavaPlugin implements Listener, CommandExecutor {

    public final Map<UUID, Integer> strength = new HashMap<>();
    public final Map<UUID, String> weapon = new HashMap<>();

    private final Random random = new Random();

    private final String[] weapons = {
            "SWORD",
            "AXE",
            "BOW",
            "TRIDENT",
            "CROSSBOW",
            "SHIELD"
    };

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // JOIN EVENTS
        Bukkit.getPluginManager().registerEvents(this, this);

        // COMBAT LISTENER
        Bukkit.getPluginManager().registerEvents(
                new StrengthListener(this),
                this
        );

        // COMMANDS
        getCommand("strength").setExecutor(this);
        getCommand("withdraw").setExecutor(this);

        // RECIPES
        addRecipes();

        getLogger().info("StrengthSMP Enabled!");
    }

    // ================= JOIN =================
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Bukkit.getLogger().info("JOIN EVENT WORKED");

        Player player = e.getPlayer();

        UUID id = player.getUniqueId();

        strength.putIfAbsent(id, 3);

        if (!weapon.containsKey(id)) {
            weapon.put(id, weapons[random.nextInt(weapons.length)]);
        }

        player.sendMessage(ChatColor.RED + "Strength: +" + strength.get(id));
        player.sendMessage(ChatColor.YELLOW + "Weapon: " + formatWeapon(weapon.get(id)));
    }

    // ================= STATUS =================
    private void sendStatus(Player p) {

        UUID id = p.getUniqueId();

        int playerStrength = strength.getOrDefault(id, 3);
        String playerWeapon = weapon.getOrDefault(id, "NONE");

        p.sendMessage(ChatColor.RED + "Strength: +" + playerStrength);
        p.sendMessage(ChatColor.YELLOW + "Weapon: " + MessageManager.formatWeapon(playerWeapon));
    }

    // ================= WEAPON FORMAT =================
    private String formatWeapon(String weapon) {

        switch (weapon.toUpperCase()) {

            case "SWORD":
                return ChatColor.RED + "⚔ Sword";

            case "AXE":
                return ChatColor.DARK_RED + "🪓 Axe";

            case "BOW":
                return ChatColor.GREEN + "🏹 Bow";

            case "TRIDENT":
                return ChatColor.AQUA + "🔱 Trident";

            case "CROSSBOW":
                return ChatColor.BLUE + "➹ Crossbow";

            case "SHIELD":
                return ChatColor.GRAY + "🛡 Shield";
        }

        return ChatColor.WHITE + weapon;
    }

    // ================= COMMANDS =================
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        UUID id = player.getUniqueId();

        // ================= /strength =================
        if (command.getName().equalsIgnoreCase("strength")) {

            sendStatus(player);

            return true;
        }

        // ================= /withdraw =================
        if (command.getName().equalsIgnoreCase("withdraw")) {

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "/withdraw <amount>");
                return true;
            }

            int amount;

            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid number.");
                return true;
            }

            int current = strength.getOrDefault(id, 3);

            current -= amount;

            if (current < -3) {
                current = -3;
            }

            strength.put(id, current);

            player.sendMessage(ChatColor.RED + "Withdrawn " + amount + " strength.");

            return true;
        }

        return true;
    }

    // ================= RECIPES =================
    private void addRecipes() {

        // ================= STRENGTH CORE =================
        ItemStack strengthCore = new ItemStack(Material.NETHER_STAR);

        ItemMeta coreMeta = strengthCore.getItemMeta();

        if (coreMeta != null) {

            coreMeta.setDisplayName(ChatColor.RED + "Strength Core");

            strengthCore.setItemMeta(coreMeta);
        }

        NamespacedKey coreKey = new NamespacedKey(this, "strength_core");

        ShapedRecipe coreRecipe = new ShapedRecipe(coreKey, strengthCore);

        coreRecipe.shape(
                "EIE",
                "INI",
                "EIE"
        );

        coreRecipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        coreRecipe.setIngredient('I', Material.NETHERITE_INGOT);
        coreRecipe.setIngredient('N', Material.NETHER_STAR);

        Bukkit.addRecipe(coreRecipe);

        // ================= REROLL BOOK =================
        ItemStack rerollBook = new ItemStack(Material.BOOK);

        ItemMeta bookMeta = rerollBook.getItemMeta();

        if (bookMeta != null) {

            bookMeta.setDisplayName(ChatColor.GREEN + "Reroll Book");

            rerollBook.setItemMeta(bookMeta);
        }

        NamespacedKey rerollKey = new NamespacedKey(this, "reroll_book");

        ShapedRecipe rerollRecipe = new ShapedRecipe(rerollKey, rerollBook);

        rerollRecipe.shape(
                "IGI",
                "GDG",
                "IGI"
        );

        rerollRecipe.setIngredient('I', Material.IRON_BLOCK);
        rerollRecipe.setIngredient('G', Material.GOLD_BLOCK);
        rerollRecipe.setIngredient('D', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(rerollRecipe);
    }
}
