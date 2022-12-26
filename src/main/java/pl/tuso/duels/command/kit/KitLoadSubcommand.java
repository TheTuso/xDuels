package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.command.UnknownSubcommand;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class KitLoadSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;

    public KitLoadSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.adminPermission = adminPermission;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String[] args) {
        if (!sender.hasPermission(this.adminPermission)) {
            sender.sendMessage(this.duels.getMessages().getLine("command.permission", subCommand, this.adminPermission));
            return false;
        }
        if (args.length != 3) return this.unknownSubcommand.execute(sender, subCommand, args);
        final String fileName = args[2].contains(".yml") ? args[2] : args[2].concat(".yml");
        try {
            if (this.duels.getGameSystem().getKitManager().loadKit(fileName)) sender.sendMessage(this.duels.getMessages().getLine("command.kit.load.success", fileName));
            else sender.sendMessage(this.duels.getMessages().getLine("command.kit.load.fail", fileName));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            sender.sendMessage(this.duels.getMessages().getLine("command.kit.load.fail", fileName));
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length != 3) return PaperSubcommand.super.tabComplete(sender, subCommand, args);
        final List<String> suggestions = List.of(new File(this.duels.getDataFolder(), "kits").list());
        return suggestions.stream().filter(s -> !this.duels.getGameSystem().getKitManager().getKits().stream().anyMatch(kit -> kit.getFile().getName().equalsIgnoreCase(s))).collect(Collectors.toList());
    }
}
