package pl.tuso.duels.api;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public interface Kit {
    Component getName();

    String getSerializedName();

    String getPlainName();

    HashMap<Integer, ItemStack> getEquipment();

    File getFile();
}
