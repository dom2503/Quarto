package quarto.properties;

import quarto.Piece;

/**
 *
 */
public enum PieceSize {
  BIG, SMALL;
  
  /**
   * Checks whether this size is the same as the size of all the other given pieces.
   */
  public boolean equalsOthers(Piece[] others){
    for(int i=0; i < others.length; i++){
      if(this != others[i].size){
        return false;
      }
    }
    return true;
  }
}
