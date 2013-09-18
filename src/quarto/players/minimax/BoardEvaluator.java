package quarto.players.minimax;

import java.util.ArrayList;
import quarto.Board;
import quarto.Piece;

/**
 *
 */
public class BoardEvaluator {
  
  /**
   * Evaluates how good the current position is for the last player that made a move.
   * @param board
   * @return 
   */
  public double evaluateBoard(Board board){
    double result;
    if(board.gameWasWon()){
      result = -1 * 1000;
    } else if(board.isDraw()){
      result = 0.0;
    } else {
      int nearlyFinishedLines = this.getNearlyFinishedLineCount(board);

      // the value of 10.0 is arbitrary, it's just to denote that it's good 
      // that we don't leave nearly finished lines
      if(nearlyFinishedLines == 0){
        return 10.0;
      }
      // nearly finished lines are bad, because, the next user could maybe finish them 
      // with the right piece
      return -1 * nearlyFinishedLines;
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
