package pl.tuso.duels.api;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BattleManager {
    boolean createBattle(DuelPlayer red, DuelPlayer blue, Kit kit);

    @Nullable Battle getBattleWith(DuelPlayer duelPlayer);

    List<Battle> getBattles();
}
