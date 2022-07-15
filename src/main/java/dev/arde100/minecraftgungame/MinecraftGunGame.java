package dev.arde100.minecraftgungame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class MinecraftGunGame extends JavaPlugin implements Listener {

    public HashMap<Player, Integer> killCounts = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        killCounts.clear();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        // Enchantment.getByKey(NamespacedKey.minecraft(""));
        // Material.valueOf("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        killCounts.put(event.getPlayer(), 0);
        event.getPlayer().getInventory().clear();
        updateEquipment();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(killCounts.containsKey(event.getPlayer())) {
            event.getPlayer().getInventory().clear();
            killCounts.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = event.getPlayer().getKiller();
        if(killer.getType() == EntityType.PLAYER) {
            if(killCounts.containsKey(killer)) {
                killCounts.put(killer, killCounts.get(killer)+1);
            } else {
                killCounts.put(killer,1);
            }
            killer.sendMessage(ChatColor.BLUE + "You now have " + killCounts.get(killer) + " kills");
        }
        if(killCounts.containsKey(player)) {
            killCounts.put(player, (int) Math.round((killCounts.get(player)) * (getConfig().getDouble("multiplier-after-death"))));
        } else {
            killCounts.put(player, 0);
        }
        event.getDrops().clear();
        updateEquipment();
    }

    @EventHandler
    public void onInventoryEvent(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        updateEquipment();
    }

    void updateEquipment() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Integer kills = killCounts.get(player);

            if (!(getConfig().contains("killItems." + kills.toString()))) {
                continue;
            }
            Material material = Material.valueOf(getConfig().getString("killItems." + kills + ".item","IRON_SWORD").toUpperCase());
            ItemStack item = new ItemStack(material);
            if (!(material == Material.AIR)) {
                ItemMeta meta = item.getItemMeta();
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
            }
            if(getConfig().contains("killItems." + kills.toString() + ".enchants")) {
                for (String key : getConfig().getStringList("killItems." + kills + ".enchants")) {
                    ItemMeta enchantMeta = item.getItemMeta();
                    Integer level = Integer.valueOf(key.substring(key.indexOf(" ") + 1));
                    String enchant = key.substring(0, key.indexOf(" "));
                    player.sendMessage("" + enchant);
                    player.sendMessage("" + level);
                    player.sendMessage("" + Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase())));
                    enchantMeta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase())), level, true);
                    item.setItemMeta(enchantMeta);
                }

            }
            String mode = getConfig().getString("killItems." + kills + ".mode", "set");
            Integer slot = getConfig().getInt("killItems." + kills + ".slot", 0);
            if (mode.equalsIgnoreCase("set")) {
                if(slot == 100 || slot == 101 || slot == 102 || slot == 103) {
                    switch (slot) {
                        case 100:
                            player.getInventory().setBoots(item);
                            break;
                        case 101:
                            player.getInventory().setLeggings(item);
                            break;
                        case 102:
                            player.getInventory().setChestplate(item);
                            break;
                        case 103:
                            player.getInventory().setHelmet(item);
                            break;
                    }
                } else {
                    player.getInventory().setItem(slot, item);
                }
            }
            if (mode.equalsIgnoreCase("add")) {
                Integer emptySlot = player.getInventory().firstEmpty();
                player.getInventory().setItem(slot, item);
            }
            player.sendMessage("" + item + "\n" + item.getItemMeta());
        }
    }
}
