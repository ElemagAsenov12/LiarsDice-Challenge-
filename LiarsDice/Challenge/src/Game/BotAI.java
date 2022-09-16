package Game;

public interface BotAI {
    boolean calculateDecision(int minValue, int currentQuantity, int maxQuantity);
    boolean calculateBid(int minValue, int currentQuantity, int maxQuantity);
}
