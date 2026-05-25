package me.strengthsmp;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;

import java.util.*;

public class StrengthSMP extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<UUID, Integer> strength = new HashMap<>();
    private final Map<UUID, Weapon> weapon = new HashMap<>();

    private final Map<UUID, Integer> swordCombo = new HashMap<>();
    private final Map<UUID, Integer> axeCrit = new HashMap<>();
    private final Map<UUID, Boolean> axeUltReady = new HashMap<>();

    private final Map<UUID, Long> axeUltCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("strength").setExecutor(this);
        getCommand("strengthsmp:withdraw").setExecutor(this);

        registerRecipes();
    }

    // ================= JOIN =================
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        strength.putIfAbsent(p.getUniqueId(), getConfig().getInt("def_strength"));

        if (!weapon.containsKey(p.getUniqueId())) {
            Weapon w = Weapon.random();
            weapon.put(p.getUniqueId(), w);

            giveWeapon(p, w);

            p.sendMessage(ChatColor.YELLOW + "Your new weapon type is");
            p.sendMessage(w.symbol + " " + w.color + w.name());
        }

        showStrength(p);
    }

    // ================= WEAPON GIVE =================
    private void giveWeapon(Player p, Weapon w) {
        ItemStack item = new ItemStack(w.mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(w.symbol + " " + w.color + w.name());
        item.setItemMeta(meta);

        p.getInventory().addItem(item);
    }

    // ================= COMBAT =================
    @EventHandler
    public void hit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;

        Weapon w = weapon.get(p.getUniqueId());
        if (w == null) return;

        UUID id = p.getUniqueId();

        // ================= SWORD =================
        if (w == Weapon.SWORD) {
            int c = swordCombo.getOrDefault(id, 0) + 1;
            swordCombo.put(id, c);

            if (c >= 3) {
                e.setDamage(e.getDamage() * 1.3);
            }

            if (c >= 5) {
                p.sendMessage(ChatColor.RED + "⚔ Ready");
                swordCombo.put(id, 0);
            }
        }

        // ================= AXE =================
        if (w == Weapon.AXE) {

            int c = axeCrit.getOrDefault(id, 0) + 1;
            axeCrit.put(id, c);

            if (c >= 5) {
                axeCrit.put(id, 0);
                stun((Player) e.getEntity());
            }

            if (axeUltReady.getOrDefault(id, false)) {
                e.setDamage(e.getDamage() * 3);
                axeUltReady.put(id, false);
            }
        }
    }

    // ================= STUN =================
    private void stun(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));

        p.setWalkSpeed(0f);
        Bukkit.getScheduler().runTaskLater(this, () -> p.setWalkSpeed(0.2f), 20L);

        p.getWorld().spawnParticle(Particle.SPELL_WITCH, p.getLocation(), 15);
    }

    // ================= REROLL BOOK =================
    @EventHandler
    public void click(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null || item.getType() != Material.KNOWLEDGE_BOOK) return;

        Weapon w = Weapon.random();
        weapon.put(p.getUniqueId(), w);

        p.sendMessage(ChatColor.YELLOW + "Your new weapon type is");
        p.sendMessage(w.symbol + " " + w.color + w.name());

        giveWeapon(p, w);
    }

    // ================= COMMANDS =================
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if (!(s instanceof Player p)) return true;

        if (cmd.getName().equalsIgnoreCase("strength")) {
            showStrength(p);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("strengthsmp:withdraw")) {
            int amt = Integer.parseInt(args[0]);

            strength.put(p.getUniqueId(),
                    Math.max(0, strength.get(p.getUniqueId()) - amt));

            showStrength(p);
            return true;
        }

        return false;
    }

    // ================= DISPLAY =================
    private void showStrength(Player p) {
        int s = strength.get(p.getUniqueId());
        Weapon w = weapon.get(p.getUniqueId());

        p.sendMessage(ChatColor.RED + "Strength: +" + s);
        p.sendMessage(ChatColor.GRAY + "Weapon: " + w.symbol + " " + w.color + w.name());
    }

    // ================= RECIPES =================
    private void registerRecipes() {

        // STRENGTH RECIPE
        ItemStack strengthBook = new ItemStack(Material.ENCHANTED_BOOK);
        NamespacedKey k1 = new NamespacedKey(this, "strength");

        ShapedRecipe r1 = new ShapedRecipe(k1, strengthBook);
        r1.shape("ENE","NSN","ENE");

        r1.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        r1.setIngredient('N', Material.NETHERITE_INGOT);
        r1.setIngredient('S', Material.NETHER_STAR);

        Bukkit.addRecipe(r1);

        // REROLL BOOK
        ItemStack reroll = new ItemStack(Material.KNOWLEDGE_BOOK);
        NamespacedKey k2 = new NamespacedKey(this, "reroll");

        ShapedRecipe r2 = new ShapedRecipe(k2, reroll);
        r2.shape("IGI","GDG","IGI");

        r2.setIngredient('I', Material.IRON_BLOCK);
        r2.setIngredient('G', Material.GOLD_BLOCK);
        r2.setIngredient('D', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(r2);
    }

    // ================= WEAPONS =================
    enum Weapon {
        SWORD(ChatColor.RED, "⚔", Material.DIAMOND_SWORD),
        AXE(ChatColor.DARK_RED, "🪓", Material.DIAMOND_AXE),
        BOW(ChatColor.GOLD, "🏹", Material.BOW),
        TRIDENT(ChatColor.AQUA, "🔱", Material.TRIDENT),
        SHIELD(ChatColor.GRAY, "🛡", Material.SHIELD),
        CROSSBOW(ChatColor.GREEN, "➹", Material.CROSSBOW);

        ChatColor color;
        String symbol;
        Material mat;

        Weapon(ChatColor c, String s, Material m) {
            color = c;
            symbol = s;
            mat = m;
        }

        static Weapon random() {
            return values()[new Random().nextInt(values().length)];
        }
    }
}
