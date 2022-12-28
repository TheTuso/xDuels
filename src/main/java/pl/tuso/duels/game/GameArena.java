package pl.tuso.duels.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Arena;
import pl.tuso.duels.api.DuelPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameArena implements Arena {
    private final String serializedName;
    private final Location redLocation;
    private final Location blueLocation;
    private File file;

    public GameArena(String serializedName, Location redLocation, Location blueLocation) {
        this.serializedName = serializedName;
        this.redLocation = redLocation;
        this.blueLocation = blueLocation;
        this.file = new File(Duels.getPlugin(Duels.class).getDataFolder(), "arenas/" + PlainTextComponentSerializer.plainText().serialize(this.getName()) + ".yml");
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
    public Location getRedSpawn() {
        return this.redLocation;
    }

    @Override
    public Location getBlueSpawn() {
        return this.blueLocation;
    }

    @Override
    public void buildFakeWalls(DuelPlayer... duelPlayers) {
        final BlockData blockData = Material.BARRIER.createBlockData();
        Arrays.stream(duelPlayers).forEach(duelPlayer -> this.getWallsFor(this.redLocation).forEach(location -> duelPlayer.getHandle().sendBlockChange(location, blockData)));
        Arrays.stream(duelPlayers).forEach(duelPlayer -> this.getWallsFor(this.blueLocation).forEach(location -> duelPlayer.getHandle().sendBlockChange(location, blockData)));
    }

    @Override
    public void destroyFakeWalls(DuelPlayer... duelPlayers) {
        final BlockData blockData = Material.AIR.createBlockData();
        Arrays.stream(duelPlayers).forEach(duelPlayer -> this.getWallsFor(this.redLocation).forEach(location -> duelPlayer.getHandle().sendBlockChange(location, blockData)));
        Arrays.stream(duelPlayers).forEach(duelPlayer -> this.getWallsFor(this.blueLocation).forEach(location -> duelPlayer.getHandle().sendBlockChange(location, blockData)));
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Contract(pure = true)
    private @NotNull Set<Location> getWallsFor(Location spawn) {
        final HashSet<Location> locations = new HashSet<>();
        for (int x = -1; x <= 1; x++)
            for (int y = 0; y <= 2; y++)
                for (int z = -1; z <= 1; z++) {
                    final Location location = spawn.clone().add(x, y, z);
                    if (location.equals(spawn) || location.equals(spawn.clone().add(0, 1, 0))) continue;
                    if (spawn.getWorld().getBlockData(location).getMaterial().isEmpty()) locations.add(location);
                }
        return locations;
    }
}
