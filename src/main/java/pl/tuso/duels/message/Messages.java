package pl.tuso.duels.message;

import com.google.common.base.Charsets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Messages {
    private final Plugin plugin;
    private final String name;
    private final File file;
    private YamlConfiguration yamlConfiguration;

    public Messages(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.name = plugin.getConfig().getString("messages", "messages.yml");
        this.file = new File(this.plugin.getDataFolder(), this.name);

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.plugin.saveResource(this.name, false);
        }

        this.reload();
    }

    public void reload() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);

        final InputStream defConfigStream = this.plugin.getResource(this.name);
        if (defConfigStream == null) return;
        this.yamlConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public Component getLine(String path, Object @NotNull ... args) {
        final Component line = this.getLineOrNull(path, args);
        return line == null ?
                MiniMessage.miniMessage().deserialize(String.format(path + (args.length > 0 ? " [" + String.join(", ", Arrays.stream(args).map(o -> o.toString()).collect(Collectors.toList())) + "]" : ""), args)) :
                line;
    }

    public Component getLineOrNull(String path, Object @NotNull ... args) {
        final String line = this.yamlConfiguration.getString(path);
        return line == null ? null : MiniMessage.miniMessage().deserialize(String.format(line, args));
    }
}
