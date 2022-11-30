import java.util.ArrayList;
import java.util.Stack;

public class BoardDiff {
    private final Stack<PairInt> previousMove = new Stack<>();
    private final Stack<ArrayList<PairInt>> flippedPieces = new Stack<>();
    private final Stack<Boolean> lastMoveByWhite = new Stack<>();

    public boolean isEmpty() {
        return previousMove.isEmpty();
    }
    public PairInt popLastMove() {
        return previousMove.pop();
    }

    public ArrayList<PairInt> popFlipped() {
        return flippedPieces.pop();
    }

    public Boolean popLastColor() {
        return lastMoveByWhite.pop();
    }

    public void pushLastMove(PairInt move) {
        previousMove.push(move);
    }

    public void pushFlipped(ArrayList<PairInt> flip) {
        flippedPieces.push(flip);
    }

    public void pushLastColor(boolean isWhite) {
        lastMoveByWhite.push(isWhite);
    }
}
