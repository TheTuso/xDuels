package pl.tuso.duels;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;
import pl.tuso.duels.command.DuelsCommand;
import pl.tuso.duels.game.GameSystem;
import pl.tuso.duels.message.Messages;

public class Duels extends JavaPlugin {
    private Messages messages;
    private GameSystem gameSystem;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.messages = new Messages(this);
        this.gameSystem = new GameSystem(this);

        this.gameSystem.getGameKitManager().reloadKits();

        this.getLogger().info(PlainTextComponentSerializer.plainText().serialize(this.messages.getLine("system.hello")));

        new DuelsCommand(this).register(this.getCommand("duels"));
    }

    @Override
    public void onDisable() {

    }

    public Messages getMessages() {
        return this.messages;
    }

    public GameSystem getGameSystem() {
        return this.gameSystem;
    }
}
