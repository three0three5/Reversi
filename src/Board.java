
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Board {
    private boolean exit = false;
    private boolean highlight = false;
    private int skips = 0;
    private final Stack<PairInt> previousMove = new Stack<>();
    private final Stack<ArrayList<PairInt>> flippedPieces = new Stack<>();
    private final Stack<Boolean> lastMoveByWhite = new Stack<>();
    private boolean whiteToMove = true;
    private final char[][] fields;
    private int opponent = 0;
    private final HashSet<PairInt> whiteStones = new HashSet<>();
    private final HashSet<PairInt> blackStones = new HashSet<>();
    private final HashSet<PairInt> whitePossibleMoves = new HashSet<>();
    private final HashSet<PairInt> blackPossibleMoves = new HashSet<>();
    private boolean botColorIsWhite = false;
    private Bot bot;

    public Board() {
        fields = new char[8][];
        for (int i = 0; i < 8; ++i) {
            fields[i] = new char[8];
            for (int j = 0; j < 8; ++j) {
                fields[i][j] = ' ';
            }
        }
        fields[3][3] = '+';
        fields[4][4] = '+';
        fields[3][4] = '-';
        fields[4][3] = '-';
        whiteStones.add(new PairInt(3, 3));
        whiteStones.add(new PairInt(4, 4));
        blackStones.add(new PairInt(3, 4));
        blackStones.add(new PairInt(4, 3));
        recountPossibleMoves();
    }

    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }
    public void setBotColor(boolean color) {
        botColorIsWhite = color;
    }
    public boolean getHumanColor() {
        return !botColorIsWhite;
    }
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public boolean isRunning() {
        if (skips == 2 || exit) {
            return false;
        }
        return whiteStones.size() != 0 && blackStones.size() != 0;
    }
    public boolean wasExit() {
        return exit;
    }

    public PairInt getScore() {
        return new PairInt(whiteStones.size(), blackStones.size());
    }

    public boolean getCurrentColor() {
        return whiteToMove;
    }

    public char getFields(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return ' ';
        }
        return fields[x][y];
    }
    public void placePiece(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return;
        }
        previousMove.push(new PairInt(x, y));
        lastMoveByWhite.push(whiteToMove);
        ArrayList<PairInt> flipped = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            if (isLine(x, y, i)) {
                flipPieces(x, y, i, flipped);
            }
        }
        flippedPieces.push(flipped);
        recountPossibleMoves();
        if (opponent != 0) {
            if (botColorIsWhite) {
                bot.setBoard(true, this, whitePossibleMoves);
            } else {
                bot.setBoard(false, this, blackPossibleMoves);
            }
        }
        whiteToMove = !whiteToMove;
    }

    public void cancelMove() {
        if (previousMove.isEmpty()) {
            System.out.println("Нечего отменять!");
            return;
        }
        PairInt move = previousMove.pop();
        if (fields[move.x][move.y] == '+') {
            whiteStones.remove(move);
        } else {
            blackStones.remove(move);
        }
        fields[move.x][move.y] = ' ';
        ArrayList<PairInt> flipped = flippedPieces.pop();
        for (PairInt x: flipped) {
            if (fields[x.x][x.y] == '+') {
                fields[x.x][x.y] = '-';
                blackStones.add(new PairInt(x.x, x.y));
                whiteStones.remove(new PairInt(x.x, x.y));
            } else {
                fields[x.x][x.y] = '+';
                whiteStones.add(new PairInt(x.x, x.y));
                blackStones.remove(new PairInt(x.x, x.y));
            }
        }
        recountPossibleMoves();
        whiteToMove = lastMoveByWhite.pop();
        if (opponent != 0) {
            if (botColorIsWhite) {
                bot.setBoard(true, this, whitePossibleMoves);
            } else {
                bot.setBoard(false, this, blackPossibleMoves);
            }
        }
    }

    private static boolean isCorrectCoords(String s) {
        if (s.length() != 2) {
            return false;
        }
        int symbol = s.charAt(0);
        int coord = s.charAt(1);
        if (symbol < 65 || symbol > 72) {
            return false;
        }
        return coord >= 49 && coord <= 56;
    }

    private boolean isCorrectMove(String position) {
        int x = position.charAt(0) - 'A';
        int y = position.charAt(1) - '1';
        if (whiteToMove) {
            return whitePossibleMoves.contains(new PairInt(x, y));
        } else {
            return blackPossibleMoves.contains(new PairInt(x, y));
        }
    }

    private void flipPieces(int x, int y, int directionInt, ArrayList<PairInt> flipped) {
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        if (whiteToMove) {
            fields[x][y] = '+';
            whiteStones.add(new PairInt(x, y));
        } else {
            fields[x][y] = '-';
            blackStones.add(new PairInt(x, y));
        }
        x += direction.x;
        y += direction.y;
        if (whiteToMove) {
            while (fields[x][y] != '+') {
                fields[x][y] = '+';
                whiteStones.add(new PairInt(x, y));
                blackStones.remove(new PairInt(x, y));
                flipped.add(new PairInt(x, y));
                x += direction.x;
                y += direction.y;
            }
        } else {
            while (fields[x][y] != '-') {
                fields[x][y] = '-';
                whiteStones.remove(new PairInt(x, y));
                blackStones.add(new PairInt(x, y));
                flipped.add(new PairInt(x, y));
                x += direction.x;
                y += direction.y;
            }
        }
    }

    private PairInt findTail(int x, int y, int directionInt, boolean forWhite) {
        if (directionInt == 4) {
            return new PairInt(-1, 0);
        }
        int lineLength = 0;
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        x += direction.x;
        y += direction.y;
        while (isBoardCoords(x, y) &&
                (forWhite ? fields[x][y] == '-' : fields[x][y] == '+')) {
            x += direction.x;
            y += direction.y;
            ++lineLength;
        }
        if (isBoardCoords(x, y) && lineLength != 0 && fields[x][y] == ' ') {
            return new PairInt(x, y);
        }
        return new PairInt(-1, 0);
    }

    public static boolean isBoardCoords(int x, int y) {
        return x <= 7 && x >= 0 && y <= 7 && y >= 0;
    }

    private boolean isLine(int x, int y, int directionInt) {
        if (directionInt == 4) {
            return false;
        }
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        x += direction.x;
        y += direction.y;
        int lineLength = 0;
        while (isBoardCoords(x, y)) {
            if (whiteToMove ? fields[x][y] == '-' : fields[x][y] == '+') {
                ++lineLength;
            } else {
                break;
            }
            x += direction.x;
            y += direction.y;
        }
        if (!isBoardCoords(x, y) ||
                (whiteToMove ? fields[x][y] != '+' : fields[x][y] != '-')) {
            return false;
        }
        return lineLength != 0;
    }

    private void recountPossibleMoves() {
        whitePossibleMoves.clear();
        blackPossibleMoves.clear();
        for (PairInt x : whiteStones) {
            for (int i = 0; i < 9; ++i) {
                PairInt possible = findTail(x.x, x.y, i, true);
                if (possible.x != -1) {
                    whitePossibleMoves.add(possible);
                }
            }
        }
        for (PairInt x : blackStones) {
            for (int i = 0; i < 9; ++i) {
                PairInt possible = findTail(x.x, x.y, i, false);
                if (possible.x != -1) {
                    blackPossibleMoves.add(possible);
                }
            }
        }
    }

    private void applyOption(String option) {
        if ("P".equals(option)) {
            System.out.println("Включаем подсветку...");
            highlight = true;
        } else if ("NP".equals(option)) {
            System.out.println("Выключаем подсветку...");
            highlight = false;
        } else if ("cancel".equals(option)) {
            cancelMove();
        } else {
            exit = true;
        }
    }
    private PairInt askHuman(Scanner sc) {
        if (whiteToMove) {
            System.out.print("ПЛЮСЫ");
        } else {
            System.out.print("МИНУСЫ");
        }
        System.out.println(ConstStrings.YOUR_MOVE);
        String position = sc.next();
        while (!isCorrectCoords(position) || !isCorrectMove(position)) {
            if (ConstStrings.GAME_OPTIONS.contains(position)) {
                applyOption(position);
                if (exit) {
                    return new PairInt(-1, 0);
                }
                display();
            } else {
                System.out.println(ConstStrings.WRONG_MOVE);
            }
            position = sc.next();
        }
        int x_coord = position.charAt(0) - 'A';
        int y_coord = position.charAt(1) - '1';
        return new PairInt(x_coord, y_coord);
    }

    public void makeMove(Scanner sc) {
        System.out.println();
        this.display();
        if (whiteToMove && whitePossibleMoves.size() == 0) {
            System.out.println(ConstStrings.NO_MOVES_WHITE);
            ++skips;
            whiteToMove = !whiteToMove;
            return;
        } else if (!whiteToMove && blackPossibleMoves.size() == 0) {
            System.out.println(ConstStrings.NO_MOVES_BLACK);
            ++skips;
            whiteToMove = !whiteToMove;
            return;
        } else {
            skips = 0;
        }
        if (opponent == 0 || whiteToMove != botColorIsWhite) {
            PairInt move = askHuman(sc);
            if (move.x == -1) {
                return;
            }
            placePiece(move.x, move.y);
        } else if (opponent == 1) {
            if (botColorIsWhite) {
                bot.setBoard(true, this, whitePossibleMoves);
            } else {
                bot.setBoard(false, this, blackPossibleMoves);
            }
            PairInt move = bot.askBot("easy");
            char moveX = (char) ('A' + move.x);
            char moveY = (char) ('1' + move.y);
            System.out.print("\nБот ходит: " + moveX + moveY);
            placePiece(move.x, move.y);
        } else if (opponent == 2) {
            if (botColorIsWhite) {
                bot.setBoard(true, this, whitePossibleMoves);
            } else {
                bot.setBoard(false, this, blackPossibleMoves);
            }
            PairInt move = bot.askBot("hard");
            char moveX = (char) ('A' + move.x);
            char moveY = (char) ('1' + move.y);
            System.out.print("\nБот ходит: " + moveX + moveY);
            placePiece(move.x, move.y);
        }
    }

    public void display() {
        System.out.println("Счет: Белые: " + whiteStones.size() + "; Черные: " + blackStones.size());
        System.out.println(ConstStrings.BOARD_TITLE);
        for (int j = 0; j < 8; ++j) {
            System.out.print((j + 1) + " ");
            for (int i = 0; i < 8; ++i) {
                if (highlight &&
                        (whiteToMove ? whitePossibleMoves.contains(new PairInt(i, j)) :
                                blackPossibleMoves.contains(new PairInt(i, j)))) {
                    System.out.print("o ");
                    continue;
                }
                System.out.print(fields[i][j] + " ");
            }
            System.out.println();
        }
    }
}
