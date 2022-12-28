package pl.tuso.duels.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DuelPlayer {
    boolean isFighting();

    void setFighting(boolean fighting);

    boolean loadState(Kit kit);

    boolean loadStateBackup();

    UUID getUuid();

    String getSerializedDisplayName();

    @NotNull Player getHandle();
}
