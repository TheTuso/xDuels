package pl.tuso.duels.command.arena;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import pl.tuso.duels.Duels;

public class ArenaListSubcommand implements PaperSubcommand {
    private final Duels duels;

    public ArenaListSubcommand(Duels duels) {
        this.duels = duels;
    }

    @Override
    public boolean execute(CommandSender sender, String subCommand, String[] args) {
        return false;
    }
}
