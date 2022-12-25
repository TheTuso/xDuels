package pl.tuso.duels.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Kit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GameKit implements Kit {
    private final String serializedName;
    private final HashMap<Integer, ItemStack> equipment;
    private final File file;

    public GameKit(String serializedName, HashMap<Integer, ItemStack> equipment) {
        this.serializedName = serializedName;
        this.equipment = equipment;
        this.file = new File(Duels.getPlugin(Duels.class).getDataFolder(), "kits/" + this.getPlainName() + ".yml");
    }

    public GameKit(String serializedName, @NotNull Inventory inventory) {
        this.serializedName = serializedName;
        final HashMap<Integer, ItemStack> equipment = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++) if (inventory.getItem(i) != null) equipment.put(i, inventory.getItem(i));
        this.equipment = equipment;
        this.file = new File(Duels.getPlugin(Duels.class).getDataFolder(), "kits/" + this.getPlainName() + ".yml");
    }

    public GameKit(String serializedName, HashMap<Integer, ItemStack> equipment, File file) {
        this.serializedName = serializedName;
        this.equipment = equipment;
        this.file = file;
    }

    @Override
    public Component getName() {
        return MiniMessage.miniMessage().deserialize(this.serializedName);
    }

    @Override
    public String getSerializedName() {
        return this.serializedName;
    }

    @Override
    public String getPlainName() {
        return PlainTextComponentSerializer.plainText().serialize(this.getName());
    }

    @Override
    public HashMap<Integer, ItemStack> getEquipment() {
        return this.equipment;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    protected FileConfiguration saveToFile() throws IOException {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.exists()) file.delete();
        file.createNewFile();
        final FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.addDefault("name", this.serializedName);
        for (int i = 0; i < 41; i++) if (this.equipment.containsKey(i)) fileConfiguration.addDefault("equipment." + i, equipment.get(i));
        fileConfiguration.options().copyDefaults(true);
        fileConfiguration.save(file);
        return fileConfiguration;
    }

    public static @Nullable GameKit loadFromFile(@NotNull File file) throws IOException, InvalidConfigurationException {
        final FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.load(file);
        if (!fileConfiguration.contains("name") || !fileConfiguration.contains("equipment")) return null;
        final HashMap<Integer, ItemStack> equipment = new HashMap<>();
        for (int i = 0; i < fileConfiguration.getInt("size", 41); i++) {
            if (!fileConfiguration.contains("equipment." + i)) continue;
            equipment.put(i, fileConfiguration.getItemStack("equipment." + i));
        }
        return new GameKit(fileConfiguration.getString("name"), equipment, file);
    }
}
