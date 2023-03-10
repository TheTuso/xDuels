package pl.tuso.duels.command;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Challenge;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.Kit;
import pl.tuso.duels.game.GamePlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DenySubcommand implements PaperSubcommand {
    private final Duels duels;
    private final UnknownSubcommand unknownSubcommand;

    public DenySubcommand(Duels duels, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String[] args) {
        if (sender instanceof Player receiver) {
            if (args.length < 3) return this.unknownSubcommand.execute(sender, subCommand, args);
            final DuelPlayer receiverDuelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(receiver.getUniqueId());
            final DuelPlayer senderDuelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(this.duels.getServer().getPlayerUniqueId(args[1]));
            if (senderDuelPlayer == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.player", args[1]));
                return false;
            }
            String name = "";
            for (int i = 2; i < args.length; i++) name += args[i] + " ";
            name = name.trim();
            final Kit kit = this.duels.getGameSystem().getKitManager().getKit(name);
            if (kit == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.kit", name));
                return false;
            }
            final Challenge challenge = this.duels.getGameSystem().getChallengeManager().getChallenge(senderDuelPlayer, receiverDuelPlayer, kit);
            if (challenge == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.challenge"));
                return false;
            }
            challenge.deny();
            return true;
        }
        sender.sendMessage(this.duels.getMessages().getLine("command.fakeplayer"));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String[] args) {
        if (sender instanceof Player player) {
            final DuelPlayer whom = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(player.getUniqueId());
            return switch (args.length) {
                case 2 -> this.duels.getGameSystem().getChallengeManager().getChallengesReceivedBy(whom).stream().map(challange -> challange.getSender().getHandle().getName()).collect(Collectors.toList());
                case 3 -> {
                    final UUID uuid = this.duels.getServer().getPlayerUniqueId(args[1]);
                    if (uuid == null) yield PaperSubcommand.super.tabComplete(sender, subCommand, args);
                    final DuelPlayer who = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(uuid);
                    yield this.duels.getGameSystem().getChallengeManager().getChallengesSendBy(who).stream().map(challange -> challange.getKit().getPlainName()).collect(Collectors.toList());
                }
                default -> PaperSubcommand.super.tabComplete(sender, subCommand, args);
            };
        }
        return PaperSubcommand.super.tabComplete(sender, subCommand, args);
    }
}
