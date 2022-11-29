import java.util.ArrayList;
import java.util.Stack;

public class BoardDiff {
    public Stack<PairInt> previousMove = new Stack<>();
    public Stack<ArrayList<PairInt>> flippedPieces = new Stack<>();
    public Stack<Integer> lastSkips = new Stack<>();
}
