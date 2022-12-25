package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.command.UnknownSubcommand;
import pl.tuso.duels.game.GameKit;

public class KitSaveSubcommand implements PaperSubcommand {
    private final Duels duels;
    private final String adminPermission;
    private final UnknownSubcommand unknownSubcommand;

    public KitSaveSubcommand(Duels duels, String adminPermission, UnknownSubcommand unknownSubcommand) {
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
            final GameKit gameKit = new GameKit(serializedName, player.getInventory());
            if (this.duels.getGameSystem().getGameKitManager().saveKit(gameKit)) {
                sender.sendMessage(this.duels.getMessages().getLine("command.kit.save.success", gameKit.getSerializedName()));
                return true;
            } else {
                sender.sendMessage(this.duels.getMessages().getLine("command.kit.save.fail", gameKit.getSerializedName()));
                return false;
            }
        }
        sender.sendMessage(this.duels.getMessages().getLine("command.fakeplayer"));
        return false;
    }
}
