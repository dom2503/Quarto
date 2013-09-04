package quarto.properties;

import quarto.Piece;

/**
 *
 */
public enum PieceInnerShape {
  HOLE("*"), FLAT(" ");
  
  private String display;
  
  private PieceInnerShape(String display){
    this.display = display;
  }
  
  @Override
  public String toString(){
    return display;
  }
  
  /**
   * Checks whether this inner shape is the same inner shape as of the given 
   * other pieces.
   */
  public boolean equalsOthers(Piece[] others){
    for(int i=0; i < others.length; i++){
      if(this != others[i].innerShape){
        return false;
      }
    }
    return true;
  }
}
