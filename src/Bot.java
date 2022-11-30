import java.util.HashSet;

public class Bot {
    private boolean isWhite;
    private char[][] fields;
    private HashSet<PairInt> moves;
    public void setSettings(boolean color, char[][] fields, HashSet<PairInt> moves) {
        this.moves = moves;
        this.isWhite = color;
        this.fields = fields;
    }
    private PairInt simpleAnswer() {
        return moves.iterator().next();
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
