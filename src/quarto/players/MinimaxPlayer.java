package quarto.players;

import quarto.Board;
import quarto.Piece;

/**
 * Uses the minimax algorithm to determine the next Quarto move.
 */
public class MinimaxPlayer extends QuartoPlayer {

  final private int depth;
  
  public MinimaxPlayer(Board board){
    this(board, 3);
  }
  
  public MinimaxPlayer(Board board, int depth){
    super(board);
    
    this.depth = depth;
  }
  
  @Override
  public void makeMove() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Piece selectPieceForOpponent() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  class Node{
    
  }
}
