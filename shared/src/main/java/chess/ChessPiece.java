package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor teamColor;
    ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (pieceType == PieceType.BISHOP){
            var validMoves = new Bishop();
            return validMoves.pieceMoves(board, myPosition);
        }
        else if (pieceType == PieceType.KING){
            var validMoves = new King();
            return validMoves.pieceMoves(board, myPosition);
        }
        else if (pieceType == PieceType.KNIGHT){
            var validMoves = new Knight();
            return validMoves.pieceMoves(board, myPosition);
        }
        else if (pieceType == PieceType.PAWN){
            var validMoves = new Pawn();
            return validMoves.pieceMoves(board, myPosition);
        }
        else if (pieceType == PieceType.QUEEN){
            var validMoves = new Queen();
            return validMoves.pieceMoves(board, myPosition);
        }
        else if (pieceType == PieceType.ROOK){
            var validMoves = new Rook();
            return validMoves.pieceMoves(board, myPosition);
        }
        return null;
    }
}