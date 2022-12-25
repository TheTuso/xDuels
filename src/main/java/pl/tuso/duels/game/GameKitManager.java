package pl.tuso.duels.game;

import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Kit;
import pl.tuso.duels.api.KitManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameKitManager implements KitManager {
    private final Duels duels;
    private final ArrayList<Kit> gameKits;

    public GameKitManager(Duels duels) {
        this.duels = duels;
        this.gameKits = new ArrayList<>();
    }

    @Override
    public List<Kit> getKits() {
        return this.gameKits;
    }

    @Override
    public @Nullable Kit getKit(String name) {
        final Optional<Kit> kit = this.gameKits.stream().filter(
                        gameKit -> gameKit.getSerializedName().equalsIgnoreCase(name) ||
                                gameKit.getPlainName().equalsIgnoreCase(name)).findAny();
        return kit.isPresent() ? kit.get() : null;
    }

    @Override
    public void loadUnloadedKits() {
        final File file = new File(this.duels.getDataFolder(), "kits");
        if (!file.exists()) file.mkdirs();
        for (String fileName : file.list()) {
            try {
                if(!this.loadKit(fileName)) this.duels.getLogger().warning(String.format("Failed to load kit from %s!", fileName));
            } catch (Exception exception) {
                this.duels.getLogger().warning(String.format("Failed to load kit from %s!", fileName));
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean loadKit(String fileName) throws IOException, InvalidConfigurationException {
        final File file = new File(this.duels.getDataFolder(), "kits/" + fileName);
        if (!file.exists()) throw new FileNotFoundException(String.format("Where is the %s?", fileName));
        if (this.gameKits.stream().anyMatch(kit -> kit.getFile().getName().equalsIgnoreCase(fileName))) {
            this.duels.getLogger().warning(String.format("Kit %s is already loaded!", fileName));
            return false;
        }
        this.duels.getLogger().info(String.format("Loading kit from %s...", fileName));
        final GameKit gameKit = GameKit.loadFromFile(file);
        if (this.gameKits.stream().anyMatch(kit -> kit.getPlainName().equalsIgnoreCase(gameKit.getPlainName()))) {
            this.duels.getLogger().warning(String.format("There is already a kit with the same name! Please change the name field in %s!", fileName));
            return false;
        }
        return gameKit != null && !gameKit.getEquipment().isEmpty() ? this.gameKits.add(gameKit) : false;
    }

    @Override
    public void reloadKits() {
        this.gameKits.clear();
        this.loadUnloadedKits();
    }

    @Override
    public boolean saveKit(@NotNull GameKit gameKit) {
        if (gameKit.getEquipment().isEmpty()) {
            this.duels.getLogger().warning(String.format("The equipment of the kit cannot be empty (%s)!", gameKit.getPlainName()));
            return false;
        }
        if (this.gameKits.stream().anyMatch(kit -> kit.getPlainName().equalsIgnoreCase(gameKit.getPlainName()))) {
            this.duels.getLogger().warning(String.format("There is already a kit with this name (%s)!", gameKit.getPlainName()));
            return false;
        }
        try {
            this.duels.getLogger().info(String.format("Saving %s kit to the file...", gameKit.getPlainName()));
            gameKit.saveToFile();
            this.gameKits.add(gameKit);
            return true;
        } catch (IOException exception) {
            this.duels.getLogger().warning(String.format("Failed to save %s kit to the file!", gameKit.getPlainName()));
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeKit(Kit kit) {
        if (kit == null || !this.gameKits.contains(kit)) return false;
        if (kit.getFile().exists()) kit.getFile().delete();
        this.gameKits.remove(this.gameKits.indexOf(kit));
        this.duels.getLogger().info(String.format("Kit %s has been removed!", kit.getPlainName()));
        return true;
    }
}
