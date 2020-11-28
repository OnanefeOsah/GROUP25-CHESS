package game_chess.engine;

import game_chess.engine.board.BoardUtils;
import game_chess.engine.player.BlackPlayer;
import game_chess.engine.player.Player;
import game_chess.engine.player.WhitePlayer;

/**
 * Author: Onanefe Osah
 * Author: Osama
 * Date Created:
 *
 * Enum class "Alliance" to represent BLack/White chess pieces
 * **/

public enum Alliance {

    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isWhite () {
            return true;
        }

        @Override
        public boolean isBlack () {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare ( int position){
            return BoardUtils.EIGHTH_RANK[position];
        }

        @Override
        public Player choosePlayer ( final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return whitePlayer;
        }
    },

    BLACK {
        @Override
        public int getDirection () {
            return 1;
        }

        @Override
        public int getOppositeDirection () {
            return -1;
        }

        @Override
        public boolean isWhite () {
            return false;
        }

        @Override
        public boolean isBlack () {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer (final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return blackPlayer;
        }
    };


    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);

}

