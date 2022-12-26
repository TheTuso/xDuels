package pl.tuso.duels.game;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Arena;
import pl.tuso.duels.api.ArenaManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameArenaManager implements ArenaManager {
    private final Duels duels;
    private final List<Arena> arenas;

    public GameArenaManager(Duels duels) {
        this.duels = duels;
        this.arenas = new ArrayList<>();
    }

    @Override
    public List<Arena> getArenas() {
        return this.arenas;
    }

    @Override
    public @Nullable Arena getArena(String name) {
        final Optional<Arena> optionalArena = this.arenas.stream().filter(
                arena -> arena.getSerializedName().equalsIgnoreCase(name) ||
                        arena.getPlainName().equalsIgnoreCase(name)).findAny();
        return optionalArena.isPresent() ? optionalArena.get() : null;
    }

    @Override
    public void loadUnloadedArenas() {
        final File file = new File(this.duels.getDataFolder(), "arenas");
        if (!file.exists()) file.mkdirs();
        for (String fileName : file.list()) {
            try {
                if(!this.loadArena(fileName)) this.duels.getLogger().warning(String.format("Failed to load arena from %s!", fileName));
            } catch (Exception exception) {
                this.duels.getLogger().warning(String.format("Failed to load arena from %s!", fileName));
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean loadArena(String fileName) throws IOException, InvalidConfigurationException {
        final File file = new File(this.duels.getDataFolder(), "arenas/" + fileName);
        if (!file.exists()) throw new FileNotFoundException(String.format("Where is the %s?", fileName));
        if (this.arenas.stream().anyMatch(arena -> arena.getFile().getName().equalsIgnoreCase(fileName))) {
            this.duels.getLogger().warning(String.format("Arena %s is already loaded!", fileName));
            return false;
        }
        this.duels.getLogger().info(String.format("Loading arena from %s...", fileName));
        final Arena loadedArena = this.loadArenaFromFile(file);
        if (loadedArena == null) {
            this.duels.getLogger().warning(String.format("Couldn't load an arena from %s! Please verify all fields in the configuration file!", fileName));
            return false;
        }
        if (this.arenas.stream().anyMatch(arena -> arena.getPlainName().equalsIgnoreCase(loadedArena.getPlainName()))) {
            this.duels.getLogger().warning(String.format("There is already an arena with the same name! Please change the name field in %s!", fileName));
            return false;
        }
        return this.arenas.add(loadedArena);
    }

    @Override
    public void reloadArenas() {
        this.arenas.clear();
        this.loadUnloadedArenas();
    }

    @Override
    public boolean saveArena(Arena arena) {
        if (this.arenas.stream().anyMatch(otherArena -> otherArena.getPlainName().equalsIgnoreCase(arena.getPlainName()))) {
            this.duels.getLogger().warning(String.format("There is already an arena with this name (%s)!", arena.getPlainName()));
            return false;
        }
        try {
            this.duels.getLogger().info(String.format("Saving %s arena to the file...", arena.getPlainName()));
            this.saveArenaToFile(arena);
            this.arenas.add(arena);
            return true;
        } catch (IOException exception) {
            this.duels.getLogger().warning(String.format("Failed to save %s arena to the file!", arena.getPlainName()));
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeArena(Arena arena) {
        if (arena == null || !this.arenas.contains(arena)) return false;
        if (arena.getFile().exists()) arena.getFile().delete();
        this.arenas.remove(this.arenas.indexOf(arena));
        this.duels.getLogger().info(String.format("Arena %s has been removed!", arena.getPlainName()));
        return true;
    }

    private @NotNull FileConfiguration saveArenaToFile(@NotNull Arena arena) throws IOException {
        final File file = arena.getFile();
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.exists()) file.delete();
        file.createNewFile();
        final FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.addDefault("name", arena.getSerializedName());
        fileConfiguration.addDefault("red", arena.getRedSpawn());
        fileConfiguration.addDefault("blue", arena.getBlueSpawn());
        fileConfiguration.options().copyDefaults(true);
        fileConfiguration.save(file);
        arena.setFile(file);
        return fileConfiguration;
    }

    private @Nullable Arena loadArenaFromFile(File file) throws IOException, InvalidConfigurationException {
        final FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.load(file);
        if (!fileConfiguration.contains("name") || !fileConfiguration.contains("red") || !fileConfiguration.contains("blue")) return null;
        return new GameArena(fileConfiguration.getString("name"), fileConfiguration.getLocation("red"), fileConfiguration.getLocation("blue"));
    }
}
