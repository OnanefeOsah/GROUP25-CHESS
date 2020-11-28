package game_chess.engine.player;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Move status enum to check the status of the board after a move is made
 * **/

public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();
}