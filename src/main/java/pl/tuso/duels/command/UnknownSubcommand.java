package pl.tuso.duels.command;

import io.papermc.paper.command.PaperSubcommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;

public class UnknownSubcommand implements PaperSubcommand {
    private final Duels duels;

    public UnknownSubcommand(Duels duels) {
        this.duels = duels;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String[] args) {
        final Component message = this.duels.getMessages().getLineOrNull("command.unknown." + subCommand, args);
        sender.sendMessage(message == null ? this.duels.getMessages().getLine("command.unknown.label", args) : message);
        return true;
    }
}
