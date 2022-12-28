package pl.tuso.duels.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.io.File;

public interface Arena {
    Component getName();

    String getSerializedName();

    String getPlainName();

    Location getRedSpawn();

    Location getBlueSpawn();

    void buildWalls(DuelPlayer... duelPlayers);

    void destroyWalls(DuelPlayer... duelPlayers);

    void setFile(File file);

    File getFile();
}
