import java.util.HashSet;

public class HardBot extends AbstractBot implements Bot {
    /**
     * Метод для каждого перебираемого хода симулирует ответ со стороны лёгкого бота
     * и затем откатывает изменения незаметно для пользователя
     * @return Возвращает лучший ответ в формате PairInt с точки зрения "продвинутого" алгоритма
     */
    private PairInt bestAnswer() {
        Bot easyBot = new EasyBot();
        PairInt bestPiece = moves.iterator().next();
        double bestCost = -1000;
        HashSet<PairInt> currentMoves = new HashSet<>();
        for (PairInt x : moves) {
            currentMoves.add(new PairInt(x));
        }
        for (PairInt x : currentMoves) {
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
