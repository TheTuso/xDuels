package pl.tuso.duels.api;

public interface Challenge {
    boolean accept();

    boolean deny();

    void announce();

    DuelPlayer getSender();

    DuelPlayer getReceiver();

    Kit getKit();
}
