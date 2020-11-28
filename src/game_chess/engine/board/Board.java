package game_chess.engine.board;

import game_chess.engine.Alliance;
import game_chess.engine.pieces.*;
import game_chess.engine.player.Player;
import game_chess.engine.player.WhitePlayer;
import game_chess.engine.player.BlackPlayer;
import java.util.ArrayList;
import java.util.*;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Board class to handle the game board and board events
 * Board contains a nested class Builder to help with building the board
 * **/

public class Board{

    //List of all the tiles on the board
    private final List<Tile> gameBoard;

    //Collection of all pieces in the game
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    //Types of Players
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    public Board(final Builder builder){
        //create a new board using the builder class
        this.gameBoard = createGameBoard(builder);

        //calculate and store all active pieces on the board
        this.whitePieces = calculateActivePieces(this.gameBoard , Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        //calculate possible moves for each piece at current time
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);

        //Instantiate player types
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

    }

    @Override
    public String toString(){

        final StringBuilder builder = new StringBuilder();

        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    //method to return all current black pieces
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    //method to return all current white pieces
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    //method to calculate all legal moves for the pieces instantiated on the board using tile locations
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }

        return Collections.unmodifiableList(legalMoves);
    }

    //Calculate active pieces on the board
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance){

        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    //Get method to return tile location
    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    //Create game board with 64 tiles (0 - 63)
    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES ];
        for(int i = 0; i < BoardUtils.NUM_TILES ; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }

        return List.of(tiles);
    }

    //Instantiate game pieces for board
    public static Board createStandardBoard(){
        final Builder builder = new Builder();

        //Black side
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK, true, true));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));


        //White side

        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, true, true));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));

        //set first move to white
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    //method to get all current legal moves
    public Collection<Move> getAllLegalMoves(){
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableList(allLegalMoves);
    }

    //nested class "Builder"
    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}