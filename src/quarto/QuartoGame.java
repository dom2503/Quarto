package quarto;

import java.awt.Point;
import java.util.Scanner;
import quarto.players.HumanPlayer;
import quarto.players.IRemotePlayer;
import quarto.players.MinimaxPlayer;
import quarto.players.NovicePlayer;
import quarto.players.Player;
import quarto.players.RandomPlayer;
import quarto.players.RemotePlayer;

/**
 * The main class of this simple command line Quarto game.
 *
 * Initializes the players and the board and then starts the game loop.
 */
public class QuartoGame {

  private Board board;
  private Scanner scanner;
  final private Player player1, player2;
  private Player nextPlayer;
  private Player currentPlayer;
  private boolean silent = false;

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

    this.player1 = this.determinePlayer("Player1");
    this.currentPlayer = this.player1;
    this.notifyRemote(this.player1, "Welcome Player1");
    
    this.player2 = this.determinePlayer("Player2");
    this.nextPlayer = this.player2;
    this.notifyRemote(this.player2, "Welcome Player2");
  }
  
  private void notifyRemote(Player player, String message){
    if(player instanceof IRemotePlayer){
      IRemotePlayer remote = (IRemotePlayer) player;
      remote.sendMessage(message);
    }
  }

  /**
   * Prints a line only if the application isn't in silent mode.
   *
   * @param s The string to print.
   */
  private void printlnSilent(String s) {
    if (!this.silent) {
      System.out.println(s);
    }
  }

  /**
   * Prints only if the application isn't in silent mode.
   *
   * @param s The string to print
   */
  private void printSilent(String s) {
    if (!this.silent) {
      System.out.print(s);
    }
  }
  /**
   * Start the game loop and run it until the game is over.
   */
  private void start() {
    int roundsToPlay = this.determineRoundsToPlay();

    if(roundsToPlay > 1){
      this.silent = true;
    }
    
    int draws = 0;
    int winsPlayer1 = 0;
    int winsPlayer2 = 0;
    
    for (int i = 0; i < roundsToPlay; i++) {
      this.printlnSilent("\n----- The game is starting now -----");

      while (!this.board.gameWasWon() && !this.board.isDraw()) {
        this.playRound();
      }

      if (this.board.isDraw()) {
        this.printlnSilent(this.board.toString());
        this.printlnSilent("You played to a draw. Nobody won.");
        draws++;
      } else if (this.board.gameWasWon()) {
        if(this.player1 == this.currentPlayer){
          winsPlayer1++;
        }else{
          winsPlayer2++;
        }
        this.printlnSilent("Congratulations " + this.currentPlayer.getName() + " you have won the game.");
      }
      
      if(roundsToPlay >1){
        System.out.println("Game " + (i+1) + " finished");
      }
      this.resetGame();
    }
    if(roundsToPlay > 1){
      System.out.println("Statistics:");
      System.out.println("Draws = " + draws);
      System.out.println("Wins " + this.player1.getName() + " = " + winsPlayer1);
      System.out.println("Wins " + this.player2.getName() + " = " + winsPlayer2);
    }
    closeRemotePlayer(this.player1);
    closeRemotePlayer(this.player2);
    System.exit(0);
  }
  
  private void closeRemotePlayer(Player player){
    if(player instanceof IRemotePlayer){
      IRemotePlayer remote = (IRemotePlayer) player;
      remote.close();
    }
  }

  private void resetGame(){
    this.player1.reset();
    this.player2.reset();
    this.board.reset();
    this.currentPlayer = this.player1;
    this.nextPlayer = this.player2;
  }
  
  private Player determinePlayer(String identifier) {
    this.printPlayerTypes();
    System.out.println("Please select the type of " + identifier + ":");
    return this.playerTypeSelection(identifier);
  }

  /**
   * If both players are simulated the user is asked to enter a number of rounds.
   *
   * @return The number of rounds to play.
   */
  private int determineRoundsToPlay() {
    int roundsToPlay = 0;
    if (this.nextPlayer instanceof HumanPlayer || this.currentPlayer instanceof HumanPlayer) {
      roundsToPlay = 1;
    } else {
      do {
        System.out.println("How many games should be simulated?");
        roundsToPlay = this.scanner.nextInt();
      } while (roundsToPlay <= 0);
    }

    return roundsToPlay;
  }

  /**
   * Plays out one round of Quarto.
   *
   * A round consists of the user that made the last move selecting a new piece and the other user
   * setting it on the field.
   */
  private void playRound() {
    this.printlnSilent("\n" + currentPlayer.getName() + " please select a piece to give to " + nextPlayer.getName() + ".");
    Piece nextPiece = currentPlayer.selectPieceForOpponent();
    this.printlnSilent(currentPlayer.getName() + " selected " + nextPiece);
    this.nextPlayer.setGivenPiece(nextPiece);
    Player tempPlayer = this.currentPlayer;
    this.currentPlayer = nextPlayer;
    this.nextPlayer = tempPlayer;

    this.printlnSilent("\n" + currentPlayer.getName() + " please make your move.");
    this.printSilent(this.board.toString());
    Point move = this.currentPlayer.makeMove();
    if(this.nextPlayer instanceof IRemotePlayer){
      IRemotePlayer remote = (IRemotePlayer) this.nextPlayer;
      remote.sendMove(move.x, move.y);
      remote.sendMessage("-");
    }
    this.printlnSilent("I made my move to " + (move.x + 1) + (char) (move.y + 65));
    this.printlnSilent(this.board.toString());
  }

  /**
   * Prints a list of all available player types to choose from.
   */
  private void printPlayerTypes() {
    System.out.println("1. Human Player");
    System.out.println("2. Random Player");
    System.out.println("3. Novice Player");
    System.out.println("4. Minimax Player");
    System.out.println("5. Remote Player");
  }

  /**
   * Asks the user for an index of a new player and creates it .
   *
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
        System.out.println("Please enter the number of moves that should be inspected:");
        int searchDepth = -1;
        while(searchDepth > 6 || searchDepth < 1){
          searchDepth = this.scanner.nextInt();
        }
        player = new MinimaxPlayer(this.board, searchDepth);
        break;
      case 5:
        IRemotePlayer remote = new RemotePlayer(this.board);
        player = remote;
        remote.sendMessage("Welcome, please enter your name:\n");
        playerName = remote.receiveMessage();     
        System.out.println(playerName + " connected.");
        break;
      default:
        System.out.println("Please only input numbers between 1 and 5. ");
        player = this.playerTypeSelection(playerName);
    }

    if (playerName != null) {
      player.setName(playerName);
    } else {
      System.out.println("Please enter a name for the player.");
      player.setName(this.scanner.next());
    }
    System.out.println();

    return player;
  }
}
