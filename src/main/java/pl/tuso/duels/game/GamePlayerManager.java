package pl.tuso.duels.game;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.DuelPlayerManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GamePlayerManager implements DuelPlayerManager {
    private final Duels duels;
    private final Set<DuelPlayer> duelPlayers;

    public GamePlayerManager(Duels duels) {
        this.duels = duels;
        this.duelPlayers = new HashSet<>();
    }

    @Override
    public Set<DuelPlayer> getDuelPlayers() {
        return this.duelPlayers;
    }

    @Override
    public DuelPlayer getDuelPlayer(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid, "Uuid cannot be null!");
        if (this.duelPlayers.isEmpty() || !this.duelPlayers.stream().anyMatch(duelPlayer -> duelPlayer.getUuid().equals(uuid))) {
            this.duelPlayers.add(new GamePlayer(uuid));
            Bukkit.getLogger().info("Creating new instance of DuelPlayer for " + uuid);
        }
        return this.duelPlayers.stream().filter(duelPlayer -> duelPlayer.getUuid().equals(uuid)).findAny().get();
    }
}
