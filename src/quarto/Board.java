/*
 * 
 */
package quarto;

import java.util.ArrayList;
import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 * This class represents a board for playing Quarto.
 *
 *
 */
public class Board {

  private static int BOARD_LENGTH = 4;
  private ArrayList<Piece> pieces;
  private Piece[][] fields;

  public Board() {
    this.resetBoard();
  }

  public Piece[][] getBoard() {
    return this.fields;
  }

  /**
   *
   * @param x
   * @param y
   * @param piece
   */
  public void setField(int x, int y, Piece piece) {
    if (this.isValidCoordinate(x, y) && this.fieldIsEmpty(x, y)) {
      this.fields[y][x] = piece;
    }
  }

  public final void resetBoard() {
    this.setupPieces();
    this.fields = new Piece[BOARD_LENGTH][BOARD_LENGTH];
  }

  /**
   * Checks if the given field is still empty.
   */
  private boolean fieldIsEmpty(int x, int y) {
    if (this.fields[x][y] == null) {
      return true;
    }

    return false;
  }

  /**
   * Checks if the given coordinates are in range of the board.
   */
  private boolean isValidCoordinate(int x, int y) {
    if (x < BOARD_LENGTH && y < BOARD_LENGTH) {
      return true;
    }

    return false;
  }

  public boolean reachedFinalState() {
    if (pieces.isEmpty()) {
      return true;
    }
    return false;
  }

  public void printBoard(){
    System.out.println("y\\x    1      2      3      4");
    
    for(int i=0; i<BOARD_LENGTH;i++){
      System.out.print(" " + (char) (i + 65) + "    ");
      for(int j=0; j<BOARD_LENGTH; j++){
        if(fields[i][j] != null){
          System.out.print(" " + fields[i][j] + "  ");
        }else{
          System.out.print(" ____  ");
        }
      }
      System.out.println();
    }
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

  public ArrayList<Piece> getLeftoverPieces() {
    return this.pieces;
  }

  public void printLeftoverPieces() {
    System.out.println();
    System.out.println("These are the unused pieces:");
    int index = 1;
    for (Piece currentPiece : pieces) {
      System.out.print(index + "." + currentPiece + "  ");
      index++;
    }
    System.out.println();
  }

  public Piece takePieceForOpponent(int index) {
    return pieces.remove(index - 1);
  }
}
