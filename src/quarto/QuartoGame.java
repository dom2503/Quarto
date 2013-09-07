package quarto;

import java.util.Scanner;
import quarto.players.HumanPlayer;
import quarto.players.MinimaxPlayer;
import quarto.players.NovicePlayer;
import quarto.players.Player;
import quarto.players.RandomPlayer;

/**
 * The main class of this simple command line Quarto game.
 * 
 * Initializes the players and the board and then starts the game loop.
 */
public class QuartoGame {

  private Board board;
  private Scanner scanner;
  private Player nextPlayer;
  private Player currentPlayer;

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

    System.out.println("---------- Welcome to Quarto! ----------\n");

    this.printPlayerTypes();
    System.out.println("Please select the type of Player1:");
    this.currentPlayer = this.playerTypeSelection("Player1");
    System.out.println();

    this.printPlayerTypes();
    System.out.println("Please select the type of Player2:");
    this.nextPlayer = this.playerTypeSelection("Player2");
    System.out.println();
  }

  /**
   * Start the game loop and run it until the game is over.
   */
  private void start() {
    System.out.println();
    System.out.println("----- The game is starting now -----");

    while(!this.board.gameWasWon() && !this.board.isDraw()) {
      this.playRound();
    }
    
    if(this.board.isDraw()){
      System.out.println("You played to a draw. Nobody won.");
      System.exit(0);
    } else if(this.board.gameWasWon()){
      System.out.println("Congratulations " + this.currentPlayer.getName() + " you have won the game.");
      System.exit(0);
    }
  }

  /**
   * Plays out one round of Quarto.
   * 
   * A round consists of the user that made the last move selecting a new piece and the other 
   * user setting it on the field.
   */
  private void playRound() {
    System.out.println();
    System.out.println(currentPlayer.getName() + " please select a piece to give to " + nextPlayer.getName() + ".");
    Piece nextPiece = currentPlayer.selectPieceForOpponent();
    this.nextPlayer.setGivenPiece(nextPiece);
    Player tempPlayer = this.currentPlayer;
    this.currentPlayer = nextPlayer;
    this.nextPlayer = tempPlayer;

    System.out.println();
    System.out.println(currentPlayer.getName() + " please make your move.");
    this.board.printBoard();
    this.currentPlayer.makeMove();
  }

  /**
   * Prints a list of all available player types to choose from.
   */
  private void printPlayerTypes() {
    System.out.println("1. Human Player");
    System.out.println("2. Random Player");
    System.out.println("3. Novice Player");
    System.out.println("4. Minimax Player");
  }

  /**
   * Asks the user for an index of a new player and creates it
   * .
   * @param playerName The name that should be given to the player
   */
  private Player playerTypeSelection(String playerName) {
    int typeIndex = this.scanner.nextInt();
    Player player = null;

    switch (typeIndex) {
      case 1:
        player = new HumanPlayer(this.board);
        break;
      case 2:
        player = new RandomPlayer(this.board);
        break;
      case 3:
        player = new NovicePlayer(this.board);
        break;
      case 4:
        player = new MinimaxPlayer(this.board);
        break;
      default:
        System.out.println("Please only input numbers between 1 and 4. ");
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
}
