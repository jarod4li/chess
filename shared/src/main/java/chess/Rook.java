package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook implements PieceMovesCalculator {

    private final ChessMoveUtility moveUtility;

    public Rook() {
        this.moveUtility = new ChessMoveUtility(); // instantiate your utility
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var validMoves = new ArrayList<ChessMove>();

        checkAndAddMoves(validMoves, board, position, 1, 0, 8);   // up
        checkAndAddMoves(validMoves, board, position, 0, -1, 8);  // left
        checkAndAddMoves(validMoves, board, position, -1, 0, 8);  // down
        checkAndAddMoves(validMoves, board, position, 0, 1, 8);   // right

        return validMoves;
    }

    void checkAndAddMoves(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition position, int rowStep, int colStep, int limit) {
        for (int i = 1; i <= limit; i++) {
            int nextRow = position.getRow() + i * rowStep;
            int nextCol = position.getColumn() + i * colStep;

            if (nextRow < 1 || nextRow > 8 || nextCol < 1 || nextCol > 8) {
                break; // exit loop if out of bounds
            }

            var nextPosition = new ChessPosition(nextRow, nextCol);
            // Use the utility class instead of the local helperFunction
            if (!moveUtility.helperFunction(moves, board, position, nextPosition, true)) {
                break;
            }
        }
    }
}
