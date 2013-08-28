/**
 *
 */
package quarto;

import java.util.ArrayList;
import java.util.Scanner;
import quarto.players.HumanPlayer;
import quarto.players.Player;
import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 * The main class of this Quarto game.
 */
public class QuartoGame {

  private Board board;
  private Scanner scanner;
  private Player nextPlayer;
  private Player currentPlayer;
  private ArrayList<Piece> pieces;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    QuartoGame game = new QuartoGame();
    game.start();
  }

  private QuartoGame() {
    this.scanner = new Scanner(System.in);
    this.board = new Board();
    this.setupPieces();

    System.out.println("---------- Welcome to Quarto! ----------");

    System.out.println("Please select the type of Player1:");
    this.currentPlayer = this.playerTypeSelection("Player1");

    System.out.println("Please select the type of Player2:");
    this.nextPlayer = this.playerTypeSelection("Player2");
  }

  private void start() {
    System.out.println();
    System.out.println("The game is starting now.");

    while (!this.isOver()) {
      this.playRound();
    }
  }

  /**
   * Plays out one round of Quarto.
   * 
   * A round consists of the user that made the last move selecting a new piece and the other 
   * user setting it on the field.
   */
  private void playRound() {
    System.out.println(currentPlayer.getName() + " please select a piece to give to " + nextPlayer.getName() + ".");
    this.printLeftoverPieces();
    Piece nextPiece = currentPlayer.selectPieceForOpponent();
    this.nextPlayer.setGivenPiece(nextPiece);
    Player tempPlayer = this.currentPlayer;
    this.currentPlayer = nextPlayer;
    this.nextPlayer = tempPlayer;

    this.currentPlayer.makeMove();
  }

  private boolean isOver() {
    if (this.pieces.isEmpty() || this.board.reachedFinalState()) {
      return true;
    }

    return false;
  }

  private void printPlayerTypes() {
    System.out.println("1. Human Player");
    System.out.println("2. Random Player");
    System.out.println("3. Novice Player");
    System.out.println("4. Minimax Player");
  }

  private Player playerTypeSelection(String playerName) {
    System.out.println();
    this.printPlayerTypes();
    int typeIndex = this.scanner.nextInt();
    Player player = null;

    switch (typeIndex) {
      case 1:
        player = new HumanPlayer(this.board);
        break;
      case 2:
        //insert random player when ready
        break;
      case 3:
        //insert novice player when ready
        break;
      case 4:
        //insert minimax player when ready
        break;
      default:
        System.out.println("Please only input number between 1 and 4. ");
        player = this.playerTypeSelection(playerName);
    }
    
    if (playerName != null) {
      player.setName(playerName);
    } else {
      System.out.println("Please enter a name for the player.");
      player.setName(this.scanner.next());
    }

    return player;
  }

  /**
   * Creates the 16 different playing pieces.
   *
   * The generation is done by using the values of the binary representation of the loop index as
   * the
   */
  private void setupPieces() {
    this.pieces = new ArrayList(16);

    for (int i = 0; i < 16; i++) {
      //takes the current permuation of the four binaries (i.e. 0000-1111) and puts them 
      //in a char array for getting the indexes of the matching enum values then
      char[] binaryValues = String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0').toCharArray();

      this.pieces.add(i, new Piece(
              PieceColor.values()[Character.getNumericValue(binaryValues[0])],
              PieceSize.values()[Character.getNumericValue(binaryValues[1])],
              PieceInnerShape.values()[Character.getNumericValue(binaryValues[2])],
              PieceShape.values()[Character.getNumericValue(binaryValues[3])]));
    }
  }

  private void printLeftoverPieces() {
    System.out.println();
    System.out.println("These are the unused pieces:");
    int index = 1;
    for(Piece currentPiece : this.pieces) {
      System.out.print(index + "." + currentPiece + "  ");
      index++;
    }
    System.out.println();
  }
}
