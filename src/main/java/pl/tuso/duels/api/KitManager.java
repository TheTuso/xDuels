package pl.tuso.duels.api;

import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.game.GameKit;

import java.io.IOException;
import java.util.List;

public interface KitManager {
    List<Kit> getKits();

    @Nullable Kit getKit(String name);

    void loadUnloadedKits();

    boolean loadKit(String fileName) throws IOException, InvalidConfigurationException;

    void reloadKits();

    boolean saveKit(GameKit gameKit);

    boolean removeKit(Kit kit);
}
