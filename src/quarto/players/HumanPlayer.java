package quarto.players;

import java.util.Scanner;
import quarto.Board;
import quarto.Piece;

/**
 * This player type queries a human in front of the game to input the moves to be made.
 */
public class HumanPlayer extends QuartoPlayer {

  private Scanner scanner;

  public HumanPlayer(Board board) {
    super(board);
    this.scanner = new Scanner(System.in);
  }

  /**
   * Queries the user for valid coordinates on the Quarto board.
   */
  @Override
  public String makeMove() {
    int xCoordinate = -1, yCoordinate = -1;

    while (!this.getBoard().setField(xCoordinate, yCoordinate, this.getGivenPiece())) {
      try {
        System.out.println("Please enter the x-coordinate for your move:");
        xCoordinate = this.scanner.nextInt() - 1;
        if (xCoordinate < 0 || xCoordinate > 3) {
          throw new NumberFormatException("The xCoordinate was not in range.");
        }

        System.out.println("Please enter the y-coordinate for your move:");
        yCoordinate = this.scanner.next("[ABCDabcd]").toUpperCase().charAt(0) - 65;
      } catch (Exception e) { //ArrayIndexOutOfBoundsExceptino|InputMismatchException
        System.out.println("Your input was invalid, please try again.");
        this.scanner = new Scanner(System.in);
      }
    }

    return "";
  }

  /**
   * Queries the user to chose a valid leftover piece for the opponent.
   */
  @Override
  public Piece selectPieceForOpponent() {
    this.getBoard().printLeftoverPieces();

    int pieceIndex = this.scanner.nextInt();

    return getBoard().takePieceForOpponent(pieceIndex);
  }
}
