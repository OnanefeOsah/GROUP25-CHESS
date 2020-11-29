package game_chess.engine.player.ai;

import game_chess.engine.board.Board;
import game_chess.engine.board.Move;
import game_chess.engine.player.MoveTransition;
import game_chess.engine.player.ai.MoveStrategy;

/**
 * Author: Luther
 * Date Created:
 *
 * Mini-Max is an algorithm... //TODO
 * **/


public class Minimax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private long boardsEvaluated;

    public Minimax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
        this.boardsEvaluated = 0;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " THINKING with depth: " +this.searchDepth);

        int numMoves = board.currentPlayer().getLegalMoves().size();

        for(final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                if (board.currentPlayer().getAlliance().isWhite()) {
                    currentValue = min(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                }
                else {
                    currentValue = max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                }

                if(board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                }
                else if(board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;

                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }


    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }

    public int min(final Board board, final int depth) {
        if (depth == 0) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth) {
        if (depth == 0) {
            this.boardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition =board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
