package Game;

import java.util.ArrayList;

public class Bot implements Playing, BotAI{
    private String nickname;
    private ArrayList<Dice> dices;
    private Bid currentBid;

    public Bot(){
        this.nickname = "BOT";
        this.dices = new ArrayList<Dice>();
        this.currentBid = new Bid(0,0);

        for(int i = 0; i < 5; i++){
            dices.add(new Dice());
        }
    }

    public void setCurrentBid(Bid currentBid) {
        this.currentBid = currentBid;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public ArrayList<Dice> getDices() {
        return this.dices;
    }

    @Override
    public int printDicePool(int guess) {
        int correctlyGuessed = 0;

        for (int i = 0; i < dices.size(); i++){
            System.out.print(dices.get(i).getCurrentFaceValue() + " ");

            if(dices.get(i).getCurrentFaceValue() == guess){
                correctlyGuessed++;
            }
        }
        return correctlyGuessed;
    }

    @Override
    public void rollHand() {
        for(int i = 0; i < dices.size(); i++){
            dices.get(i).roll();
        }
    }

    @Override
    public void makeBid(Playing bidder, int maxQuantity, boolean hasBid) {
        int minValue = 0;
        int newFaceValue = 0;
        int newQuantity = 0;
        int currentQuantity = 0;
        boolean option = false;

        if(bidder instanceof Player){
            minValue = ((Player) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Player) bidder).getCurrentBid().getDiceQuantity();
        } else if(bidder instanceof Bot){
            minValue = ((Bot) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Bot) bidder).getCurrentBid().getDiceQuantity();
        }

        if(hasBid)
        {
            if(minValue == 6 && currentQuantity == maxQuantity){
                System.out.println(this.getNickname() + " must challenge the bid.");
                this.challengeBid(bidder, this);
                return;
            } else if(minValue == 6){
                option = true;
            } else if(currentQuantity == maxQuantity){
                option = false;
            } else {
                option = this.calculateBid(minValue, currentQuantity, maxQuantity);
            }
            // if option == true, then the bot is going to keep the face and raise the quantity.
            // if option == false, then the bot is going to raise the face and pick a random quantity;

            if(option){
                while(true){
                    newQuantity = currentQuantity + (int)Math.floor(Math.random() * (maxQuantity - currentQuantity) + 1);
                    if(newQuantity > currentQuantity && newQuantity <= maxQuantity){
                        this.currentBid.setDiceQuantity(newQuantity);
                        this.currentBid.setFaceValue(minValue);
                        bidder = this;
                        Game.playerOnTurn++;
                        break;
                    }
                }
            } else {
                newFaceValue = (int) Math.floor((Math.random() * (5 - minValue)) + 1 + minValue);
                newQuantity = (int) Math.floor((Math.random() * (maxQuantity - 1) + 1));

                this.currentBid.setFaceValue(newFaceValue);
                this.currentBid.setDiceQuantity(newQuantity);
                Game.playerOnTurn++;
            }
        } else
        {
            newFaceValue = (int) Math.floor((Math.random() * (6 - 1)) + 1);
            newQuantity = (int) Math.floor((Math.random() * (maxQuantity - 1)) + 1);

            this.currentBid.setFaceValue(newFaceValue);
            this.currentBid.setDiceQuantity(newQuantity);
            Game.playerOnTurn++;
        }
        System.out.println("!-----------------New bidder-----------------!" +
                "\nCurrent bid: Quantity[" + this.currentBid.getDiceQuantity() + "], FaceValue[" +
                this.currentBid.getFaceValue() + "] from " + this.getNickname());
    }

    @Override
    public void challengeBid(Playing bidder, Playing challenger) {
        int guess = 0;
        if(bidder instanceof Player){
            guess = ((Player) bidder).getCurrentBid().getFaceValue();
        } else if (bidder instanceof Bot){
            guess = ((Bot) bidder).getCurrentBid().getFaceValue();
        }

        System.out.println(this.getNickname() + " challenged " + bidder.getNickname());
        Game.printPool(guess);
        Game.checkBid(bidder, challenger);
    }

    @Override
    public boolean makeDecision(boolean hasBid, Playing bidder, int maxQuantity) {
        boolean option = false;
        int minValue = 0;
        int currentQuantity = 0;

        if(bidder instanceof Player){
            minValue = ((Player) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Player) bidder).getCurrentBid().getDiceQuantity();
        } else {
            minValue = ((Bot) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Bot) bidder).getCurrentBid().getDiceQuantity();
        }

        System.out.println(this.getNickname() + "'s turn.");

        option = this.calculateDecision(minValue, currentQuantity, maxQuantity);

        if(hasBid){
            if(option){
                this.makeBid(bidder, maxQuantity, hasBid);
                return true;
            } else {
                this.challengeBid(bidder, this);
                return false;
            }
        } else {
            this.makeBid(bidder, maxQuantity, hasBid);
            return true;
        }
    }

    @Override
    public String getNickname() {
        return "BOT";
    }

    @Override
    public boolean calculateDecision(int minValue, int currentQuantity, int maxQuantity) {
        // double p = 1 / 6; -> probabilityOfDiceFacingAnyValue.
//        BigInteger nCr = BigInteger.ONE;
//        BigDecimal P = BigDecimal.ONE;
        int r = minValue;
        int n = maxQuantity;

        double P = Math.random() + 0.1; // probability of rolling at least X dice with a value of r.(X = currentQuantity, r = minValue)
        // P == 0.0 means the event can never happen. P > 0.0 && < 0.5 - the event is most likely not going to happen.
        // P == 0.5 means 50% chance of happening. P > 0.5 - most likely to happen. P == 1 - definitely going to happen.
        // I wanted to use math and probability formulas but numbers in them(factorials) caused the primitive types to overflow,
        // tried to rewrite them using BigInteger and BigDecimal, but mathematical functions didn't work properly afterwards...
        // USED SITE -> https://www.omnicalculator.com/statistics/dice

        //nCr = factorial(n).divide(factorial(r).multiply(factorial(n-r))); // Number of possible combinations.
//        for (int i = r; i <= 6; i++){
//            P += pfrexsv(nCr, int n, int r);
//        }

        return this.doRisk(P);
    }

    public boolean calculateBid(int minValue, int currentQuantity, int maxQuantity){
        return ((int) Math.floor(Math.random() * 9) + 1 <= 5);
        // Keeping the bot's choice random, without dependencies.

    //        if(minValue < 3 && currentQuantity < (maxQuantity / 2)){
    //
    //        } else if(minValue < 3 && currentQuantity >= (maxQuantity / 2)){
    //
    //        } else if(minValue > 2 && minValue < 5 && currentQuantity < (maxQuantity / 2)){
    //
    //        } else if(minValue > 2 && minValue < 5 && currentQuantity >= (maxQuantity / 2)){
    //
    //        } else if(minValue == 5 && currentQuantity < (maxQuantity / 2)){
    //
    //        } else if(minValue == 5 && currentQuantity >= (maxQuantity / 2)){
    //
    //        } else if(minValue == 6 && currentQuantity < (maxQuantity / 2)){
    //
    //        }
    }


//    private static BigInteger factorial(int number){
//        BigInteger factorial = BigInteger.ONE;
//        for (int i = 1; i <= number; i++){
//            factorial = factorial.multiply(BigInteger.valueOf(i));
//        }
//        return factorial;
//    }
//    private static BigInteger pfrexsv(BigInteger nCr, int n, int r){ // probabilityOfRollingExactXSameValue;
//        //nCr * Math.pow(n, r) * Math.pow((1 - n), (n - r));
//        return nCr.multiply(BigInteger.valueOf((long)Math.pow(n, r)).multiply(BigInteger.valueOf((long)Math.pow((1-n), (n-r)))));
//    }

    private boolean doRisk(double P){
        // Comparing P would've been usefull to somehow affect bots decisions based on the probability of the event happening.
        // Since I can't calculate it correctly, I will just keep it randomly generated.

        // true = bid
        // false = challenge
        if(P < 0.3){ // Chances of a correct bid are very low likely to happen.
            // P.compareTo(new BigDecimal("0.3") < 0) in case of working with BigDecimal.
            if ((int) Math.floor(Math.random() * 9) + 1 < 8) {
                return false;
            } else {
                return true;
            }
        } else if(P < 0.5){ // Chances of a correct bid are most likely not to happen.
            // P.compareTo(new BigDecimal("0.5") < 0)
            if((int) Math.floor(Math.random() * 9) + 1 <= 6){
                return false;
            } else {
                return true;
            }
        } else if(P == 0.5){ // Chances are 50/50.
            // P.compareTo(new BigDecimal("0.5") == 0)
            if((int) Math.floor(Math.random() * 9) + 1 <= 5){
                return false;
            } else {
                return true;
            }
        } else if(P > 0.5){ // Chances of a correct bid are most likely to happen.
            // P.compareTo(new BigDecimal("0.5") > 0)
            if((int) Math.floor(Math.random() * 10) + 1 < 3){
                return false;
            } else {
                return true;
            }
        } else if (P > 0.7){ // Chances of a correct bid are high likely to happen.
            // P.compareTo(new BigDecimal("0.7") > 0)
            if ((int) Math.floor(Math.random() * 9) + 1 < 8){
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
