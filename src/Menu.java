import java.util.Scanner;
public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static int bestScore = -1;
    private static int bestWhiteScore = -1;
    private static int bestBlackScore = -1;
    private static final Board board = new Board();

    private Menu() {}

    public static void setBestScore(int score, int color) {
        switch (color) {
            case 1 -> {
                if (bestScore < score) bestScore = score;
            }
            case 2 -> {
                if (bestWhiteScore < score) bestWhiteScore = score;
            }
            case 3 -> {
                if (bestBlackScore < score) bestBlackScore = score;
            }
        }
    }
    private static int optionsCheck(String option, int numberOfPoints) {
        for (int i = 1; i <= numberOfPoints; ++i) {
            if (Integer.toString(i).equals(option)) {
                return i;
            }
        }
        return 0;
    }

    private static int pageSwitch(String pageText, int numberOfPoints) {
        System.out.print(pageText);
        String option = sc.next();
        int point;
        while ((point = optionsCheck(option, numberOfPoints)) == 0) {
            System.out.print(ConstStrings.OPTIONS_WARNING + pageText);
            option = sc.next();
        }
        return point;
    }
    private static int gameFirstPage() {
        return switch (pageSwitch(ConstStrings.GAME_PAGE_TEXT, 4)) {
            case 1 -> 4; // chose game with no bot
            case 2 -> 5; // chose game with easy bot
            case 3 -> 6; // chose game with hard bot
            default -> 1;
        };
    }
    private static void scorePage() {
        System.out.print(ConstStrings.PLAYER_SCORE);
        System.out.println(bestScore == -1 ? ConstStrings.NO_SCORE : bestScore);
        System.out.print(ConstStrings.WHITE_SCORE);
        System.out.println(bestWhiteScore == -1 ? ConstStrings.NO_SCORE : bestWhiteScore);
        System.out.print(ConstStrings.BLACK_SCORE);
        System.out.println(bestBlackScore == -1 ? ConstStrings.NO_SCORE : bestBlackScore);
        System.out.println(ConstStrings.SCORES_PAGE_TEXT);
        sc.next();
    }
    private static int mainPage() {
        return switch (pageSwitch(ConstStrings.GREETING, 3)) {
            case 1 -> 2; // chose begin game
            case 2 -> 3; // chose best result
            default -> 0; // chose exit
        };
    }
    public static void start() {
        int state = mainPage();
        while (state != 0) {
            switch (state) {
                case 1 -> state = mainPage();
                case 2 -> state = gameFirstPage();
                case 3 -> {
                    state = 1;
                    scorePage();
                }
                case 4 -> {
                    state = 1;
                    board.startGame(1);
                }
                case 5 -> {
                    state = 1;
                    board.startGame(2);
                }
                case 6 -> {
                    state = 1;
                    board.startGame(3);
                }
            }
        }
        System.out.println(ConstStrings.FAREWELL);
    }
}
