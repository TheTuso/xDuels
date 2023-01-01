package pl.tuso.duels.game;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.Kit;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayer implements DuelPlayer {
    private final UUID uuid;
    private final HashMap<Integer, ItemStack> inventoryBackup;
    private boolean fighting;
    private GameMode gameMode;
    private double health;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.inventoryBackup = new HashMap<>();
        this.fighting = false;
        this.gameMode = this.getHandle().getGameMode();
        this.health = this.getHandle().getHealth();
    }

    @Override
    public boolean isFighting() {
        return this.fighting;
    }

    @Override
    public void setFighting(boolean fighting) {
        this.fighting = fighting;
    }

    @Override
    public boolean loadState(@NotNull Kit kit) {
        final Player player = this.getHandle();
        if (!player.isOnline()) return false;
        this.inventoryBackup.clear();
        for (int i = 0; i < player.getInventory().getSize(); i++) this.inventoryBackup.put(i, player.getInventory().getItem(i));
        for (int i = 0; i < player.getInventory().getSize(); i++) player.getInventory().setItem(i, kit.getEquipment().get(i));
        this.gameMode = player.getGameMode();
        player.setGameMode(GameMode.SURVIVAL);
        if (player.isFlying()) this.getHandle().setFlying(false);
        this.health = player.getHealth();
        player.setHealth(20.0F);
        return true;
    }

    @Override
    public boolean loadStateBackup() {
        final Player player = this.getHandle();
        for (int i = 0; i < player.getInventory().getSize(); i++) player.getInventory().setItem(i, this.inventoryBackup.get(i));
        player.setGameMode(this.gameMode);
        return true;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getSerializedDisplayName() {
        return MiniMessage.miniMessage().serialize(this.getHandle().displayName());
    }

    @Override
    public @NotNull Player getHandle() {
        return Bukkit.getPlayer(this.uuid);
    }
}
