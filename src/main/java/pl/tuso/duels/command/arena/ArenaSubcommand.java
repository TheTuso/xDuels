package pl.tuso.duels.command.arena;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.command.UnknownSubcommand;

import java.util.List;

public class ArenaSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;
    private final ArenaCreateSubcommand arenaCreateSubcommand;
    private final ArenaRemoveSubcommand arenaRemoveSubcommand;
    private final ArenaListSubcommand arenaListSubcommand;

    public ArenaSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.adminPermission = adminPermission;
        this.unknownSubcommand = unknownSubcommand;
        this.arenaCreateSubcommand = new ArenaCreateSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.arenaRemoveSubcommand = new ArenaRemoveSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.arenaListSubcommand = new ArenaListSubcommand(this.duels);
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length < 2) return this.unknownSubcommand.execute(sender, subCommand, args);
        return switch (args[1].toLowerCase()) {
            case "create" -> this.arenaCreateSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "remove" -> this.arenaRemoveSubcommand.execute(sender, args[1].toLowerCase(), args);
            case "list" -> this.arenaListSubcommand.execute(sender, args[1].toLowerCase(), args);
            default -> this.unknownSubcommand.execute(sender, args[1].toLowerCase(), args);
        };
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (args.length < 2) return PaperSubcommand.super.tabComplete(sender, subCommand, args);
        return args.length == 2 ? sender.hasPermission(this.adminPermission) ? List.of("create", "remove", "list") : List.of("list") :
                args.length > 2 ? switch (args[1].toLowerCase()) {
                    case "remove" -> this.arenaRemoveSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                    default -> this.unknownSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
                } : this.unknownSubcommand.tabComplete(sender, args[1].toLowerCase(), args);
    }
}
