package fr.gregderiz.saveapi;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class PlayersManager {
    private final Map<UUID, Stat> playerStats = Maps.newHashMap();

    public boolean isPlayerSaved(Player player) {
        return this.playerStats.containsKey(player.getUniqueId());
    }

    public void savePlayerStat(Player player, Stat stat) {
        if (isPlayerSaved(player)) return;

        this.playerStats.put(player.getUniqueId(), stat);
    }

    public void restorePlayerStat(Player player) {
        if (!isPlayerSaved(player)) return;

        this.playerStats.remove(player.getUniqueId());
    }

    public void restorePlayerStats() {
        playerStats.forEach((uuid, stat) -> stat.restore(null));
    }
}
