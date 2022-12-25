package pl.tuso.duels.command;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Challenge;
import pl.tuso.duels.api.Kit;
import pl.tuso.duels.game.GamePlayer;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final UnknownSubcommand unknownSubcommand;

    public ChallengeSubcommand(Duels duels, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String @NotNull [] args) { // TODO kit selection, GamePlayer instqance refactor
        if (sender instanceof Player who) {
            if (args.length < 2) return this.unknownSubcommand.execute(sender, subCommand, args);
            final Player whom = this.duels.getServer().getPlayer(args[1]);
            if (whom == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.player", args[1]));
                return false;
            }
            if (who.getUniqueId().equals(whom.getUniqueId())) {
                sender.sendMessage(this.duels.getMessages().getLine("command.challenge.self"));
                return false;
            }
            final GamePlayer whoGamePlayer = GamePlayer.getInstance(who.getUniqueId());
            final GamePlayer whomGamePlayer = GamePlayer.getInstance(whom.getUniqueId());
            if (args.length == 2) { // TODO open challenge gui
                sender.sendMessage(this.duels.getMessages().getLine("todo.challenge.gui"));
                return false;
            }
            String name = "";
            for (int i = 2; i < args.length; i++) name += args[i] + " ";
            name = name.trim();
            final Kit kit = this.duels.getGameSystem().getGameKitManager().getKit(name);
            if (kit == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.kit", name));
                return false;
            }
            final Challenge challenge = this.duels.getGameSystem().getGameChallengeManager().createChallange(whoGamePlayer, whomGamePlayer, kit);
            if (challenge == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.challenge.duplicate", name));
                return false;
            }
            challenge.announce();
            return true;
        }
        sender.sendMessage(this.duels.getMessages().getLine("command.fakeplayer"));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        return switch (args.length) {
            case 2 -> this.duels.getServer().getOnlinePlayers().stream().map(Player::getName).filter(s -> {
                if (sender instanceof Player self) return !s.equalsIgnoreCase(self.getName()); // True if doesn't match
                return true;
            }).collect(Collectors.toList());
            case 3 -> this.duels.getGameSystem().getGameKitManager().getKits().stream().map(kit -> kit.getPlainName()).collect(Collectors.toList());
            default -> PaperSubcommand.super.tabComplete(sender, subCommand, args);
        };
    }
}
