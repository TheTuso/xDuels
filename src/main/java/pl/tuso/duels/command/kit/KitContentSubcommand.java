package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Kit;
import pl.tuso.duels.command.UnknownSubcommand;
import pl.tuso.duels.game.GameKit;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class KitContentSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final UnknownSubcommand unknownSubcommand;

    public KitContentSubcommand(Duels duels, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length < 3) return this.unknownSubcommand.execute(sender, subCommand, args);
        String name = "";
        for (int i = 2; i < args.length; i++) name += args[i] + " ";
        name = name.trim();
        final String finalName = name;
        final Kit kit = this.duels.getGameSystem().getGameKitManager().getKit(finalName);
        if (kit == null) {
            sender.sendMessage(this.duels.getMessages().getLine("command.unknown.kit", finalName));
            return false;
        }
        final Collection<ItemStack> content = kit.getEquipment().values();
        sender.sendMessage(this.duels.getMessages().getLine("command.kit.content.title", finalName, content.size()));
        content.forEach(itemStack -> sender.sendMessage(this.duels.getMessages().getLine("command.kit.content.element", MiniMessage.miniMessage().serialize(itemStack.displayName()))));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length != 3) return PaperSubcommand.super.tabComplete(sender, subCommand, args);
        return this.duels.getGameSystem().getGameKitManager().getKits().stream().map(kit -> kit.getPlainName()).collect(Collectors.toList());
    }
}
