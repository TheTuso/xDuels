package pl.tuso.duels.api;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ChallengeManager {
    Challenge createChallange(DuelPlayer sender, DuelPlayer receiver, Kit kit);

    @Nullable Challenge getChallenge(DuelPlayer sender, DuelPlayer reveiver, Kit kit);

    Set<Challenge> getChallenges();

    List<Challenge> getChallengesSendBy(DuelPlayer duelPlayer);

    List<Challenge> getChallengesReceivedBy(DuelPlayer duelPlayer);
}
