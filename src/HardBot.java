import java.util.HashSet;

public class HardBot extends AbstractBot implements Bot {
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
            }
//            System.out.print("\nFor move " +
//                    (char) (x.x + 'A') + (char) (x.y + '1') +
//                    " with " + currentCost
//                    + " best opponent is " +
//                    (char) (easyBot.askBot().x + 'A') +
//                    (char) (easyBot.askBot().y + '1') + " with " + opponentCost);
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
}
