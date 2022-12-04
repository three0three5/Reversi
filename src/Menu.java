import java.util.Scanner;

/**
 * Menu - класс процедура с точкой входа start()
 */
public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static int bestScore = -1;
    private static int bestWhiteScore = -1;
    private static int bestBlackScore = -1;
    private static int skinNumber = 1;
    private static Board board;

    private Menu() { // этот класс не нуждается в экземплярах
    }

    private static int optionsCheck(String option, int numberOfPoints) {
        for (int i = 1; i <= numberOfPoints; ++i) {
            if (Integer.toString(i).equals(option)) {
                return i;
            }
        }
        return 0;
    }

    private static int optionsGet(int numberOfPoints) {
        int point;
        String option = sc.next();
        while ((point = optionsCheck(option, numberOfPoints)) == 0) {
            System.out.println(ConstStrings.OPTIONS_WARNING);
            option = sc.next();
        }
        return point;
    }

    private static int pageSwitch(String pageText, int numberOfPoints) {
        System.out.print(pageText);
        return optionsGet(numberOfPoints);
    }

    private static int gameFirstPage() {
        return switch (pageSwitch(ConstStrings.GAME_PAGE_TEXT, 4)) {
            case 1 -> 4; // chose game with no bot
            case 2 -> 5; // chose game with easy bot
            case 3 -> 6; // chose game with hard bot
            default -> 1;
        };
    }

    private static int skinSelect() {
        return pageSwitch(ConstStrings.CHOOSE_SKIN, 3) - 1;
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

    public static boolean getBotColor() {
        return pageSwitch(ConstStrings.CHOOSE_COLOR, 2) != 1;
    }

    private static void finishGame(int opponent) {
        if (board.wasExit()) {
            return;
        }
        PairInt result = board.getScore();
        String winner = result.x > result.y ? "Белые" : "Черные";
        if (result.x == result.y) {
            winner = "Дружба";
        }
        System.out.print(ConstStrings.FINISHER + winner + "!");
        System.out.println();
        if (opponent == 0) {
            bestBlackScore = Math.max(bestBlackScore, result.y);
            bestWhiteScore = Math.max(bestWhiteScore, result.x);
        } else {
            if (board.getHumanColor()) {
                bestScore = Math.max(bestScore, result.x);
            } else {
                bestScore = Math.max(bestScore, result.y);
            }
        }
    }

    private static void startGame(int opponent) {
        board = new Board();
        board.setSkin(ConstStrings.SKINS[skinNumber]);
        board.setOpponent(opponent);
        Bot bot = new EasyBot();
        if (opponent != 0) {
            board.setBotColor(getBotColor());
            if (opponent == 2) {
                bot = new HardBot();
            }
            board.setBot(bot);
        }
        while (board.isRunning()) {
            board.makeMove(sc);
        }
        finishGame(opponent);
    }

    private static int mainPage() {
        return switch (pageSwitch(ConstStrings.GREETING, 4)) {
            case 1 -> 2; // chose begin game
            case 2 -> 3; // chose best result
            case 3 -> 0;
            case 4 -> 7; // chose skins
            default -> 0; // chose exit
        };
    }

    /**
     * Метод для переключения состояний меню
     * 1 - заглавная страница
     * 2 - страница для выбора оппонента
     * 3 - страница счета
     * 4, 5, 6 - начать игру с человеком или легким/сложным ботом
     * 7 - выбрать скин для фигур
     */
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
                    startGame(0);
                }
                case 5 -> {
                    state = 1;
                    startGame(1);
                }
                case 6 -> {
                    state = 1;
                    startGame(2);
                }
                case 7 -> {
                    skinNumber = skinSelect();
                    state = 1;
                }
            }
        }
        System.out.println(ConstStrings.FAREWELL);
    }
}
