import java.util.HashSet;

public interface Bot {
    void setBoard(boolean color, Board board, HashSet<PairInt> moves);

    PairInt askBot();

    double getLastCost();
}
