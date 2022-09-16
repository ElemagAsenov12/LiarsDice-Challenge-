package Game;

import java.util.Scanner;
import java.util.ArrayList;

public class Game implements GameConfiguration{
    public static boolean hasBid = false;
    public static int playerOnTurn = 0;
    private static ArrayList<Playing> participants;

    public Game(ArrayList<Playing> participants){
        this.participants = participants;
    }
    public Game(){
        this.participants = new ArrayList<Playing>();
    }

    @Override
    public void addParticipant(Playing participant) {
        if(participants.size() == 5){
            System.out.println(participant.getNickname() + " can't be joined to the game. Maximum players reached.");
        } else {
            participants.add(participant);
        }
    }

    @Override
    public void removeParticipant(Playing participant) {
        if(participants.size() == 0){
            System.out.println("There are no available participants to be removed.");
        } else {
            participants.remove(participant);
        }
    }

    @Override
    public void rollDicePool() {
        for(int i = 0; i < participants.size(); i++){
            participants.get(i).rollHand();
        }
    }

    public static void checkBid(Playing bidder, Playing challenger) {
        int faceValueBid = 0; //Value bidden by the -bidder-
        int quantityBid = 0; //Quantity of dice guessed by the -bidder-
        int wildDiceCount = 0;
        int actualDiceQuantity = 0; // The actual quantity of dice with the correct value bid.
        ArrayList<Dice> currentPlayerDicePool; // Reference to each participant's dice pool for every loop of the -for()-
        //I think the
        // func()
        // {
        // }
        //way of writing is easier to read and understand than
        // func(){
        // }
        // I want to ask for the best in practice way of professionals.

        if(bidder instanceof Player) //
        {
           faceValueBid = ((Player) bidder).getCurrentBid().getFaceValue();
           quantityBid = ((Player) bidder).getCurrentBid().getDiceQuantity();
        } else if (bidder instanceof Bot)
        {
           faceValueBid = ((Bot) bidder).getCurrentBid().getFaceValue();
           quantityBid = ((Bot) bidder).getCurrentBid().getDiceQuantity();
        }

        for (int i = 0; i < participants.size(); i++)
        {
           if(participants.get(i) instanceof Player)
           {
               currentPlayerDicePool = ((Player) participants.get(i)).getDices();
               for (int j = 0; j < currentPlayerDicePool.size(); j++)
               {
                   if(faceValueBid != 1 && currentPlayerDicePool.get(j).getCurrentFaceValue() == 1){
                       wildDiceCount++;
                   }
                   if(faceValueBid == currentPlayerDicePool.get(j).getCurrentFaceValue())
                   {
                       // Loops through the participant's dice pool and looks for dice
                       // that are with the same value as the bidder's chosen value.
                       // The variable down here counts them for later comparison.
                       actualDiceQuantity++;
                   }
               }
           } else if(participants.get(i) instanceof Bot) // Identical with the previous block, but for -Bot- instance.
           {
               currentPlayerDicePool = ((Bot) participants.get(i)).getDices();
               for (int j = 0; j < currentPlayerDicePool.size(); j++)
               {
                   if(faceValueBid != 1 && currentPlayerDicePool.get(j).getCurrentFaceValue() == 1){
                       wildDiceCount++;
                   }
                   if(faceValueBid == currentPlayerDicePool.get(j).getCurrentFaceValue())
                   {
                       actualDiceQuantity++;
                   }
               }
           }
        }

        if((actualDiceQuantity + wildDiceCount) < quantityBid) // Compares the participant's bid with the pool of dice values + wild dice.
        {
            if(bidder instanceof Player)
            {
                ((Player) participants.get(participants.indexOf(bidder))).getDices().remove(0);
                System.out.print("\nWild dice count - " + wildDiceCount);
                System.out.println("\n" + participants.get(participants.indexOf(bidder)).getNickname() + " lost a dice(" +
                        ((Player) participants.get(participants.indexOf(bidder))).getDices().size() + " dice left).");
                playerOnTurn = participants.indexOf(bidder);

                if(((Player) participants.get(participants.indexOf(bidder))).getDices().size() == 0)
                {
                    System.out.println(participants.get(participants.indexOf(bidder)).getNickname() + " has been eliminated.");
                    participants.remove(bidder);
                }
            } else if(bidder instanceof Bot)
            {
                ((Bot) participants.get(participants.indexOf(bidder))).getDices().remove(0);
                System.out.print("\nWild dice count - " + wildDiceCount);
                System.out.println("\n" + participants.get(participants.indexOf(bidder)).getNickname() + " lost a dice(" +
                        ((Bot) participants.get(participants.indexOf(bidder))).getDices().size() +" dice left).");
                playerOnTurn = participants.indexOf(bidder);

                if(((Bot) participants.get(participants.indexOf(bidder))).getDices().size() == 0)
                {
                    System.out.println(participants.get(participants.indexOf(bidder)).getNickname() + " has been eliminated.");
                    participants.remove(bidder);
                }
            }
        } else
        {
            if(challenger instanceof Player)
            {
                ((Player) participants.get(participants.indexOf(challenger))).getDices().remove(0);
                System.out.print("\nWild dice count - " + wildDiceCount);
                System.out.println("\n" + participants.get(participants.indexOf(challenger)).getNickname() + " lost a dice(" +
                        ((Player) participants.get(participants.indexOf(challenger))).getDices().size() + " dice left).");
                playerOnTurn = participants.indexOf(challenger);

                if(((Player) participants.get(participants.indexOf(challenger))).getDices().size() == 0)
                {
                    System.out.println(participants.get(participants.indexOf(challenger)).getNickname() + " has been eliminated.");
                    participants.remove(challenger);
                }
            } else if(challenger instanceof Bot)
            {
                ((Bot) participants.get(participants.indexOf(challenger))).getDices().remove(0);
                System.out.print("\nWild dice count - " + wildDiceCount);
                System.out.println("\n" + participants.get(participants.indexOf(challenger)).getNickname() + " lost a dice(" +
                        ((Bot) participants.get(participants.indexOf(challenger))).getDices().size() + " dice left).");
                playerOnTurn = participants.indexOf(challenger);

                if(((Bot) participants.get(participants.indexOf(challenger))).getDices().size() == 0)
                {
                    System.out.println(participants.get(participants.indexOf(challenger)).getNickname() + " has been eliminated.");
                    participants.remove(challenger);
                }
            }
        }
        hasBid = false;
    }

    private static int maxQuantityCount(){
        int maxCount = 0;

        for (int i = 0; i < participants.size(); i++){
            if(participants.get(i) instanceof Player){
                maxCount += ((Player) participants.get(i)).getDices().size();
            } else if(participants.get(i) instanceof Bot){
                maxCount += ((Bot) participants.get(i)).getDices().size();
            }
        }
        return maxCount;
    }

    public static void printPool(int guess){
        int correctlyGuessed = 0;
        System.out.print("Dice pool: ");
        for (int i = 0; i < participants.size(); i++)
        {
            correctlyGuessed += participants.get(i).printDicePool(guess);
        }
        System.out.print("\nThere are " + correctlyGuessed + " dice with value of " + guess);
    }

    @Override
    public void play() throws InterruptedException {
        Playing bidder;

        while (participants.size() > 1) {

            if(!hasBid){
                rollDicePool();
            }

            if(playerOnTurn == 0){
                bidder = participants.get(participants.size() - 1);
            } else {
                bidder = participants.get(playerOnTurn - 1);
            }

            System.out.println("=====================================================");
            hasBid = participants.get(playerOnTurn).makeDecision(hasBid, bidder, maxQuantityCount());

            if(playerOnTurn >= participants.size()){
                playerOnTurn = 0;
            }
            Thread.sleep(2000);
        }

        System.out.println("The winner is: " + participants.get(0).getNickname());
    }
}
