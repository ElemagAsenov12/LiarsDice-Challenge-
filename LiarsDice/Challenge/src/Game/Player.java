package Game;

import java.util.Scanner;
import java.util.ArrayList;

public class Player implements Playing{
    Scanner cin = new Scanner(System.in);
    private ArrayList<Dice> dices;
    private String nickname;
    private Bid currentBid;

    public Player(String nickname){
        this.nickname = nickname;
        this.dices = new ArrayList<Dice>();
        this.currentBid = new Bid(0,0);

        for (int i = 0; i < 5; i++){
            dices.add(new Dice());
        }
    }

    public void setCurrentBid(Bid currentBid) {
        this.currentBid = currentBid;
    }

    public Bid getCurrentBid() {
        return this.currentBid;
    }

    public ArrayList<Dice> getDices() {
        return this.dices;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        int option = 0;

        if(bidder instanceof Player){
            minValue = ((Player) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Player) bidder).getCurrentBid().getDiceQuantity();
        } else if(bidder instanceof Bot){
            minValue = ((Bot) bidder).getCurrentBid().getFaceValue();
            currentQuantity = ((Bot) bidder).getCurrentBid().getDiceQuantity();
        }

        if(hasBid)
        {
            while(true)
            {
                if(minValue == 6 && currentQuantity == maxQuantity){
                    System.out.println(this.getNickname() + " must challenge the bid.");
                    this.challengeBid(bidder, this);
                    Game.playerOnTurn++;
                    break;
                } else if(minValue == 6){
                    System.out.println("Maximum value bid reached, you can only raise the quantity.");
                    option = 'Q';
                } else if(currentQuantity == maxQuantity){
                    System.out.println("Maximum quantity bid reached. You can only raise the face value.");
                    option = 'F';
                } else {
                    System.out.println("Enter 'Q' to make/raise the current bid on quantity\n" +
                            "Enter 'F' to raise the face value and choose any quantity.");
                    option = cin.next().charAt(0);
                }

                if(option == 'Q' || option == 'F'){
                    break;
                } else
                {
                    System.out.println("Incorrect input.");
                }
            }

            switch(option)
            {
                case 81:
                    while(true)
                    {
                        System.out.print("Enter quantity: ");
                        newQuantity = cin.nextInt();
                        if(currentQuantity < newQuantity && newQuantity <= maxQuantity)
                        {
                            this.currentBid.setDiceQuantity(newQuantity);
                            this.currentBid.setFaceValue(minValue);
                            Game.playerOnTurn++;
                            break;
                        } else
                        {
                            System.out.println("You can bid a quantity between " + (currentQuantity + 1) + " and " + maxQuantity);
                        }
                    }
                    System.out.println("!-----------------New bidder-----------------!" +
                            "\nCurrent bid: Quantity[" + this.currentBid.getDiceQuantity() + "], FaceValue[" +
                            this.currentBid.getFaceValue() + "] from " + this.getNickname());
                    break;
                case 70:
                    while(true)
                    {
                        System.out.print("Enter face value: ");
                        newFaceValue = cin.nextInt();

                        if(minValue < newFaceValue && newFaceValue <= 6)
                        {
                            while(true)
                            {
                                System.out.print("Enter quantity: ");
                                newQuantity = cin.nextInt();

                                if(newQuantity <= maxQuantity)
                                {
                                    this.currentBid.setFaceValue(newFaceValue);
                                    this.currentBid.setDiceQuantity(newQuantity);
                                    Game.playerOnTurn++;
                                    break;
                                } else
                                {
                                    System.out.println("Quantity has to be less than " + (maxQuantity + 1));
                                }
                            }
                            System.out.println("!-----------------New bidder-----------------!" +
                                    "\nCurrent bid: Quantity[" + this.currentBid.getDiceQuantity() + "], FaceValue[" +
                                    this.currentBid.getFaceValue() + "] from " + this.getNickname());
                            break;
                        } else
                        {
                            System.out.println("You must bid on a face value from " + (minValue + 1) + " to 6");
                        }
                    }
                }
        } else
        {
            while(true)
            {
                System.out.print("Bid quantity: ");
                newQuantity = cin.nextInt();

                if(newQuantity <= maxQuantity)
                {
                    while(true)
                    {
                        System.out.print("Bid face value: ");
                        newFaceValue = cin.nextInt();

                        if(newFaceValue <= 6)
                        {
                            this.currentBid.setDiceQuantity(newQuantity);
                            this.currentBid.setFaceValue(newFaceValue);
                            Game.playerOnTurn++;
                            break;
                        } else {
                            System.out.println("You must bid on a face value from " + minValue + " to 6");
                        }
                    }
                    System.out.println("!-----------------New bidder-----------------!" +
                            "\nCurrent bid: Quantity[" + this.currentBid.getDiceQuantity() + "], FaceValue[" +
                            this.currentBid.getFaceValue() + "] from " + this.getNickname());
                    break;
                } else
                {
                    System.out.println("Quantity has to be less than " + (maxQuantity + 1));
                }
            }
        }
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
        int option;

        if(hasBid)
        {
            System.out.println(this.getNickname() + "'s turn. Enter:\n'B' - to make/raise bid.\n'C' - to challenge the current bid.");
            option = cin.next().charAt(0);

            switch(option)
            {
                case 66:
                    this.makeBid(bidder, maxQuantity, hasBid);
                    return true;
                case 67:
                    this.challengeBid(bidder, this);
                    return false;
                default:
                    System.out.println("Invalid character input.");
                    this.makeDecision(hasBid, bidder, maxQuantity);
                    break;
            }
        } else
        {
            System.out.println(this.getNickname() + " has to make a bid.");
            this.makeBid(bidder, maxQuantity, hasBid);
            return true;
        }
        return false;
    }

}
