package game_chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import game_chess.engine.Alliance;
import game_chess.engine.board.Board;
import game_chess.engine.board.Tile;
import game_chess.engine.pieces.Piece;
import game_chess.engine.board.Move;
import game_chess.engine.pieces.Rook;

import static game_chess.engine.board.Move.*;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * "White" Player class that represents the alliance of a type of player on the board
 **/

public class WhitePlayer extends Player {

    public WhitePlayer (final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    //return piece alliance
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    //return opponent piece type
    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    //method to calculate the possible castle moves for a white king
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //white king side castle
            if (this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }

            //white queen side castle
            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied()&& !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()
                        && rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }

        return Collections.unmodifiableList(kingCastles);
    }
}
