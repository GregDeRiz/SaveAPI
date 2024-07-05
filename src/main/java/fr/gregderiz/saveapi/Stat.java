package fr.gregderiz.saveapi;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Stat {
    private Location previousLocation;
    private GameMode previousGameMode;
    private ItemStack[] previousInventory;
    private ItemStack[] previousArmor;

    private final Player player;
    private final StatsManager playersManager;

    public Stat(Player player, StatsManager playersManager) {
        this.player = player;
        this.playersManager = playersManager;
    }

    public void save() {
        this.previousLocation = this.player.getLocation();
        this.previousGameMode = this.player.getGameMode();
        this.previousInventory = this.player.getInventory().getContents();
        this.previousArmor = this.player.getInventory().getArmorContents();

        this.player.getInventory().clear();
        this.playersManager.savePlayerStat(this.player, this);
    }

    public void restore(JavaPlugin plugin) {
        this.player.getInventory().clear();

        if (this.previousLocation != null) this.player.teleport(this.previousLocation);
        if (this.previousGameMode != null) this.player.setGameMode(this.previousGameMode);
        if (this.previousInventory != null) this.player.getInventory().setContents(this.previousInventory);
        if (this.previousArmor != null) this.player.getInventory().setArmorContents(this.previousArmor);

        this.playersManager.restorePlayerStat(this.player);
        if (plugin == null) return;

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> this.player.setFireTicks(0), 2);
    }
}
