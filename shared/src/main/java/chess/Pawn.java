package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> validMoves = new ArrayList<>();
        int currRow = position.getRow();
        int currCol = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        if (piece == null) return validMoves;

        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        int promotionRow = isWhite ? 8 : 1;

        ChessPosition forward = new ChessPosition(currRow + direction, currCol);
        if (board.getPiece(forward) == null) {
            addMove(validMoves, position, forward, promotionRow);

            if (currRow == startRow) {
                ChessPosition doubleStep = new ChessPosition(currRow + 2 * direction, currCol);
                if (board.getPiece(doubleStep) == null) {
                    validMoves.add(new ChessMove(position, doubleStep));
                }
            }
        }

        for (int offset : new int[]{-1, 1}) {
            ChessPosition diagonal = new ChessPosition(currRow + direction, currCol + offset);
            if (diagonal.getColumn() >= 1 && diagonal.getColumn() <= 8) {
                ChessPiece target = board.getPiece(diagonal);
                if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                    addMove(validMoves, position, diagonal, promotionRow);
                }
            }
        }

        return validMoves;
    }

    private void addMove(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, int promotionRow) {
        if (to.getRow() == promotionRow) {
            for (ChessPiece.PieceType promotionType : List.of(
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.QUEEN)) {
                moves.add(new ChessMove(from, to, promotionType));
            }
        } else {
            moves.add(new ChessMove(from, to));
        }
    }
}