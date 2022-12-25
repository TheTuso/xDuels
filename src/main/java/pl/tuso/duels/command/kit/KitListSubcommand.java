package pl.tuso.duels.command.kit;

import io.papermc.paper.command.PaperSubcommand;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Kit;

import java.util.List;

public class KitListSubcommand implements PaperSubcommand {
    private final Duels duels;

    public KitListSubcommand(Duels duels) {
        this.duels = duels;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String subCommand, String[] args) {
        final List<Kit> kits = this.duels.getGameSystem().getGameKitManager().getKits();
        sender.sendMessage(this.duels.getMessages().getLine("command.kit.list.title", kits.size()));
        kits.forEach(kit -> sender.sendMessage(this.duels.getMessages().getLine("command.kit.list.element", kit.getSerializedName())
                .hoverEvent(HoverEvent.showText(this.duels.getMessages().getLine("command.kit.list.hover", kit.getSerializedName())))
                .clickEvent(ClickEvent.runCommand("/duels kit content " + kit.getPlainName()))));
        return true;
    }
}
