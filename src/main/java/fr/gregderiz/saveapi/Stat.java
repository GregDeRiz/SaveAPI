package fr.gregderiz.saveapi;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Stat {
    private final Map<StatType, Object> stats;

    private final Player player;
    private final StatsManager playersManager;

    public Stat(Player player, StatsManager playersManager) {
        this.player = player;
        this.playersManager = playersManager;
        this.stats = Maps.newHashMap();
    }

    public void saveAll() {
        this.stats.put(StatType.LOCATION, this.player.getLocation());
        this.stats.put(StatType.GAMEMODE, this.player.getGameMode());
        this.stats.put(StatType.INVENTORY, this.player.getInventory().getContents());
        this.stats.put(StatType.ARMOR, this.player.getInventory().getArmorContents());

        this.player.getInventory().clear();
        this.playersManager.savePlayerStat(this.player, this);
    }

    public void save(StatType[] statTypes, Object[] objects) {
        for (int i = 0; i < statTypes.length; i++)
            for (int j = 0; j < objects.length; j++)
                this.stats.put(statTypes[i], objects[i]);

        this.player.getInventory().clear();
        this.playersManager.savePlayerStat(this.player, this);
    }

    public void restoreAll(JavaPlugin plugin) {
        this.player.getInventory().clear();

        if (this.stats.containsKey(StatType.INVENTORY)) {
            Location location = (Location) this.stats.get(StatType.LOCATION);
            if (location != null) this.player.teleport(location);
        }

        if (this.stats.containsKey(StatType.GAMEMODE)) {
            GameMode gameMode = (GameMode) this.stats.get(StatType.GAMEMODE);
            if (gameMode != null) this.player.setGameMode(gameMode);
        }

        if (this.stats.containsKey(StatType.INVENTORY)) {
            ItemStack[] inventory = (ItemStack[]) this.stats.get(StatType.INVENTORY);
            if (inventory != null) this.player.getInventory().setContents(inventory);
        }

        if (this.stats.containsKey(StatType.ARMOR)) {
            ItemStack[] armor = (ItemStack[]) this.stats.get(StatType.ARMOR);
            if (armor != null) this.player.getInventory().setArmorContents(armor);
        }

        this.playersManager.restorePlayerStat(this.player);
        if (plugin == null) return;

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> this.player.setFireTicks(0), 2);
    }
}
