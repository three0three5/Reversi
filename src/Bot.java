import java.util.HashSet;

public class Bot {
    private boolean isWhite;
    private char[][] fields;
    private HashSet<PairInt> moves;
    private static boolean isCorner(PairInt move) {
        return move.x == 0 && move.y == 0 || move.x == 7 && move.y == 7 ||
                move.x == 0 && move.y == 7 || move.x == 7 && move.y == 0;
    }
    private static boolean isSide(PairInt move) {
        return move.x == 0 || move.x == 7 || move.y == 0 || move.y == 7;
    }
    public void setSettings(boolean color, char[][] fields, HashSet<PairInt> moves) {
        this.moves = moves;
        this.isWhite = color;
        this.fields = fields;
    }

    private int lineCounter(PairInt move, int directionInt, boolean sideLine) {
        if (directionInt == 4) return 0;
        int x = move.x, y = move.y;
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        int lineLength = 0;
        while (Board.isBoardCoords(x, y) &&
                (isWhite ? fields[x][y] == '-' : fields[x][y] == '+')) {
            x += direction.x;
            y += direction.y;
            if (!sideLine || isSide(new PairInt(x, y))) {
                ++lineLength;
            }
        }
        if (Board.isBoardCoords(x, y) &&
                isWhite ? fields[x][y] != '+' : fields[x][y] != '-') {
            return 0;
        }
        return lineLength;
    }
    private int piecesFlipped(PairInt move, boolean onlyOnSide) {
        int result = 0;
        for (int i = 0; i < 8; ++i) {
            if (i == 4) {
                continue;
            }
            result += lineCounter(move, i, onlyOnSide);
        }
        return result;
    }
    private double findCost(PairInt move) {
        double sum = 0;
        if (isCorner(move)) {
            sum += 0.8;
        } else if (isSide(move)) {
            sum += 0.4;
        }
        sum += piecesFlipped(move, true);
        sum += piecesFlipped(move, false);
        return sum;
    }
    private PairInt simpleAnswer() {
        if (moves.isEmpty()) {
            return new PairInt(-1, -1);
        }
        PairInt bestPiece = moves.iterator().next();
        double bestCost = findCost(bestPiece);
        for (PairInt x: moves) {
            double cost = findCost(x);
            if (cost > bestCost) {
                bestCost = cost;
                bestPiece = x;
            }
        }
        return bestPiece;
    }

    private PairInt bestAnswer() {
        return moves.iterator().next();
    }
    public PairInt askBot(String level) {
        if ("easy".equals(level)) {
            return simpleAnswer();
        } else {
            return bestAnswer();
        }
    }
}
