package pl.tuso.duels.api;

import org.bukkit.Location;

public interface Lobby {
    void setLobbyLocation(Location location);

    Location getLobbyLocation();
}
