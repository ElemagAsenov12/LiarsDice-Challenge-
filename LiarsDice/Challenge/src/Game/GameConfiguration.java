package Game;

public interface GameConfiguration {
    void play() throws InterruptedException;
    void addParticipant(Playing participant);
    void removeParticipant(Playing participant);
    void rollDicePool();
}
