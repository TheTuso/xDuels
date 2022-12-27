package pl.tuso.duels.api;

import java.util.List;

public interface Countdown {
    void start();

    void stop();

    List<DuelPlayer> getAudience();
}
