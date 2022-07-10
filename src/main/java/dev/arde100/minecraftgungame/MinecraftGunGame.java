package dev.arde100.minecraftgungame;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class MinecraftGunGame extends JavaPlugin implements Listener {

    public HashMap<Player, Integer> killCounts = new HashMap<>();
    public HashMap<Integer, ItemStack[]> killItems = new HashMap<>();
    FileConfiguration config = getConfig();
    ArrayList<ItemStack> upgradeList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        killCounts.clear();
        createItems();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void updateEquipment() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            // Do stuff
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(killCounts.containsKey(event.getPlayer())) {
            killCounts.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = event.getPlayer().getKiller();
        if(killer instanceof Player) {
            if(killCounts.containsKey(killer)) {
                killCounts.put(killer, killCounts.get(killer)+1);
            } else {
                killCounts.put(killer,0);
            }
        }
        if(killCounts.containsKey(player)) {
            killCounts.put(player, Math.round(killCounts.get(player)/2));
        } else {
            killCounts.put(player, 0);
        }
        updateEquipment();
    }

    void createItems() {
        createWeaponsList();
    }

    void createWeaponsList() {
        upgradeList.add(new ItemStack(Material.WOODEN_AXE)); // 0
        upgradeList.add(new ItemStack(Material.WOODEN_SWORD)); // 1

        upgradeList.add(new ItemStack(Material.WOODEN_AXE)); // 2
        upgradeList.add(new ItemStack(Material.WOODEN_SWORD)); // 3

        upgradeList.add(new ItemStack(Material.STONE_AXE)); // 4
        upgradeList.add(new ItemStack(Material.STONE_SWORD)); // 5

        upgradeList.add(new ItemStack(Material.STONE_AXE)); // 6
        upgradeList.add(new ItemStack(Material.STONE_SWORD)); // 7

        upgradeList.add(new ItemStack(Material.IRON_AXE)); // 8
        upgradeList.add(new ItemStack(Material.IRON_SWORD)); // 9

        upgradeList.add(new ItemStack(Material.IRON_AXE)); // 10
        upgradeList.add(new ItemStack(Material.IRON_SWORD)); // 11

        upgradeList.add(new ItemStack(Material.DIAMOND_AXE)); // 12
        upgradeList.add(new ItemStack(Material.DIAMOND_SWORD)); // 13

        upgradeList.add(new ItemStack(Material.DIAMOND_AXE)); // 14
        upgradeList.add(new ItemStack(Material.DIAMOND_SWORD)); // 15
        // 2, 3, 6, 7, 10, 11, 14, 15

        for (ItemStack weapon : upgradeList) {
            ItemMeta meta = weapon.getItemMeta();
            meta.setUnbreakable(true);
            weapon.setItemMeta(meta);
        }

        upgradeList.get(2).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(3).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(6).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(7).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(10).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(11).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(14).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        upgradeList.get(15).addEnchantment(Enchantment.DAMAGE_ALL, 1);
    }
}
