/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quarto.players;

import quarto.Board;
import quarto.Piece;

/**
 *
 */
public class HumanPlayer extends QuartoPlayer{

  public HumanPlayer(Board board){
    super(board);
  }
  
  @Override
  public void makeMove() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Piece selectPieceForOpponent() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
