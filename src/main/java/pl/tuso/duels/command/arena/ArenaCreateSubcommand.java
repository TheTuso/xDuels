package pl.tuso.duels.command.arena;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Arena;
import pl.tuso.duels.command.UnknownSubcommand;
import pl.tuso.duels.game.GameArena;

public class ArenaCreateSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;

    public ArenaCreateSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
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
        if (args.length < 3) return this.unknownSubcommand.execute(sender, subCommand, args);
        if (sender instanceof Player player) {
            String serializedName = "";
            for (int i = 2; i < args.length; i++) serializedName += args[i] + " ";
            serializedName = serializedName.trim();
            final Arena arena = new GameArena(serializedName, player.getLocation(), player.getLocation());
            if (this.duels.getGameSystem().getArenaManager().saveArena(arena)) {
                sender.sendMessage(this.duels.getMessages().getLine("command.arena.create.success", arena.getSerializedName()));
                return true;
            } else {
                sender.sendMessage(this.duels.getMessages().getLine("command.arena.create.fail", arena.getSerializedName()));
                return false;
            }
        }
        sender.sendMessage(this.duels.getMessages().getLine("command.fakeplayer"));
        return false;
    }
}
