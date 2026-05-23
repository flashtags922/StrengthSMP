package me.anti.strength;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class StrengthSMP extends JavaPlugin implements Listener {

    private FileConfiguration config;

    private final int MAX_STRENGTH = 5;
    private final int DEFAULT_STRENGTH = 3;
    private final int MIN_STRENGTH = -3;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        config = getConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("StrengthSMP enabled");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        String uuid = player.getUniqueId().toString();

        if (!config.contains(uuid)) {

            config.set(uuid, DEFAULT_STRENGTH);

            saveConfig();

            updateDamage(player, DEFAULT_STRENGTH);

            player.sendMessage(
                    ChatColor.GREEN +
                    "Your starting strength is " +
                    DEFAULT_STRENGTH
            );
        }
        else {

            int strength = config.getInt(uuid);

            updateDamage(player, strength);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        removeStrength(victim, 1);

        if (killer != null) {
            addStrength(killer, 1);
        }
    }

    @EventHandler
    public void onTokenUse(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.NETHER_STAR) return;

        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasDisplayName()) return;

        if (!meta.getDisplayName().equals(ChatColor.RED + "Strength Token")) return;

        addStrength(player, 1);

        item.setAmount(item.getAmount() - 1);

        player.sendMessage(ChatColor.GREEN + "+1 Strength consumed");
    }

    public void addStrength(Player player, int amount) {

        int strength = config.getInt(player.getUniqueId().toString());

        strength += amount;

        if (strength > MAX_STRENGTH) {
            strength = MAX_STRENGTH;
        }

        config.set(player.getUniqueId().toString(), strength);

        saveConfig();

        updateDamage(player, strength);

        player.sendMessage(ChatColor.GREEN + "+1 Strength");
    }

    public void removeStrength(Player player, int amount) {

        int strength = config.getInt(player.getUniqueId().toString());

        strength -= amount;

        if (strength < MIN_STRENGTH) {
            strength = MIN_STRENGTH;
        }

        config.set(player.getUniqueId().toString(), strength);

        saveConfig();

        updateDamage(player, strength);

        player.sendMessage(ChatColor.RED + "-1 Strength");
    }

    public void updateDamage(Player player, int strength) {

        double baseDamage = 1 + strength;

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                .setBaseValue(baseDamage);
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("strength")) {

            int strength = config.getInt(player.getUniqueId().toString());

            player.sendMessage(
                    ChatColor.GOLD +
                    "Strength: " +
                    strength
            );

            return true;
        }

        if (label.equalsIgnoreCase("withdraw")) {

            int strength = config.getInt(player.getUniqueId().toString());

            if (strength <= MIN_STRENGTH) {
                player.sendMessage(ChatColor.RED + "No strength to withdraw");
                return true;
            }

            removeStrength(player, 1);

            ItemStack star = new ItemStack(Material.NETHER_STAR);

            ItemMeta meta = star.getItemMeta();

            meta.setDisplayName(ChatColor.RED + "Strength Token");

            star.setItemMeta(meta);

            player.getInventory().addItem(star);

            player.sendMessage(ChatColor.GREEN + "Withdrawn 1 Strength");

            return true;
        }

        return true;
    }
}
