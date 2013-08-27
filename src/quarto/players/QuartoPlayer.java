package quarto.players;

import quarto.Board;
import quarto.Piece;

/**
 * This class provides some generally usable functionality for an implementation of a Quarto player.
 * 
 * It enables a user to set the board and the piece for the next move and encapsulates this data.
 * All other functionality regarding the actual choices of the next move and the next piece for the 
 * opponent have to be implemented by the user.
 */
abstract public class QuartoPlayer implements Player{

  private Piece givenPiece;
  private Board board;
  
  public QuartoPlayer(Board board, Piece givenPiece){
    this.board = board;
    this.givenPiece = givenPiece;
  }
  
  /**
   * @return The piece that was given by the opponent to be used for the next move.
   */
  protected Piece getGivenPiece(){
    return this.givenPiece;
  }
  
  /**
   * @return The Quarto board on which the game is currently played.
   */
  protected Board getBoard(){
    return this.board;
  }
  
}
