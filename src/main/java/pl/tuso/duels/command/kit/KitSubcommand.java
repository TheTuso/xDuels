package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.command.UnknownSubcommand;

import java.util.List;

public class KitSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final KitSaveSubcommand kitSaveSubcommand;
    private final KitRemoveSubcommand kitRemoveSubcommand;
    private final KitLoadSubcommand kitLoadSubcommand;
    private final KitListSubcommand kitListSubcommand;
    private final KitContentSubcommand kitContentSubcommand;
    private final UnknownSubcommand unknownSubcommand;

    public KitSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.adminPermission = adminPermission;
        this.unknownSubcommand = unknownSubcommand;
        this.kitSaveSubcommand = new KitSaveSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.kitRemoveSubcommand = new KitRemoveSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.kitLoadSubcommand = new KitLoadSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.kitListSubcommand = new KitListSubcommand(this.duels);
        this.kitContentSubcommand = new KitContentSubcommand(this.duels, this.unknownSubcommand);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length < 2) return this.unknownSubcommand.execute(sender, subCommand, args);
        return switch (args[1].toLowerCase()) {
            case "save" -> this.kitSaveSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "remove" -> this.kitRemoveSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "load" -> this.kitLoadSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "list" -> this.kitListSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "content" -> this.kitContentSubcommand.execute(sender, args[1].toLowerCase(), args);
            default -> this.unknownSubcommand.execute(sender, args[1].toLowerCase(), args);
        };
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length < 2) return PaperSubcommand.super.tabComplete(sender, subCommand, args);
        return args.length == 2 ? sender.hasPermission(this.adminPermission) ? List.of("save", "remove", "load", "list", "content") : List.of("list", "content") :
                args.length > 2 ? switch (args[1].toLowerCase()) {
                    case "save" -> this.kitSaveSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    case "remove" -> this.kitRemoveSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    case "load" -> this.kitLoadSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    case "list" -> this.kitListSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    case "content" -> this.kitContentSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    default -> this.unknownSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                } : this.unknownSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
    }
}
