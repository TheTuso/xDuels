package pl.tuso.duels.game;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
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

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.inventoryBackup = new HashMap<>();
    }

    @Override
    public boolean isFighting() {
        return false;
    }

    @Override
    public boolean loadKit(@NotNull Kit kit) {
        final Player player = this.getHandle();
        if (!player.isOnline()) return false;
        this.inventoryBackup.clear();
        for (int i = 0; i < player.getInventory().getSize(); i++) this.inventoryBackup.put(i, player.getInventory().getItem(i));
        for (int i = 0; i < player.getInventory().getSize(); i++) player.getInventory().setItem(i, kit.getEquipment().get(i));
        return true;
    }

    @Override
    public boolean loadInventoryBackup() {
        final Player player = this.getHandle();
        if (!player.isOnline()) return false;
        for (int i = 0; i < player.getInventory().getSize(); i++) player.getInventory().setItem(i, this.inventoryBackup.get(i));
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
