package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessBoard = new ChessBoard();
    private ChessGame.TeamColor currentTurn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> moves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : moves) {
            ChessPiece startPiece = chessBoard.getPiece(move.getStartPosition());
            ChessPiece endPiece = chessBoard.getPiece(move.getEndPosition());

            chessBoard.addPiece(move.getEndPosition(), startPiece);
            chessBoard.addPiece(move.getStartPosition(), null);

            if (!isInCheck(piece.teamColor)) {
                validMoves.add(move);
            }

            chessBoard.addPiece(move.getStartPosition(), startPiece);
            chessBoard.addPiece(move.getEndPosition(), endPiece);
        }
        return validMoves;
    }




    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("Invalid move: " + move);
        }

        TeamColor teamPlaying = piece.getTeamColor();
        if (!teamPlaying.equals(getTeamTurn())) {
            throw new InvalidMoveException("It's not your turn!");
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (!moves.contains(move)) {
            throw new InvalidMoveException("Invalid move: " + move);
        }

        if (isInStalemate(teamPlaying)) {
            throw new InvalidMoveException("This is a stalemate!");
        }

        ChessPiece endPiece;
        if (move.getPromotionPiece() == null) {
            endPiece = chessBoard.getPiece(move.getStartPosition());
        } else {
            endPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        chessBoard.addPiece(move.getEndPosition(), endPiece);
        chessBoard.addPiece(move.getStartPosition(), null);
        setTeamTurn(teamPlaying == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMoves = piece.pieceMoves(chessBoard, new ChessPosition(i + 1, j + 1));
                    for (ChessMove move : enemyMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(i + 1, j + 1);
                }
            }
        }
        return null;
    }




    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        List<ChessMove> friendMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    friendMoves.addAll(validMoves(new ChessPosition(i + 1, j + 1)));
                }
            }
        }
        for (ChessMove move : friendMoves) {
            ChessBoard hypotheticalBoard = new ChessBoard(chessBoard);
            hypotheticalBoard.addPiece(move.getEndPosition(), hypotheticalBoard.getPiece(move.getStartPosition()));
            hypotheticalBoard.addPiece(move.getStartPosition(), null);
            if (!isInCheck(teamColor)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            List<ChessMove> friendMoves = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece piece = chessBoard.getPiece(new ChessPosition(i + 1, j + 1));
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        friendMoves.addAll(validMoves(new ChessPosition(i + 1, j + 1)));
                    }
                }
            }
            if (friendMoves.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = (ChessBoard) board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
    }
}
