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
  private String name;
  
  public QuartoPlayer(Board board){
    this.board = board;
  }
  
  /**
   * @return The piece that was given by the opponent to be used for the next move.
   */
  protected Piece getGivenPiece(){
    return this.givenPiece;
  }
  
  @Override
  public void setGivenPiece(Piece givenPiece){
    this.givenPiece =  givenPiece;
  }
  
  /**
   * @return The Quarto board on which the game is currently played.
   */
  protected Board getBoard(){
    return this.board;
  }
  
  @Override
  public void setName(String name){
    this.name = name;
  }
  
  @Override
  public String getName(){
    return this.name;
  }
  
}
