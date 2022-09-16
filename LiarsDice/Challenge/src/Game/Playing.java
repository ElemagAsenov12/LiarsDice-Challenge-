package Game;

public interface Playing {
    int printDicePool(int guess);
    void rollHand();
    void makeBid(Playing bidder, int maxQuantity, boolean hasBid);
    void challengeBid(Playing bidder, Playing challenger);
    boolean makeDecision(boolean hasBid, Playing bidder, int maxQuantity);
    String getNickname();

    class Bid{
        private int faceValue;
        private int diceQuantity;

        public Bid(int faceValue, int diceQuantity){
            this.faceValue = faceValue;
            this.diceQuantity = diceQuantity;
        }

        public int getDiceQuantity() {
            return diceQuantity;
        }

        public int getFaceValue() {
            return faceValue;
        }

        public void setDiceQuantity(int diceQuantity) {
            this.diceQuantity = diceQuantity;
        }

        public void setFaceValue(int faceValue) {
            this.faceValue = faceValue;
        }
    }
}
