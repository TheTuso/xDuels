package pl.tuso.duels.api;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface DuelPlayerManager {
    Set<DuelPlayer> getDuelPlayers();

    DuelPlayer getDuelPlayer(@NotNull UUID uuid);
}
