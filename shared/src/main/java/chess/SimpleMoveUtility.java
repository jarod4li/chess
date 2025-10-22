package chess;

import java.util.Collection;

public class SimpleMoveUtility {

    /**
     * Adds a move to the collection if the next position is empty or contains an opponent piece.
     */
    public void addMoveIfValid(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition position, ChessPosition nextPosition) {
        ChessPiece targetPiece = board.getPiece(nextPosition);
        ChessPiece currentPiece = board.getPiece(position);

        if (targetPiece == null || targetPiece.getTeamColor() != currentPiece.getTeamColor()) {
            validMoves.add(new ChessMove(position, nextPosition));
        }
    }
}
