package game_chess.engine.player;

import game_chess.engine.Alliance;
import game_chess.engine.board.Board;
import game_chess.engine.board.Move;
import game_chess.engine.board.Tile;
import game_chess.engine.pieces.Piece;
import game_chess.engine.pieces.Rook;

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
 * "Black" Player class that represents the alliance for a type of player on the board
 **/

public class BlackPlayer extends Player {

    public  BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves){
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    //return piece alliance
    public Alliance getAlliance(){
        return Alliance.BLACK;
    }

    //return opponent piece type
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    //calculate king castle moves on the black side
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()){
            //black king side castle
            if (this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() ){
                    if(Player.calculateAttacksOnTile(5,opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }

            //black queen side castle
            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied()&& !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2,(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }

        return Collections.unmodifiableList(kingCastles);
    }
}