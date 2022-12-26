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
import java.util.UUID;
import java.util.stream.Collectors;

public class AcceptSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final UnknownSubcommand unknownSubcommand;

    public AcceptSubcommand(Duels duels, UnknownSubcommand unknownSubcommand) {
        this.duels = duels;
        this.unknownSubcommand = unknownSubcommand;
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String[] args) {
        if (sender instanceof Player receiver) {
            if (args.length < 3) return this.unknownSubcommand.execute(sender, subCommand, args);
            final GamePlayer receiverGamePlayer = GamePlayer.getInstance(receiver.getUniqueId());
            if (receiverGamePlayer.isFighting()) {
                sender.sendMessage(this.duels.getMessages().getLine("command.fighting"));
                return false;
            }
            final GamePlayer senderGamePlayer = GamePlayer.getInstance(this.duels.getServer().getPlayerUniqueId(args[1]));
            if (senderGamePlayer == null) {
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
            final Challenge challenge = this.duels.getGameSystem().getChallengeManager().getChallenge(senderGamePlayer, receiverGamePlayer, kit);
            if (challenge == null) {
                sender.sendMessage(this.duels.getMessages().getLine("command.unknown.challenge"));
                return false;
            }
            challenge.accept();
            return true;
        }
        sender.sendMessage(this.duels.getMessages().getLine("command.fakeplayer"));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String @NotNull [] args) {
        if (sender instanceof Player player) {
            final GamePlayer whom = GamePlayer.getInstance(player.getUniqueId());
            return switch (args.length) {
                case 2 -> this.duels.getGameSystem().getChallengeManager().getChallengesReceivedBy(whom).stream().map(challange -> challange.getSender().getHandle().getName()).collect(Collectors.toList());
                case 3 -> {
                    final UUID uuid = this.duels.getServer().getPlayerUniqueId(args[1]);
                    if (uuid == null) yield PaperSubcommand.super.tabComplete(sender, subCommand, args);
                    final GamePlayer who = GamePlayer.getInstance(uuid);
                    yield this.duels.getGameSystem().getChallengeManager().getChallengesSendBy(who).stream().map(challange -> challange.getKit().getPlainName()).collect(Collectors.toList());
                }
                default -> PaperSubcommand.super.tabComplete(sender, subCommand, args);
            };
        }
        return PaperSubcommand.super.tabComplete(sender, subCommand, args);
    }
}
