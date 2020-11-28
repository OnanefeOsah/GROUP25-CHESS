package game_chess.engine.board;

import game_chess.engine.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Tile class to represent the properties of each tile on the board
 * **/


public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_STORE = createAllPossibleEmptyTiles();

    //create tiles for the board
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles(){

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i< BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }

        //return emptyTileMap;
        return Collections.unmodifiableMap(emptyTileMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_STORE.get(tileCoordinate);
    }

    private Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile{

        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        public boolean isTileOccupied(){
            return false;
        }

        public Piece getPiece(){
            return null;
        }

        @Override
        public String toString(){
            return "-";
        }

    }

    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }
}