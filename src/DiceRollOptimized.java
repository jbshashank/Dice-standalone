import java.util.Scanner;  // Import the Scanner class
import java.util.stream.*;
import java.util.*;

public class DiceRollOptimized {
    public static int maxScore = 6;
    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);
        int numberOfPlayers = sc.nextInt();
        int pointsToWin = sc.nextInt();

        if(numberOfPlayers <1 ){System.out.println("we need atleast one player to play");}
        if(pointsToWin <1 ){System.out.println("Winning score must be atleast 1");}

        int currentRollScore = 0;
        boolean playCompleted = false;

        Map<Integer, CurrentPlrStanding> playerInfo = new HashMap<>();
        Map<Integer,String> ranks = new LinkedHashMap<>();
        List<Integer> randomPlayers = IntStream.range(1, numberOfPlayers + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(randomPlayers);


        while (!playCompleted) {
            for (int i = 0; i < randomPlayers.size(); i++) {
                int player = randomPlayers.get(i);
                System.out.println("Player " + player + ",Its your turn, roll dice by pressing 'r'");
                char input = sc.next().charAt(0);
                while(input != 'r') {
                    System.out.println("Player " + player + ",Its your turn, roll dice by pressing 'r'");
                }
                    currentRollScore = rollDice();
                    System.out.println("You rolled "+currentRollScore);
                    CurrentPlrStanding currentPlayerStanding = playerInfo.containsKey(player) ? playerInfo.get(player) : new CurrentPlrStanding();

                    if (currentPlayerStanding.totalScoreTillNow + currentRollScore> pointsToWin  ) {
                        System.out.println("Player " + player + " Won! ");

                        randomPlayers.remove(randomPlayers.indexOf(player));
                        playerInfo.remove(player);
                        ranks.put(ranks.size()+1,"Player "+player);
                        i--;
                    } else {
                        if (currentPlayerStanding.previousScores.size() == 2 && (currentPlayerStanding.previousScores.get(currentPlayerStanding.previousScores.size() - 1) == 1 && currentPlayerStanding.previousScores.get(currentPlayerStanding.previousScores.size() - 2) == 1)) {
                            currentPlayerStanding.previousScores.clear();
                            break;
                        } else if (currentPlayerStanding.previousScores.size() == 2) {
                            currentPlayerStanding.previousScores.remove(0);
                        }
                        currentPlayerStanding.previousScores.add(currentRollScore);
                        currentPlayerStanding.totalScoreTillNow = currentPlayerStanding.totalScoreTillNow + currentRollScore;
                        currentPlayerStanding.totalAttempts++;
                        while (currentRollScore == maxScore && currentPlayerStanding.totalScoreTillNow < pointsToWin) {
                            i--;
                            break;
                        }
                        playerInfo.put(player, currentPlayerStanding);
                    }


                if (playerInfo.size() == 0) {
                    System.out.println("Play Completed!");
                    playCompleted = true;
                    printRankings(ranks);
                } else {
                    printCurrentScore(playerInfo);
                }
            }
        }
    }

    static int rollDice() {
        return (int)(Math.random()*6) + 1;
        //return 1;
    }

    static void printCurrentScore(Map<Integer, CurrentPlrStanding> playerInfo){
        System.out.println("The current score are:");
        String format = "|%1$-15s|%2$-10s|%3$-20s|\n";
        System.out.println(" _______________________________________________");
        System.out.format(format, "PLAYER NAME", "SCORE", "TOTAL ATTEMPTS");
        System.out.println("|_______________________________________________|");
        playerInfo.entrySet().forEach(entry -> {
            System.out.format(format,"PLAYER "+entry.getKey(), entry.getValue().totalScoreTillNow, entry.getValue().totalAttempts);
        });
        System.out.println("|_______________________________________________|");
    }

    static void printRankings(Map<Integer, String> winnerInfo){
        System.out.println("The leaderboard is :");
        String format = "|%1$-10s|%2$-10s|\n";
        System.out.println(" ______________________");
        System.out.format(format, "POSITION", "NAME");
        System.out.println("|_____________________|");
        winnerInfo.entrySet().forEach(entry -> {
            System.out.format(format,entry.getKey(), entry.getValue());
        });
        System.out.println("|_____________________|");
    }
}

class CurrentPlrStanding {
    List<Integer> previousScores = new ArrayList<>();
    long totalScoreTillNow = 0;
    long totalAttempts= 0;
}
