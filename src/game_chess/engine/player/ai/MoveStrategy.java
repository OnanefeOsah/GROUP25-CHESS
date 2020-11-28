package game_chess.engine.player.ai;


import game_chess.engine.board.Board;
import game_chess.engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board);
}
