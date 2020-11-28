package game_chess.engine.pieces;

import game_chess.engine.Alliance;
import game_chess.engine.board.Board;
import game_chess.engine.board.BoardUtils;
import game_chess.engine.board.Move;
import game_chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static game_chess.engine.board.Move.*;


/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * King class to represent the piece type "King"
 * **/
public class King extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final int piecePosition, final Alliance pieceAlliance, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }
    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable, final boolean isCastled) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled(){
        return this.isCastled;
    }
    public boolean isKingSideCastleCapable(){
        return this.kingSideCastleCapable;
    }
    public boolean isQueenSideCastleCapable(){
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || (isEightColumnExclusion(this.piecePosition,currentCandidateOffset))){
                continue;
            }

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                //if tile is not occupied carry out a regular move, else carry out an attack move
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Regular(board,this, candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new MajorAttack(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

}
