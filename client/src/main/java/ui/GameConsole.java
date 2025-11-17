package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class GameConsole {
    private static ChessBoard board = new ChessBoard();
    public static void runGame(){
        board.resetBoard();

        System.out.println(" A B C D E F G H ");
        for (int i = 0; i < 8; i++){
            System.out.print((8 - i) + " ");

            for (int j = 0; j < 8; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if ((i+j)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + helperFunctionPieceColor(piece) + pieceType(piece));
                }
                else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + helperFunctionPieceColor(piece) + pieceType(piece));
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }
        System.out.println(" A B C D E F G H ");

        System.out.println(" H G F E D C B A ");
        for (int i = 0; i < 8; i++){
            System.out.print(i + " ");

            for (int j = 0; j < 8; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if ((i+8-j)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + helperFunctionPieceColor(piece) + pieceType(piece));
                }
                else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + helperFunctionPieceColor(piece) + pieceType(piece));
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }
        System.out.println(" H G F E D C B A ");

    }


    private static String helperFunctionPieceColor(ChessPiece piece){
        if (piece == null){
            return "";
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return EscapeSequences.SET_TEXT_COLOR_RED;
        }
        else{
            return EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
    }
    private static String pieceType(ChessPiece piece){
        if (piece == null){
            return " ";
        }
        String pieceLetter = null;
        switch (piece.getPieceType()){
            case KING:
                pieceLetter = "K";
                break;
            case QUEEN:
                pieceLetter = "Q";
                break;
            case BISHOP:
                pieceLetter = "B";
                break;
            case KNIGHT:
                pieceLetter = "N";
                break;
            case ROOK:
                pieceLetter = "R";
                break;
            case PAWN:
                pieceLetter = "P";
                break;
        }
        return " " + pieceLetter + " ";
    }
}
