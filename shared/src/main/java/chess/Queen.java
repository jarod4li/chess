package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Queen implements PieceMovesCalculator {

    private final ChessMoveUtility moveUtility;

    public Queen() {
        this.moveUtility = new ChessMoveUtility(); // instantiate the utility class
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var validMoves = new ArrayList<ChessMove>();
        int currRow = position.getRow();
        int currCol = position.getColumn();

        // vertical and horizontal
        checkAndAddMoves(validMoves, board, position, currRow, 1, 0, 8);   // up
        checkAndAddMoves(validMoves, board, position, currRow, -1, 0, 8);  // down
        checkAndAddMoves(validMoves, board, position, currRow, 0, 1, 8);   // right
        checkAndAddMoves(validMoves, board, position, currRow, 0, -1, 8);  // left

        // diagonals
        checkAndAddMoves(validMoves, board, position, currRow, 1, 1, 8);    // up-right
        checkAndAddMoves(validMoves, board, position, currRow, 1, -1, 8);   // up-left
        checkAndAddMoves(validMoves, board, position, currRow, -1, 1, 8);   // down-right
        checkAndAddMoves(validMoves, board, position, currRow, -1, -1, 8);  // down-left

        return validMoves;
    }

    void checkAndAddMoves(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition position,
                          int startRow, int rowStep, int colStep, int limit) {
        for (int i = 1; i <= limit; i++) {
            int nextRow = startRow + i * rowStep;
            int nextCol = position.getColumn() + i * colStep;

            if (nextRow < 1 || nextRow > 8 || nextCol < 1 || nextCol > 8) {
                break; // exit loop if out of bounds
            }

            var nextPosition = new ChessPosition(nextRow, nextCol);
            boolean canContinue = moveUtility.helperFunction(moves, board, position, nextPosition, true);

            if (!canContinue) {
                break;
            }
        }
    }
}
