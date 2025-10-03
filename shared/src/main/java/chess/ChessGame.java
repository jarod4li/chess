package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.team = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
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
        if (board.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        ChessBoard boardCopy = new ChessBoard(board);

        var valid = new ArrayList<ChessMove>();

        for (ChessMove move: moves){
            var piece = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);

            // check if that is in check
            if (!isInCheck(piece.teamColor)){
                valid.add(move);
            }
            board = new ChessBoard(boardCopy);
        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at starting position: " + move.getStartPosition());
        }

        var teamPlaying = piece.getTeamColor();
        var moves = validMoves(move.getStartPosition());

        if (!moves.contains(move)) {
            throw new InvalidMoveException("Invalid move: " + move);
        }
        if (!teamPlaying.equals(getTeamTurn())) {
            throw new InvalidMoveException("It's not your turn!");
        }
        if (isInStalemate(teamPlaying)) {
            throw new InvalidMoveException("This is a stalemate!");
        }
        // if (isInCheckmate(teamPlaying)) throw new InvalidMoveException("This is a checkmate!");

        var newPiece = (move.getPromotionPiece() == null)
                ? piece
                : new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());

        board.addPiece(move.getEndPosition(), newPiece);
        board.addPiece(move.getStartPosition(), null);

        setTeamTurn(teamPlaying == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        var enemyMoves = new ArrayList<ChessMove>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var pos = new ChessPosition(i, j);
                var piece = board.getPiece(pos);
                if (piece == null) continue;

                if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = pos;
                } else if (piece.getTeamColor() != teamColor) {
                    enemyMoves.addAll(piece.pieceMoves(board, pos));
                }
            }
        }

        if (kingPosition == null) return false;

        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }

        return false;
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

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()) {
                        return false; // Found at least one valid move → not checkmate
                    }
                }
            }
        }

        return true; // No valid moves while in check → checkmate
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        var friendMoves = new ArrayList<ChessMove>();
        if (!isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                        friendMoves.addAll(validMoves(new ChessPosition(i, j)));
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
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }
}
