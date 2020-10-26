package game_chess;

import game_chess.board.Board;


/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Main class acts as our "test class" during this iteration
 * Test involved printing the board positions and debugging the piece behaviour algorithms**
 **/

public class Main{

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
    }
}
