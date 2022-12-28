package pl.tuso.duels.game;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Lobby;

public class GameLobby implements Lobby { // TODO command /duels lobby
    private final Duels duels;
    private Location location;

    public GameLobby(Duels duels) {
        this.duels = duels;
        this.location = this.getLobbyLocation();
    }

    @Override
    public void setLobbyLocation(Location location) {
        Preconditions.checkNotNull(location, "location cannot be null!");
        this.location = location;
    }

    @Override
    public Location getLobbyLocation() {
        if (this.location != null) return this.location;
        this.duels.getConfig().addDefault("lobby", new Location(this.duels.getServer().getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F));
        this.duels.reloadConfig();
        this.location = this.duels.getConfig().getLocation("lobby");
        return this.location;
    }
}
