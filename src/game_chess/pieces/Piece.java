package game_chess.pieces;

import game_chess.Alliance;
import game_chess.board.Board;
import game_chess.board.Move;

import java.util.Collection;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Abstract Piece class, represent game pieces
 * **/

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    //abstract method to calculate the possible moves for each different chess piece
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    //
    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    //method to compute hashcode
    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove? 1 : 0 );
        return result;
    }

    //method to compare pieces
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }

        final  Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    //method to get piece alliance (black/white)
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public abstract Piece movePiece(Move move);

    public enum PieceType{

        PAWN("P"){
            public boolean isKing() {
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        KNIGHT("N") {
            public boolean isKing() {
                        return false;
                    }
            public boolean isRook(){
                return false;
            }
        },
        BISHOP("B"){
            public boolean isKing() {
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        ROOK("R"){
            public boolean isKing() {
                return false;
            }
            public boolean isRook(){
                return true;
            }
        },
        QUEEN("Q"){
            public boolean isKing() {
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        KING("K"){
            public boolean isKing() {
                return true;
            }
            public boolean isRook(){
                return false;
            }
        };

        private String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }

}
