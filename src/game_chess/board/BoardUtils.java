/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Board Utilities class.
 * This class holds necessary information about the board
 *
 */

package game_chess.board;

public class BoardUtils {

    //column exceptions for pieces, due to movement constraints
    public static final boolean[] FIRST_COLUMN = inColumn(0);
    public static final boolean[] SECOND_COLUMN = inColumn(1);
    public static final boolean[] SEVENTH_COLUMN = inColumn(6);
    public static final boolean[] EIGHT_COLUMN = inColumn(7);

    //row exceptions for pieces, due to movement constraints
    public static final boolean[] SECOND_ROW = inRow(8);
    public static final boolean[] SEVENTH_ROW = inRow(48);

    //Board info
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    //calculate column space
    private static boolean[] inColumn(int columnNumber) {

        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }while(columnNumber < NUM_TILES);
        return column;

    }

    //calculate row spaces
    private static boolean[] inRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);

        return row;
    }

    public static boolean isValidTileCoordinate(int coordinate){
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
}
