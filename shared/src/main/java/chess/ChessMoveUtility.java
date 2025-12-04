package chess;

import java.util.Collection;

public class ChessMoveUtility {

    public boolean helperFunction(Collection<ChessMove> validMoves, ChessBoard board,
                                  ChessPosition position, ChessPosition nextPosition,
                                  boolean keepGoing) {

        if (keepGoing) {
            if (board.getPiece(nextPosition) == null) {
                validMoves.add(new ChessMove(position, nextPosition));
                return true;
            } else if (board.getPiece(nextPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validMoves.add(new ChessMove(position, nextPosition));
                return false;
            } else {
                return false;
            }
        }

        return false;
    }
}
