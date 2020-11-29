package game_chess.engine.player;

import game_chess.engine.board.Board;
import game_chess.engine.board.Move;

/**
 * Created by :
 * Date Created:
 *
 * Move Transition class
 * Player class is abstract and defines the attributes of a "Player"
 **/
public class MoveTransition{
    private final Board transitionBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard, final Move transitionMove, final MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }

}