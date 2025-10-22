package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight implements PieceMovesCalculator {

    private final SimpleMoveUtility moveUtility;

    public Knight() {
        this.moveUtility = new SimpleMoveUtility();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var validMoves = new ArrayList<ChessMove>();
        int currRow = position.getRow();
        int currCol = position.getColumn();

        // All 8 possible knight moves
        int[][] moves = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] move : moves) {
            int newRow = currRow + move[0];
            int newCol = currCol + move[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition nextPosition = new ChessPosition(newRow, newCol);
                moveUtility.addMoveIfValid(validMoves, board, position, nextPosition);
            }
        }

        return validMoves;
    }
}
