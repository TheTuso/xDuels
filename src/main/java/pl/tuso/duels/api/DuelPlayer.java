package pl.tuso.duels.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DuelPlayer {
    boolean isFighting();

    boolean loadKit(Kit kit);

    boolean loadInventoryBackup();

    UUID getUuid();

    String getSerializedDisplayName();

    @NotNull Player getHandle();
}
