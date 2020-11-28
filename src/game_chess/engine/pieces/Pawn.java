package game_chess.engine.pieces;

import game_chess.engine.Alliance;
import game_chess.engine.board.Board;
import game_chess.engine.board.BoardUtils;
import game_chess.engine.board.Move;

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
 * Pawn class to represent the piece type "Pawn"
 * **/

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){

            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }

            //pawns can move two spaces on their first move
            else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                            (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {

                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);

                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()
                        && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }


            }

            else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
                            || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){

                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }

            }

            else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())
                            || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))){

                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if(board.getEnPassantPawn() != null ) {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }

        }

        return Collections.unmodifiableList(legalMoves);
    }

    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    //TODO
    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition, this.pieceAlliance,false);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
