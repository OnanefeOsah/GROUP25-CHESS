package game_chess.player;

import game_chess.Alliance;
import game_chess.board.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import game_chess.pieces.King;
import game_chess.board.Move;
import game_chess.pieces.Piece;
import java.util.*;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Player class that represents a type of player on the board
 * Player class is abstract and defines the attributes of a "Player"
 **/

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection <Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();

        List<Move> legal = new ArrayList<>();
        legal.addAll(legalMoves);
        legal.addAll(calculateKingCastles(legalMoves, opponentMoves));
        this.legalMoves = legal;

        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();

    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    //method to check if a destination coordinate is under/can be attack(ed)
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){
        final List<Move> attackMoves = new ArrayList<>();
            for (final Move move : moves){
                if(piecePosition == move.getDestinationCoordinate()) {
                         attackMoves.add(move);
                }
            }
            return Collections.unmodifiableList(attackMoves);
    }

    //establishKing method to ensure there is a valid King for each player
    private King establishKing() {
        //iterate through active pieces to find king piece for each player
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid Board due to lack of King");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    //method to check if a "king" piece is being "checked" by an opposing piece
    public boolean isInCheck(){
        return this.isInCheck();
    }

    //method to check if a "king" piece is being "checked" by an opposing piece and has no escape moves
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    //method to check if a "king" piece is not being "checked" by an opposing piece and has no escape moves
    public boolean isInStaleMate() {
        return !this.isInCheck() && !hasEscapeMoves();
    }

    //method to check if a "king" piece is being "checked" by an opposing piece and has escape moves
    protected boolean hasEscapeMoves(){
        for (final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){

        if (!isMoveLegal(move)){
            return new MoveTransition(this.board,move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();
        //get opponents legal attack moves to a king piece
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    //returns active pieces
    public abstract Collection<Piece> getActivePieces();

    //returns piece alliance
    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals );
}


