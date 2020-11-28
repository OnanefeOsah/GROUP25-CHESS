package game_chess.engine.player.ai;

import game_chess.engine.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
