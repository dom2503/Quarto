/**
 * 
 */
package quarto;

import java.util.Scanner;
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
  
  private Player player1;
  private Player player2;
  
  private Piece[] pieces;
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    QuartoGame game = new QuartoGame();
    game.start();
  }
  
  private QuartoGame(){
    this.scanner = new Scanner(System.in);
    this.board = new Board();
    this.setupPieces();
  }
  
  private void start(){
    System.out.println("---------- Welcome to Quarto! ----------");
    
    System.out.println("Please select the type of Player1:");
    this.player1 = this.playerTypeSelection();
    
    System.out.println("Please select the type of Player2:");
    this.player2 = this.playerTypeSelection();
    
    this.printLeftoverPieces();
  }
  
  private void printPlayerTypes(){
    System.out.println("1. Human Player");
    System.out.println("2. Random Player");
    System.out.println("3. Novice Player");
    System.out.println("4. Minimax Player");
  }
  
  private Player playerTypeSelection(){
    this.printPlayerTypes();
    int typeIndex = this.scanner.nextInt();
    Player player = null;
    
    switch(typeIndex){
      case 1:
        //insert human player when ready
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
        player = this.playerTypeSelection();
    }
    
    return player;
  }
  
  /**
   * Creates the 16 different playing pieces.
   */
  private void setupPieces(){
    this.pieces = new Piece[16];
    
    for(int i=0; i<16; i++){
      //takes the current permuation of the four binaries (i.e. 0000-1111) that can be converted into
      //the appropriate enum representations for the piece creation
      char[] binaryValues = String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0').toCharArray();
      
      this.pieces[i] = new Piece(
              PieceColor.values()[Character.getNumericValue(binaryValues[0])], 
              PieceSize.values()[Character.getNumericValue(binaryValues[1])], 
              PieceInnerShape.values()[Character.getNumericValue(binaryValues[2])], 
              PieceShape.values()[Character.getNumericValue(binaryValues[3])]
              );
    }
  }
  
  private void printLeftoverPieces(){
    for(Piece currentPiece : this.pieces){
      System.out.println(currentPiece);
    }
  }
}
