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
  public boolean setField(int x, int y, Piece piece) {
    if (this.isValidCoordinate(x, y) && this.fieldIsEmpty(x, y)) {
      this.fields[y][x] = piece;
      return true;
    }
    
    return false;
  }

  /**
   * Returns the board to it's initial state, so that a new game could be started.
   */
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

  /**
   * Checks whether the games is over.
   * 
   * There exist two possibilities, either one of the players has one by putting a 
   * piece or all the pieces are gone and it's a draw.
   */
  public boolean reachedFinalState() {
    if (pieces.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Prints a string representation of the current state of the board.
   */
  public void printBoard() {
    System.out.println("y\\x    1      2      3      4");

    for (int i = 0; i < BOARD_LENGTH; i++) {
      System.out.print(" " + (char) (i + 65) + "    ");
      for (int j = 0; j < BOARD_LENGTH; j++) {
        if (fields[i][j] != null) {
          System.out.print(" " + fields[i][j] + "  "); //uses toString of the piece to print it
        } else {
          System.out.print(" ____  "); //empty field
        }
      }
      System.out.println();
    }
  }

  /**
   * Creates the 16 different playing pieces.
   *
   * The generation is done by using the values of the binary representation of the loop index.
   * These are then converted into the appropriate Enum values from the properties package.
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

  /**
   * Prints a list of all the pieces that haven't been put on the board yet and adds a 1-based index
   * number to them, that can be used for user input.
   */
  public void printLeftoverPieces() {
    System.out.println();
    System.out.println("These are the available pieces:");
    int index = 1;
    for (Piece currentPiece : pieces) {
      System.out.print(index + "." + currentPiece + "  ");
      index++;
    }
    System.out.println();
  }

  /**
   * Retrieves the piece that currently has the given index and removes it from the collection of
   * available pieces.
   *
   * @param index A 1 based index number of the
   * @return The selected Piece.
   */
  public Piece takePieceForOpponent(int index) {
    if (pieces.size() >= index) {
      return pieces.remove(index - 1);
    }

    throw new IllegalArgumentException("The given index is out of range.");
  }
}
