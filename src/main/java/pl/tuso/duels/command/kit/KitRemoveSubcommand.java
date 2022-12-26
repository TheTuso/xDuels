package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Kit;
import pl.tuso.duels.command.UnknownSubcommand;

import java.util.List;
import java.util.stream.Collectors;

public class KitRemoveSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;

    public KitRemoveSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.adminPermission = adminPermission;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String @NotNull [] args) {
        if (!sender.hasPermission(this.adminPermission)) {
            sender.sendMessage(this.duels.getMessages().getLine("command.permission", subCommand, this.adminPermission));
            return false;
        }
        if (args.length < 3) return this.unknownSubcommand.execute(sender, subCommand, args);
        String name = "";
        for (int i = 2; i < args.length; i++) name += args[i] + " ";
        name = name.trim();
        final Kit kit = this.duels.getGameSystem().getKitManager().getKit(name);
        if (kit == null) {
            sender.sendMessage(this.duels.getMessages().getLine("command.unknown.kit", name));
            return false;
        }
        if (this.duels.getGameSystem().getKitManager().removeKit(kit)) {
            sender.sendMessage(this.duels.getMessages().getLine("command.kit.remove.success", name));
            return true;
        } else {
            sender.sendMessage(this.duels.getMessages().getLine("command.kit.remove.fail", name));
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length != 3) return PaperSubcommand.super.tabComplete(sender, subCommand, args);
        return this.duels.getGameSystem().getKitManager().getKits().stream().map(kit -> kit.getPlainName()).collect(Collectors.toList());
    }
}
