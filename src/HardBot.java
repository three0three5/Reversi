import java.util.HashSet;

public class HardBot implements Bot{
    private boolean isWhite;
    private Board board;
    private HashSet<PairInt> moves;
    private double costOfLastMove = 0;
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
    private PairInt bestAnswer() {
        Bot easyBot = new EasyBot();
        PairInt bestPiece = moves.iterator().next();
        double bestCost = -1000;
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
                easyBot.setBoard(!isWhite, board, board.getMoves(!isWhite));
                easyBot.askBot();
                opponentCost = easyBot.getLastCost();
                System.out.println("Best opponent: " + opponentCost);
            }
            currentCost -= opponentCost;
            if (currentCost > bestCost) {
                bestCost = currentCost;
                bestPiece = x;
            }
            board.cancelMove();
        }
        costOfLastMove = bestCost;
        return bestPiece;
    }
    public PairInt askBot() {
        return bestAnswer();
    }

    @Override
    public double getLastCost() {
        return costOfLastMove;
    }
}
