package pl.tuso.duels.api;

import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public interface ArenaManager {
    List<Arena> getArenas();

    @Nullable Arena getArena(String name);

    void loadUnloadedArenas();

    boolean loadArena(String fileName) throws IOException, InvalidConfigurationException;

    void reloadArenas();

    boolean saveArena(Arena arena);

    boolean removeArena(Arena arena);
}
