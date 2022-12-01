import java.util.HashSet;
import java.util.Random;
public class Bot {
    private boolean isWhite;
    private final Random rand = new Random();
    private Board board;
    private HashSet<PairInt> moves;
    private static boolean isCorner(PairInt move) {
        return move.x == 0 && move.y == 0 || move.x == 7 && move.y == 7 ||
                move.x == 0 && move.y == 7 || move.x == 7 && move.y == 0;
    }
    private static boolean isSide(PairInt move) {
        return move.x == 0 || move.x == 7 || move.y == 0 || move.y == 7;
    }
    public void setBoard(boolean color, Board board, HashSet<PairInt> moves) {
        this.moves = moves;
        this.isWhite = color;
        this.board = board;
    }

    private int lineCounter(PairInt move, int directionInt, boolean sideLine) {
        if (directionInt == 4) return 0;
        int x = move.x, y = move.y;
        PairInt direction = new PairInt(directionInt % 3 - 1, directionInt / 3 - 1);
        int lineLength = 0;
        x += direction.x;
        y += direction.y;
        while (Board.isBoardCoords(x, y) &&
                (isWhite ? board.getFields(x, y) == '-' : board.getFields(x, y) == '+')) {
            if (!sideLine || isSide(new PairInt(x, y))) {
                ++lineLength;
            }
            x += direction.x;
            y += direction.y;
        }
        if (!Board.isBoardCoords(x, y) ||
                (isWhite ? board.getFields(x, y) != '+' : board.getFields(x, y) != '-')) {
            return 0;
        }
        return lineLength;
    }
    private int piecesFlipped(PairInt move, boolean onlyOnSide) {
        int result = 0;
        for (int i = 0; i < 9; ++i) {
            if (i == 4) {
                continue;
            }
            result += lineCounter(move, i, onlyOnSide);
        }
        return result;
    }
    private double findCost(PairInt move) {
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
            } else if (bestCost == cost && rand.nextDouble() > 0.5) {
                bestCost = cost;
                bestPiece = x;
            }
        }
        //System.out.println("Cost of this move: " + bestCost + " for " + bestPiece.x + " " + bestPiece.y);
        return bestPiece;
    }

    private PairInt bestAnswer() {
        PairInt bestPiece = moves.iterator().next();
        double bestCost = findCost(bestPiece);
        HashSet<PairInt> currentMoves = new HashSet<>();
        for (PairInt x: moves) {
            currentMoves.add(new PairInt(x));
        }
        for (PairInt x: currentMoves) {
            double currentCost = findCost(x);
            double opponentCost;
            board.placePiece(x.x, x.y);
            if ((board.getCurrentColor() == isWhite) || !board.isRunning()) {
                opponentCost = 0;
            } else {
                opponentCost = findCost(simpleAnswer());
            }
            currentCost -= opponentCost;
            if (currentCost > bestCost) {
                bestCost = currentCost;
                bestPiece = x;
            }
            board.cancelMove();
        }
        return bestPiece;
    }
    public PairInt askBot(String level) {
        if ("easy".equals(level)) {
            return simpleAnswer();
        } else {
            return bestAnswer();
        }
    }
}
