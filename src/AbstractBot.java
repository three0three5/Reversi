import java.util.HashSet;

public abstract class AbstractBot {
    protected boolean isWhite;
    protected Board board;
    protected HashSet<PairInt> moves;
    protected double costOfLastMove = 0;
    protected char[] skin;

    public void setBoard(boolean color, Board board, HashSet<PairInt> moves) {
        this.moves = moves;
        this.isWhite = color;
        this.board = board;
        this.skin = board.getSkin();
    }

    public double getLastCost() {
        return costOfLastMove;
    }
    protected static boolean isCorner(PairInt move) {
        return move.x == 0 && move.y == 0 || move.x == 7 && move.y == 7 ||
                move.x == 0 && move.y == 7 || move.x == 7 && move.y == 0;
    }
    protected static boolean isSide(PairInt move) {
        return move.x == 0 || move.x == 7 || move.y == 0 || move.y == 7;
    }
    protected int lineCounter(PairInt move, int directionInt, boolean sideLine) {
        if (directionInt == 4) return 0;
        int x = move.x, y = move.y;
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        int lineLength = 0;
        x += direction.x;
        y += direction.y;
        while (Board.isBoardCoords(x, y) &&
                (isWhite ? board.getFields(x, y) == skin[1] : board.getFields(x, y) == skin[0])) {
            if (!sideLine || isSide(new PairInt(x, y))) {
                ++lineLength;
            }
            x += direction.x;
            y += direction.y;
        }
        if (!Board.isBoardCoords(x, y) ||
                (isWhite ? board.getFields(x, y) != skin[0] : board.getFields(x, y) != skin[1])) {
            return 0;
        }
        return lineLength;
    }
    protected int piecesFlipped(PairInt move, boolean onlyOnSide) {
        int result = 0;
        for (int i = 0; i < 9; ++i) {
            if (i == 4) {
                continue;
            }
            result += lineCounter(move, i, onlyOnSide);
        }
        return result;
    }
    protected double findCost(PairInt move) {
        if (move.x == -1) {
            return 0;
        }
        double sum = 0;
        if (isCorner(move)) {
            sum += 0.8;
        } else if (isSide(move)) {
            sum += 0.4;
        }
        int onSide = piecesFlipped(move, true);
        int flip = piecesFlipped(move, false);
        return sum + onSide + flip;
    }
}
