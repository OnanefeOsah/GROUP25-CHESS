package game_chess.engine.board;

import game_chess.engine.pieces.Pawn;
import game_chess.engine.pieces.Piece;
import game_chess.engine.pieces.Rook;


/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Move class to represent possible moves
 * **/


public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }


    //"hashcode" method overridden
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();

        return result;
    }

    //"equals" method overridden
    public boolean equals(final Object other){
        if (this == other ){
            return true ;
        }
        if (!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() && getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public int getDestinationCoordinate(){
     return this.destinationCoordinate;
    }

    public boolean isCastlingMove(){
      return false;
    }

    public Piece getAttackedPiece(){
        return null;
}

    public Board execute(){
        final Board.Builder builder = new Board.Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    //Regular Move
    public static class Regular extends Move{

        public Regular(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof Regular && super.equals(other);
        }
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    //Attack Move
    public static class Attack extends Move{

        final Piece attackedPiece;

        public Attack(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        public boolean equals(final Object other){
            if (this == other){
                return true ;
            }
            if (!(other instanceof Regular)){
                return false;
            }
            final Attack otherAttack = (Attack) other;
            return super.equals(otherAttack) && getAttackedPiece().equals(otherAttack.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        public boolean isAttack(){
            return true;
        }

        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    //Regular pawn move
    public static final class PawnMove extends Move{

        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    //regular pawn attack move
    public static class PawnAttackMove extends Attack{

        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

    }

    //Special move "En Passant"
    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

    }

    //Pawn jump(2 space) move for each pawn in its starting position
    public static final class PawnJump extends Move{

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Board.Builder builder  = new Board.Builder();

            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces() ){
                builder.setPiece(piece);
            }
             final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    //Castle Move to represent "Castling" a technique in chess where the king moves the the opposing side of a rook piece
    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        public boolean isCastlingMove(){
            return true;
        }



        public Board execute(){

            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);

                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces() ){
                builder.setPiece(piece);
            }
          builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    //Castling can happen on the kings side
    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                  final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        public String toString(){
            return "O-O";
        }
    }

    //Castling can happen on the queens side
    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook,
                                   final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook,castleRookStart,castleRookDestination);
        }
        public String toString(){
            return "O-O-O";
        }
    }

    //Null move class to reverse invalid moves
    public static final class NullMove extends Move {

        public NullMove() {
            super(null, -1);
        }

        public Board execute(){
            throw new RuntimeException("Cannot execute null move!!!");
        }
    }

    //Move factory processes moves on the board
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Cannot Create!!!");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
