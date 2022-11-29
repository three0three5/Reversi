
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Board {
    private boolean highlight = false;
    private int skips = 0;
    private Stack<PairInt> moves = new Stack<>();
    private BoardDiff previousBoards = new BoardDiff();
    private boolean whiteToMove = true;
    private char[][] fields;
    private int opponent = 0;
    private HashSet<PairInt> whiteStones = new HashSet<>();
    private HashSet<PairInt> blackStones = new HashSet<>();
    private HashSet<PairInt> whitePossibleMoves = new HashSet<>();
    private HashSet<PairInt> blackPossibleMoves = new HashSet<>();

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
        whitePossibleMoves.add(new PairInt(3, 5));
        whitePossibleMoves.add(new PairInt(2, 4));
        whitePossibleMoves.add(new PairInt(4, 2));
        whitePossibleMoves.add(new PairInt(5, 3));
        blackPossibleMoves.add(new PairInt(3, 2));
        blackPossibleMoves.add(new PairInt(2, 3));
        blackPossibleMoves.add(new PairInt(5, 4));
        blackPossibleMoves.add(new PairInt(4, 5));
    }

    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }

    public boolean isRunning() {
        if (skips == 2) {
            return false;
        }
        return whiteStones.size() != 0 && blackStones.size() != 0;
    }

    public PairInt getScore() {
        return new PairInt(whiteStones.size(), blackStones.size());
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

    private ArrayList<PairInt> flipPieces(int x, int y, int directionInt) {
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        ArrayList<PairInt> flipped = new ArrayList<>();
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
        return flipped;
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

    private static boolean isBoardCoords(int x, int y) {
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

    private void placePiece(int x, int y) {
        previousBoards.previousMove.push(new PairInt(x, y));
        for (int i = 0; i < 9; ++i) {
            if (isLine(x, y, i)) {
                previousBoards.flippedPieces.push(flipPieces(x, y, i));
            }
        }
        recountPossibleMoves();
    }

    private void cancelMove() {

    }
    private void askHuman(Scanner sc) {
        if (whiteToMove) {
            System.out.print("ПЛЮСЫ");
        } else {
            System.out.print("МИНУСЫ");
        }
        System.out.println(ConstStrings.YOUR_MOVE);
        String position = sc.next();
        if ("P".equals(position)) {
            System.out.println("Включаем подсветку...");
            highlight = true;
            whiteToMove = !whiteToMove;
            return;
        } else if ("NP".equals(position)) {
            System.out.println("Выключаем подсветку...");
            highlight = false;
            whiteToMove = !whiteToMove;
            return;
        } else if ("cancel".equals(position)) {
            cancelMove();
            return;
        }
        while (!isCorrectCoords(position) || !isCorrectMove(position)) {
            System.out.println(ConstStrings.WRONG_MOVE);
            position = sc.next();
        }
        int x_coord = position.charAt(0) - 'A';
        int y_coord = position.charAt(1) - '1';
        placePiece(x_coord, y_coord);
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
        if (opponent == 0) {
            askHuman(sc);
        }
        whiteToMove = !whiteToMove;
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
                    System.out.print("P ");
                    continue;
                }
                System.out.print(fields[i][j] + " ");
            }
            System.out.println();
        }
    }
}
