package me.anti.strength;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

        // REROLL LISTENER
        Bukkit.getPluginManager().registerEvents(
                new RerollListener(this),
                this
        );

        // STRENGTH ORB LISTENER
        Bukkit.getPluginManager().registerEvents(
                new StrengthOrbListener(this),
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

        Player player = e.getPlayer();

        UUID id = player.getUniqueId();

        strength.putIfAbsent(id, 3);

        if (!weapon.containsKey(id)) {
            weapon.put(id, weapons[random.nextInt(weapons.length)]);
        }

        player.sendMessage(
                ChatColor.YELLOW +
                        "Strength: " +
                        ChatColor.RED +
                        "+" + strength.get(id)
        );

        player.sendMessage(
                ChatColor.YELLOW +
                        "Weapon: " +
                        MessageManager.formatWeapon(
                                weapon.get(id)
                        )
        );
    }

    // ================= STATUS =================
    private void sendStatus(Player p) {

        UUID id = p.getUniqueId();

        int playerStrength =
                strength.getOrDefault(id, 3);

        String playerWeapon =
                weapon.getOrDefault(id, "NONE");

        p.sendMessage(
                ChatColor.YELLOW +
                        "Strength: " +
                        ChatColor.RED +
                        "+" + playerStrength
        );

        p.sendMessage(
                ChatColor.YELLOW +
                        "Weapon: " +
                        MessageManager.formatWeapon(
                                playerWeapon
                        )
        );
    }

    // ================= COMMANDS =================
    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        UUID id = player.getUniqueId();

        // ================= /strength =================
        if (command.getName()
                .equalsIgnoreCase("strength")) {

            sendStatus(player);

            return true;
        }

        // ================= /withdraw =================
        if (command.getName()
                .equalsIgnoreCase("withdraw")) {

            if (args.length == 0) {

                player.sendMessage(
                        ChatColor.RED +
                                "/withdraw <amount>"
                );

                return true;
            }

            int amount;

            try {

                amount =
                        Integer.parseInt(args[0]);

            } catch (Exception e) {

                player.sendMessage(
                        ChatColor.RED +
                                "Invalid number."
                );

                return true;
            }

            int current =
                    strength.getOrDefault(id, 3);

            current -= amount;

            if (current < -3) {
                current = -3;
            }

            strength.put(id, current);

            WithdrawManager.giveStrengthOrb(
                    player,
                    amount
            );

            player.sendMessage(
                    ChatColor.YELLOW +
                            "You withdrew "
                            + ChatColor.RED +
                            amount +
                            ChatColor.YELLOW +
                            " strength."
            );

            return true;
        }

        return true;
    }

    // ================= RECIPES =================
    private void addRecipes() {

        // ================= STRENGTH CORE =================
        ItemStack strengthCore =
                new ItemStack(Material.NETHER_STAR);

        ItemMeta coreMeta =
                strengthCore.getItemMeta();

        if (coreMeta != null) {

            coreMeta.setDisplayName(
                    ChatColor.RED +
                            "Strength Core"
            );

            strengthCore.setItemMeta(coreMeta);
        }

        NamespacedKey coreKey =
                new NamespacedKey(
                        this,
                        "strength_core"
                );

        ShapedRecipe coreRecipe =
                new ShapedRecipe(
                        coreKey,
                        strengthCore
                );

        coreRecipe.shape(
                "EIE",
                "INI",
                "EIE"
        );

        coreRecipe.setIngredient(
                'E',
                Material.ENCHANTED_GOLDEN_APPLE
        );

        coreRecipe.setIngredient(
                'I',
                Material.NETHERITE_INGOT
        );

        coreRecipe.setIngredient(
                'N',
                Material.NETHER_STAR
        );

        Bukkit.addRecipe(coreRecipe);

        // ================= REROLL BOOK =================
        ItemStack rerollBook =
                new ItemStack(Material.BOOK);

        ItemMeta bookMeta =
                rerollBook.getItemMeta();

        if (bookMeta != null) {

            bookMeta.setDisplayName(
                    ChatColor.GREEN +
                            "Reroll Book"
            );

            rerollBook.setItemMeta(bookMeta);
        }

        NamespacedKey rerollKey =
                new NamespacedKey(
                        this,
                        "reroll_book"
                );

        ShapedRecipe rerollRecipe =
                new ShapedRecipe(
                        rerollKey,
                        rerollBook
                );

        rerollRecipe.shape(
                "DND",
                "EWE",
                "DTD"
        );

        rerollRecipe.setIngredient(
                'D',
                Material.DIAMOND_BLOCK
        );

        rerollRecipe.setIngredient(
                'N',
                Material.NETHERITE_HOE
        );

        rerollRecipe.setIngredient(
                'W',
                Material.WITHER_SKELETON_SKULL
        );

        rerollRecipe.setIngredient(
                'E',
                Material.ECHO_SHARD
        );

        rerollRecipe.setIngredient(
                'T',
                Material.TRIDENT
        );

        Bukkit.addRecipe(rerollRecipe);
    }
}
