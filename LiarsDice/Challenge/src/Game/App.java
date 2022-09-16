package Game;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Player p1 = new Player("CSS");
        Player p2 = new Player("HTML");
        Player p3 = new Player("JS");
        Player p4 = new Player("SQL");
        Player p5 = new Player("C#/++");
        Bot bot1 = new Bot();
        Bot bot2 = new Bot();
        Bot bot3 = new Bot();
        Bot bot4 = new Bot();
        Bot bot5 = new Bot();

        ArrayList<Playing> participants = new ArrayList<>();
        ArrayList<Playing> bots = new ArrayList<>();

        participants.add(p1);
        participants.add(p2);
        participants.add(p3);
        participants.add(p4);
        participants.add(p5);

        Game game1 = new Game(participants);
        Game testGame = new Game();
        // Create a game and pass already existing player/bot pool.
        // Or create a game and add participants one by one.
        testGame.addParticipant(bot1);
        testGame.addParticipant(p1);
        testGame.addParticipant(bot3);
        testGame.addParticipant(bot4);
        testGame.addParticipant(p2);

        try{
            testGame.play();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
