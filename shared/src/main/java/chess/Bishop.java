package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop implements PieceMovesCalculator {

    private final ChessMoveUtility moveUtility;

    public Bishop() {
        this.moveUtility = new ChessMoveUtility(); // instantiate the utility class
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var validMoves = new ArrayList<ChessMove>();
        int currRow = position.getRow();
        int currCol = position.getColumn();

        boolean upRight = true;
        boolean upLeft = true;
        boolean downRight = true;
        boolean downLeft = true;

        for (int i = 1; i <= 8; i++) {
            if (currRow + i <= 8 && currCol + i <= 8) {
                var nextPosition = new ChessPosition(currRow + i, currCol + i);
                upRight = moveUtility.helperFunction(validMoves, board, position, nextPosition, upRight);
            }
            if (currRow + i <= 8 && currCol - i >= 1) {
                var nextPosition = new ChessPosition(currRow + i, currCol - i);
                upLeft = moveUtility.helperFunction(validMoves, board, position, nextPosition, upLeft);
            }
            if (currRow - i >= 1 && currCol + i <= 8) {
                var nextPosition = new ChessPosition(currRow - i, currCol + i);
                downRight = moveUtility.helperFunction(validMoves, board, position, nextPosition, downRight);
            }
            if (currRow - i >= 1 && currCol - i >= 1) {
                var nextPosition = new ChessPosition(currRow - i, currCol - i);
                downLeft = moveUtility.helperFunction(validMoves, board, position, nextPosition, downLeft);
            }
        }
        return validMoves;
    }
}
