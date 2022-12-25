package pl.tuso.duels.game;

import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Challenge;
import pl.tuso.duels.api.ChallengeManager;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.Kit;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GameChallengeManager implements ChallengeManager {
    private final Duels duels;
    private final HashSet<Challenge> challanges;

    public GameChallengeManager(Duels duels) {
        this.duels = duels;
        this.challanges = new HashSet<>();
    }

    @Override
    public Challenge createChallange(DuelPlayer sender, DuelPlayer receiver, Kit kit) { // TODO challenge system
        if (this.getChallenge(sender, receiver, kit) != null) return null;
        final GameChallenge gameChallenge = new GameChallenge(this.duels, sender, receiver, kit);
        this.challanges.add(gameChallenge);
        return gameChallenge;
    }

    @Override
    public @Nullable Challenge getChallenge(DuelPlayer sender, DuelPlayer reveiver, Kit kit) {
        final Optional<Challenge> finalChallenge =  this.challanges.stream().filter(challenge ->
                challenge.getSender().equals(sender) &&
                challenge.getReceiver().equals(reveiver) &&
                challenge.getKit().equals(kit)).findAny();
        return finalChallenge.isPresent() ? finalChallenge.get() : null;
    }

    @Override
    public Set<Challenge> getChallenges() {
        return this.challanges;
    }

    @Override
    public List<Challenge> getChallengesSendBy(DuelPlayer duelPlayer) {
        return this.challanges.stream().filter(challenge -> challenge.getSender().equals(duelPlayer)).collect(Collectors.toList());
    }

    @Override
    public List<Challenge> getChallengesReceivedBy(DuelPlayer duelPlayer) {
        return this.challanges.stream().filter(challenge -> challenge.getReceiver().equals(duelPlayer)).collect(Collectors.toList());
    }
}
