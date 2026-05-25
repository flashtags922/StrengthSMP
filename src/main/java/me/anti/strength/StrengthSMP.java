package me.strengthsmp;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class StrengthSMPPlugin extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<UUID, Integer> strength = new HashMap<>();
    private final Map<UUID, String> weapon = new HashMap<>();
    private final Map<UUID, Integer> combo = new HashMap<>();
    private final Map<UUID, Boolean> axeUlt = new HashMap<>();

    private final Random random = new Random();

    private final String[] weapons = {
            "SWORD", "AXE", "BOW", "TRIDENT", "CROSSBOW", "SHIELD"
    };

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("strength").setExecutor(this);
        getCommand("strengthsmp:withdraw").setExecutor(this);
        getCommand("rerollbook").setExecutor(this);

        addRecipes(); // IMPORTANT
    }

    // ================= JOIN =================
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        strength.putIfAbsent(id, 3);

        if (!weapon.containsKey(id)) {
            weapon.put(id, weapons[random.nextInt(weapons.length)]);
        }

        sendStatus(p);
        giveRerollBook(p);
    }

    // ================= STATUS =================
    private void sendStatus(Player p) {
        UUID id = p.getUniqueId();

        p.sendMessage(ChatColor.RED + "Strength: +" + strength.get(id));
        p.sendMessage(ChatColor.YELLOW + "Weapon: " + formatWeapon(weapon.get(id)));
    }

    private String formatWeapon(String w) {
        switch (w) {
            case "SWORD": return ChatColor.RED + "⚔ Sword";
            case "AXE": return ChatColor.DARK_RED + "🪓 Axe";
            case "BOW": return ChatColor.GREEN + "🏹 Bow";
            case "TRIDENT": return ChatColor.AQUA + "🔱 Trident";
            case "CROSSBOW": return ChatColor.BLUE + "➹ Crossbow";
            case "SHIELD": return ChatColor.GRAY + "🛡 Shield";
        }
        return w;
    }

    // ================= COMMANDS =================
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

        if (!(s instanceof Player)) return true;
        Player p = (Player) s;
        UUID id = p.getUniqueId();

        if (c.getName().equalsIgnoreCase("strength")) {
            sendStatus(p);
        }

        if (c.getName().equalsIgnoreCase("strengthsmp:withdraw")) {
            int amt = args.length > 0 ? Integer.parseInt(args[0]) : 1;
            strength.put(id, Math.max(-3, strength.get(id) - amt));
            p.sendMessage(ChatColor.RED + "Withdrawn " + amt + " strength.");
        }

        if (c.getName().equalsIgnoreCase("rerollbook")) {
            String w = weapons[random.nextInt(weapons.length)];
            weapon.put(id, w);

            p.sendMessage(ChatColor.YELLOW + "Your new weapon type is");
            p.sendMessage(formatWeapon(w));
        }

        return true;
    }

    private void giveRerollBook(Player p) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Reroll Book");
        item.setItemMeta(meta);

        p.getInventory().addItem(item);
    }

    // ================= COMBAT =================
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getDamager();
        Player t = (Player) e.getEntity();

        UUID id = p.getUniqueId();
        String w = weapon.get(id);

        if (w == null) return;

        int c = combo.getOrDefault(id, 0) + 1;
        combo.put(id, c);

        Bukkit.getScheduler().runTaskLater(this, () -> combo.put(id, 0), 60);

        // ================= AXE =================
        if (w.equals("AXE")) {

            if (c % 5 == 0) {
                stun(t, 20);
                p.sendMessage(ChatColor.RED + "STUN!");
            }

            if (axeUlt.getOrDefault(id, false)) {
                e.setDamage(8);
                axeUlt.put(id, false);
            }

            if (c == 5) {
                axeUlt.put(id, true);
            }
        }

        // ================= SWORD =================
        if (w.equals("SWORD")) {
            if (c % 3 == 0) {
                e.setDamage(e.getDamage() * 1.5);
            }
        }

        // ================= TRIDENT =================
        if (w.equals("TRIDENT")) {
            t.getWorld().strikeLightningEffect(t.getLocation());
        }

        // ================= BOW =================
        if (w.equals("BOW")) {
            if (c % 3 == 0) {
                e.setDamage(e.getDamage() * 2);
            }
        }

        // ================= CROSSBOW =================
        if (w.equals("CROSSBOW")) {
            if (c % 5 == 0) {
                t.teleport(p.getLocation());
            }
        }

        // ================= SHIELD =================
        if (w.equals("SHIELD")) {
            if (strength.get(id) >= 5) {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE, 40, 1));
            }
        }
    }

    // ================= STUN =================
    private void stun(Player p, int ticks) {
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i++ > ticks) {
                    cancel();
                    return;
                }

                p.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOW, 10, 10));
            }
        }.runTaskTimer(this, 0, 1);
    }

    // ================= RECIPES =================
    private void addRecipes() {

        // STRENGTH CORE
        ItemStack core = new ItemStack(Material.NETHER_STAR);
        ItemMeta cm = core.getItemMeta();
        cm.setDisplayName(ChatColor.RED + "Strength Core");
        core.setItemMeta(cm);

        NamespacedKey k1 = new NamespacedKey(this, "strength_core");

        ShapedRecipe r1 = new ShapedRecipe(k1, core);
        r1.shape("EIE", "INI", "EIE");
        r1.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        r1.setIngredient('I', Material.NETHERITE_INGOT);
        r1.setIngredient('N', Material.NETHER_STAR);

        Bukkit.addRecipe(r1);

        // REROLL BOOK
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bm = book.getItemMeta();
        bm.setDisplayName(ChatColor.GREEN + "Reroll Book");
        book.setItemMeta(bm);

        NamespacedKey k2 = new NamespacedKey(this, "reroll_book");

        ShapedRecipe r2 = new ShapedRecipe(k2, book);
        r2.shape("IGI", "GDG", "IGI");
        r2.setIngredient('I', Material.IRON_BLOCK);
        r2.setIngredient('G', Material.GOLD_BLOCK);
        r2.setIngredient('D', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(r2);
    }
}
