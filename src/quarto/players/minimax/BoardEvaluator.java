package quarto.players.minimax;

import java.util.ArrayList;
import java.util.Random;
import quarto.Board;
import quarto.Piece;

/**
 *
 */
public class BoardEvaluator {
  
  private Random rand = new Random();
  
  /**
   * Evaluates how good the current position is for the last player that made a move.
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
      int nearlyFinishedLines = this.getNearlyFinishedLineCount(board);
      
      if(nearlyFinishedLines == 0){
        // 5 is arbitrary, but no finished lines means, that the next user can't finish
        // which is more positive than a draw
        return 5.0;
      }
      // nearly finished lines are bad, because, the next user could maybe finish them 
      // with the right piece
      return -1.0 * nearlyFinishedLines;
    }
    
    return result;
  }
  
  private int getNearlyFinishedLineCount(Board board){
    ArrayList<Piece[]> lines = board.getLines();
    int count = 0;
    
    for(Piece[] line : lines){
      ArrayList<Piece> filledPieces = new ArrayList<Piece>();
      for(Piece piece : line){
        if(piece != null){
          filledPieces.add(piece);
        }
      }
      
      if(filledPieces.size() == 3){
        if(filledPieces.get(0).color == filledPieces.get(1).color 
                && filledPieces.get(1).color == filledPieces.get(2).color){
        count++;
        } else if(filledPieces.get(0).innerShape == filledPieces.get(1).innerShape 
                && filledPieces.get(1).innerShape == filledPieces.get(2).innerShape){
        count++;
        } else if(filledPieces.get(0).shape == filledPieces.get(1).shape 
                && filledPieces.get(1).shape == filledPieces.get(2).shape){
        count++;
        } else if(filledPieces.get(0).size == filledPieces.get(1).size 
                && filledPieces.get(1).size == filledPieces.get(2).size){
        count++;
        }
      }
    }
    
    return count;
  }
}
