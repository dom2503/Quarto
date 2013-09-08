package quarto;

import java.util.ArrayList;
import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 * This class represents a board for playing Quarto.
 */
public class Board {

  public final static int BOARD_LENGTH = 4;
  private ArrayList<Piece> pieces;
  private Piece[][] fields;

  /**
   * Sets up the board to the starting state.
   */
  public Board() {
    this.setupPieces();
    this.fields = new Piece[BOARD_LENGTH][BOARD_LENGTH];
  }
  
  /**
   * Copies the state of the given board to this board. 
   */
  public Board(Board board){
    this.pieces = new ArrayList<Piece>(board.pieces);
    this.fields = new Piece[BOARD_LENGTH][BOARD_LENGTH];
    
    Piece[][] oldFields = board.fields;
    
    for (int i = 0; i < BOARD_LENGTH; i++) {
      System.arraycopy(oldFields[i], 0, this.fields[i], 0, oldFields[0].length);
    }
  }

  /**
   * Returns the current state of the board.
   * 
   * Please note that the return value should never(!) be changed and just be
   * used for evaluating the state of the board. Always use methods of this 
   * class to change the state of the board.
   */
  public Piece[][] getBoard() {
    return this.fields;
  }

  /**
   * Sets the given field to the given coordinates if they are valid.
   */
  public boolean setField(int x, int y, Piece piece) {
    if (this.isValidCoordinate(x, y) && this.fieldIsEmpty(x, y)) {
      this.fields[x][y] = piece;
      return true;
    }
    
    return false;
  }

  public boolean fieldCanBeSet(int x, int y){
    if (this.isValidCoordinate(x, y) && this.fieldIsEmpty(x, y)) {
      return true;
    }
    
    return false;
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
   * Checks whether the last player that put a piece has won the game.
   */
  public boolean gameWasWon() {
    for(int i = 0; i < BOARD_LENGTH; i++){
      //get row to check
      Piece[] currentRow = {fields[0][i], fields[1][i], fields[2][i], fields[3][i]};
      
      //checks column and row
      if(lineIsWon(fields[i]) || lineIsWon(currentRow)){
        return true;
      }
    }
    
    //check diagonals
    Piece[] backslashDiagonal = {fields[0][0], fields[1][1], fields[2][2], fields[3][3]};
    Piece[] slashDiagonal = {fields[3][0], fields[2][1], fields[1][2], fields[0][3]};
    if(lineIsWon(backslashDiagonal) || lineIsWon(slashDiagonal)){
      return true;
    }
    
    return false;
  }
  
  /**
   * Checks whether every space in the line was set.
   */
  private boolean lineIsFull(Piece[] line){
    for(Piece piece : line){
      if(piece == null){
        return false;
      }
    }
    return true;
  }
  
  private boolean lineIsWon(Piece[] line){
    Piece[] others = new Piece[line.length -1];
    System.arraycopy(line, 1, others, 0, line.length-1);
    
    if(lineIsFull(line) && 
            (line[0].size.equalsOthers(others) || 
            line[0].color.equalsOthers(others) || 
            line[0].innerShape.equalsOthers(others) || 
            line[0].shape.equalsOthers(others))){
      return true;
    }
    
    return false;
  }
  
  /**
   * Checks whether the game is over and in a state of draw.
   */
  public boolean isDraw(){
    if(!this.gameWasWon()){
      for(int i = 0; i < BOARD_LENGTH; i++){
        if(!lineIsFull(this.fields[i])){
          return false;
        }
      }
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
        if (fields[j][i] != null) {
          System.out.print(" " + fields[j][i] + "  "); //uses toString of the piece to print it
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

  /**
   * @return Returns the list of all the pieces that haven't been set yet.
   */
  public ArrayList<Piece> getLeftoverPieces() {
    return this.pieces;
  }
  
  /**
   * @return The number of pieces that haven't been set yet.
   */
  public int getLeftoverPieceCount(){
    return this.pieces.size();
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
   * @param index A 1-based index number of the piece that should be taken.
   * @return The selected Piece.
   */
  public Piece takePieceForOpponent(int index) {
    if (pieces.size() >= index) {
      return pieces.remove(index - 1);
    }

    throw new IllegalArgumentException("The given index is out of range.");
  }
  
  /**
   * If the piece was retrieved through some other means, this method just removes it from
   * the collection
   * 
   * @param piece
   * @return 
   */
  public Piece takePieceForOpponent(Piece piece){
    if(this.pieces.contains(piece)){
      this.pieces.remove(piece);
      return piece;
    }
    
    return null;
  }
  
  /**
   * Calculates the number of already made moves.
   */
  public int getMoveCount(){
    int count = 0;
    for(int i = 0; i<BOARD_LENGTH; i++){
      for(int j = 0; j < BOARD_LENGTH; j++){
        if(this.fields[i][j]!=null){
          count++;
        }
      }
    }
    return count;
  }
}
