import java.util.*;
import java.io.*;

public class winner {

    // card values
    private static final int A = 1;
    private static final int CLUB = 1;
    private static final int DIAMOND = 2;
    private static final int HEART = 3;
    private static final int SPADE = 4;
    private static final int J = 11;
    private static final int Q = 12;
    private static final int K = 13;


    // keeps the information about the players
    static class Player {
        private String name;
        private int cardScore;
        private int suitScore;

        // constructor
        public Player(String name, int cardScore, int suitScore) {
            this.name = name;
            this.cardScore = cardScore;
            this.suitScore = suitScore;
        }

        // getters and setters
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getCardScore() {
            return cardScore;
        }
        public void setCardScore(int cardScore) {
            this.cardScore = cardScore;
        }
        public int getSuitScore() {
            return suitScore;
        }
        public void setSuitScore(int suitScore) {
            this.suitScore = suitScore;
        }
    }


    // retrveie the value of the card
    public static int getCardValue(String cardLetter) {
        int cardValue = 0;
        switch (cardLetter) {
            case "A":
                cardValue = A;
                break;
            case "J":
                cardValue = J;
                break;
            case "Q":
                cardValue = Q;
                break;
            case "K":
                cardValue = K;
                break;
            default:
                try {
                    cardValue = Integer.parseInt(cardLetter);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid card value " + cardLetter);
                    System.exit(0);
                }
        }
        return cardValue;
    }

    // retrieve the suit of the card
    public static int getCardSuit(String cardSuit) {
        int cardSuitValue = 0;
        switch (cardSuit) {
            case "C":
                cardSuitValue = CLUB;
                break;
            case "D":
                cardSuitValue = DIAMOND;
                break;
            case "H":
                cardSuitValue = HEART;
                break;
            case "S":
                cardSuitValue = SPADE;
                break;
            default:
                System.out.println("Invalid card suit " + cardSuit);
                System.exit(0);
        }
        return cardSuitValue;
    }


    // reads the file and constructs the ojects of the player class
    public static List<Player> readFile(String filename) {
        // list of players
        List<Player> players = new ArrayList<Player>();

        // read the file
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    System.out.println("Error: invalid line in file");
                    System.exit(0);
                }

                String playerName = parts[0];
                String[] cards = parts[1].split(",");
                if (cards.length != 5) {
                    System.out.println("Error: invalid line in file");
                    System.exit(0);
                }

                int[] cardValues = new int[cards.length];
                int[] cardSuits = new int[cards.length];
                for (int i = 0; i < cards.length; i++) {
                    int length = cards[i].length();
                    String cardValue = cards[i].substring(0, length - 1);
                    String cardSuit = cards[i].substring(length - 1);
                    cardValues[i] = getCardValue(cardValue);
                    cardSuits[i] = getCardSuit(cardSuit);
                }

                int cardScore = 0;
                int suitScore = 0;
                for (int i = 0; i < cards.length; i++) {
                    cardScore += cardValues[i];
                    suitScore += cardSuits[i];
                }

                Player player = new Player(playerName, cardScore, suitScore);
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }

        return players;
    }

    // finds the list of winners
    public static List<Player> printWinner(List<Player> players) {
        // find the maximum card score and suit score
        int maxCardScore = 0;
        int maxSuitScore = 0;
        for (Player player : players) {
            if (player.getCardScore() > maxCardScore) {
                maxCardScore = player.getCardScore();
                maxSuitScore = player.getSuitScore();
            } else if (player.getCardScore() == maxCardScore) {
                if (player.getSuitScore() > maxSuitScore) {
                    maxSuitScore = player.getSuitScore();
                }
            }
        }

        // find the list of winners
        List<Player> winners = new ArrayList<Player>();
        for (Player player : players) {
            if (player.getCardScore() == maxCardScore && player.getSuitScore() == maxSuitScore) {
                winners.add(player);
            }
        }

        // return the list of winners
        return winners;
    }

    // prints the winners
    public static void writeWinners(List<Player> winners, String outfilename) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(outfilename));
            out.println("Winner Name, Card Score, Suit Score");
            for (Player player : winners) {
                out.println(player.getName() + ", " + player.getCardScore() + ", " + player.getSuitScore());
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error: unable to write to file");
            System.exit(0);
        }
    }

    public static void main(String[] args) {

        // validate the number of arguments
        if (args.length != 4) {
            System.out.println("Error: invalid number of arguments");
            System.exit(0);
        }

        // check if the arguments are valid
        if (!args[0].equals("--in") && !args[0].equals("--out")) {
            System.out.println("Error: invalid argument " + args[0]);
            System.exit(0);
        }

        // get input and output filenames based on the arguments
        String filename, outfilename;
        if (args[0].equals("--in")) {
            filename = args[1];
            outfilename = args[3];
        } else {
            filename = args[3];
            outfilename = args[1];
        }

        // process input file and write output
        List<Player> players = readFile(filename);
        List<Player> winners = printWinner(players);
        writeWinners(winners, outfilename);
    }
}