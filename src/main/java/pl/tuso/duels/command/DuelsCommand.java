package pl.tuso.duels.command;

import com.google.common.base.Preconditions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.command.kit.KitSubcommand;

import java.util.List;
import java.util.stream.Collectors;

public class DuelsCommand implements TabExecutor {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;
    private final ChallengeSubcommand challengeSubcommand;
    private final AcceptSubcommand acceptSubcommand;
    private final DenySubcommand denySubcommand;
    private final KitSubcommand kitSubcommand;
    private final ReloadSubcommand reloadSubcommand;

    public DuelsCommand(Duels duels) {
        this.duels = duels;
        this.adminPermission = "duels.admin";
        this.unknownSubcommand = new UnknownSubcommand(this.duels);
        this.challengeSubcommand = new ChallengeSubcommand(this.duels, this.unknownSubcommand);
        this.acceptSubcommand = new AcceptSubcommand(this.duels, this.unknownSubcommand);
        this.denySubcommand = new DenySubcommand(this.duels, this.unknownSubcommand);
        this.kitSubcommand = new KitSubcommand(this.duels, this.adminPermission, this.unknownSubcommand);
        this.reloadSubcommand = new ReloadSubcommand(this.duels, this.adminPermission);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) return this.unknownSubcommand.execute(sender, "unknown", args);
        return switch (args[0].toLowerCase()) {
            case "challenge" -> this.challengeSubcommand.execute(sender, args[0], args);
            case "accept" -> this.acceptSubcommand.execute(sender, args[0], args);
            case "deny" -> this.denySubcommand.execute(sender, args[0], args);
            case "kit" -> this.kitSubcommand.execute(sender, args[0], args);
            case "reload" -> this.reloadSubcommand.execute(sender, args[0], args);
            default -> this.unknownSubcommand.execute(sender, args[0], args);
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        final List<String> suggestions = args.length == 1 ? sender.hasPermission(this.adminPermission) ? List.of("challenge", "accept", "deny", "kit", "reload") : List.of("challenge", "accept", "deny", "kit") :
                args.length > 1 ? switch (args[0].toLowerCase()) {
                    case "challenge" -> this.challengeSubcommand.tabComplete(sender, args[0], args);
                    case "accept" -> this.acceptSubcommand.tabComplete(sender, args[0], args);
                    case "deny" -> this.denySubcommand.tabComplete(sender, args[0], args);
                    case "kit" -> this.kitSubcommand.tabComplete(sender, args[0], args);
                    default -> this.unknownSubcommand.tabComplete(sender, args[0], args);
                } : this.unknownSubcommand.tabComplete(sender, args[0], args);
        return suggestions.stream().filter(s -> s.regionMatches(true, 0, args[args.length - 1], 0, args[args.length - 1].length())).collect(Collectors.toList());
    }

    public void register(PluginCommand pluginCommand) {
        Preconditions.checkNotNull(pluginCommand, "pluginCommand cannot be null!");
        pluginCommand.setExecutor(this::onCommand);
        pluginCommand.setTabCompleter(this::onTabComplete);
    }
}
