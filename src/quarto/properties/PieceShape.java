package quarto.properties;

import quarto.Piece;

/**
 *
 */
public enum PieceShape {
  SQUARE("[]"), ROUND("()");
  
  private String display;
  
  private PieceShape(String display){
    this.display = display;
  }
  
  @Override
  public String toString(){
    return display;
  }
  
  /**
   * Checks whether the given pieces has the same shape as all the given others.
   */
  public boolean equalsOthers(Piece[] others){
    for(int i=0; i < others.length; i++){
      if(this != others[i].shape){
        return false;
      }
    }
    return true;
  }
}
