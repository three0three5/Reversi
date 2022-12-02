public class EasyBot extends AbstractBot implements Bot {
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
        costOfLastMove = bestCost;
        //System.out.println("Cost of this move: " + bestCost + " for " + bestPiece.x + " " + bestPiece.y);
        return bestPiece;
    }
    public PairInt askBot() {
        return simpleAnswer();
    }
}
