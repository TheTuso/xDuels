package pl.tuso.duels.command.arena;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Arena;

import java.util.List;

public class ArenaListSubcommand implements PaperSubcommand {
    private final Duels duels;

    public ArenaListSubcommand(Duels duels) {
        this.duels = duels;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String[] args) {
        final List<Arena> arenas = this.duels.getGameSystem().getArenaManager().getArenas();
        sender.sendMessage(this.duels.getMessages().getLine("command.arena.list.title", arenas.size()));
        arenas.forEach(arena -> sender.sendMessage(this.duels.getMessages().getLine("command.arena.list.element", arena.getSerializedName())));
        return true;
    }
}
