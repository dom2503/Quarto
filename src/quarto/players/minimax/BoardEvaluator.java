package quarto.players.minimax;

import java.util.Random;
import quarto.Board;

/**
 *
 */
public class BoardEvaluator {
  
  private Random rand = new Random();
  
  /**
   * Evaluates the chances for the next user who can put a piece on the board.
   * @param board
   * @return 
   */
  public double evaluateBoard(Board board){
    double result;
    if(board.gameWasWon()){
      result = Double.POSITIVE_INFINITY;
    } else if(board.isDraw()){
      result = 0.0;
    } else {
      result = Math.random();
    }
    
    return result;
  }
}
