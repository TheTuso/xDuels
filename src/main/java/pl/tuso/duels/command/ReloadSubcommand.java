package pl.tuso.duels.command;

import io.papermc.paper.command.PaperSubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;

public class ReloadSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;

    public ReloadSubcommand(Duels duels, String adminPermission) {
        this.duels = duels;
        this.adminPermission = adminPermission;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String[] args) {
        if (!sender.hasPermission(this.adminPermission)) {
            sender.sendMessage(this.duels.getMessages().getLine("command.permission", subCommand, this.adminPermission));
            return false;
        }
        this.duels.reloadConfig();
        this.duels.getMessages().reload();
        this.duels.getGameSystem().getKitManager().reloadKits();
        this.duels.getGameSystem().getArenaManager().reloadArenas();
        sender.sendMessage(this.duels.getMessages().getLine("command.reload.success"));
        return true;
    }
}
