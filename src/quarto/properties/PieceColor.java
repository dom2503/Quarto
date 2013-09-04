package quarto.properties;

import quarto.Piece;

/**
 *
 */
public enum PieceColor {
  RED("r"), BLACK("b");
  
  private String display;
  
  private PieceColor(String display){
    this.display = display;
  }
  
  @Override
  public String toString(){
    return display;
  }
  
  /**
   * Checks whether this color is the same as the color of all the other given pieces.
   */
  public boolean equalsOthers(Piece[] others){
    for(int i=0; i < others.length; i++){
      if(this != others[i].color){
        return false;
      }
    }
    return true;
  }
}
