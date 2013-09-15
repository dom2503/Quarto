package quarto.players;

import quarto.Board;
import quarto.Piece;

/**
 * This player makes it moves according to these rules (taken from 
 * the task document):
 * 
 * It always places its piece in a winning formation, if possible. 
 * Otherwise, it chooses randomly among the existing open cells.
 * 
 * If given a choice among pieces to give the opponent, it always chooses 
 * one that cannot be used to immediately win the game, if such a piece 
 * is available.
 */
public class NovicePlayer extends QuartoPlayer {

  public NovicePlayer(Board board){
    super(board);
  }
  
  @Override
  public String makeMove() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Piece selectPieceForOpponent() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
