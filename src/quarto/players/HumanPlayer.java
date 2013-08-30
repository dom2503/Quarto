package quarto.players;

import java.util.Scanner;
import quarto.Board;
import quarto.Piece;

/**
 * This player type queries a human in front of the game to input the moves to be
 * made.
 */
public class HumanPlayer extends QuartoPlayer{

  private Scanner scanner;
  
  public HumanPlayer(Board board){
    super(board);
    this.scanner = new Scanner(System.in);
  }
  
  @Override
  /**
   * Queries the user for valid coordinates on the Quarto board.
   */
  public void makeMove() {
    System.out.println("Please enter the x-coordinate for your move:");
    int xCoordinate = this.scanner.nextInt();
    System.out.println("Please enter the y-coordinate for your move:");
    String yCoordinate = this.scanner.next("[ABCD]");
    
    this.getBoard().setField(xCoordinate -1, yCoordinate.charAt(0) - 65, this.getGivenPiece());
    this.getBoard().printBoard();
  }

  @Override
  /**
   * Queries the user to chose a valid leftover piece for the opponent.
   */
  public Piece selectPieceForOpponent() {
    this.getBoard().printLeftoverPieces();
    
    int pieceIndex = this.scanner.nextInt();
    
    return getBoard().takePieceForOpponent(pieceIndex);
  }
  
}
