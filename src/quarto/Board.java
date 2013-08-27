/*
 * 
 */
package quarto;

/**
 * This class represents a board for playing Quarto.
 * 
 * 
 */
public class Board {
  
  private static int BOARD_LENGTH = 4; 
  
  private Piece[][] fields;
  
  public Board(){
    this.resetBoard();
  }
  
  public Piece[][] getBoard(){
    return this.fields;
  }
  
  /**
   * 
   * @param x
   * @param y
   * @param piece 
   */
  public void setField(int x, int y, Piece piece){
    if(this.isValidCoordinate(x, y) && this.fieldIsEmpty(x, y)){
      this.fields[x][y] = piece;
    }
  }
  
  public final void resetBoard(){
    this.fields = new Piece[BOARD_LENGTH][BOARD_LENGTH];
  }
  
  /**
   * Checks if the given field is still empty.
   */
  private boolean fieldIsEmpty(int x, int y){
    if(this.fields[x][y] == null){
      return true;
    }
    
    return false;
  }
  
  /**
   * Checks if the given coordinates are in range of the board.
   */
  private boolean isValidCoordinate(int x, int y){
    if(x < BOARD_LENGTH && y < BOARD_LENGTH){
      return true;
    }
    
    return false;
  }
  
  @Override
  public String toString(){
    System.out.println("   ");
    return "";
  }
}
