/*
 *
 */
package quarto;

import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 *
 */
public class Piece{
  final public PieceColor color;
  final public PieceSize size;
  final public PieceInnerShape innerShape;
  final public PieceShape shape;
  
  public Piece(PieceColor color, PieceSize size, PieceInnerShape innerShape, PieceShape shape){
    this.color = color;
    this.size = size;
    this.innerShape = innerShape;
    this.shape = shape;
  }
  
  @Override
  /**
   * Puts together a string representation of the properties of this piece.
   * 
   * Examples: [r*], (B )
   */
  public String toString(){
    StringBuilder returnBuilder = new StringBuilder();
    
    returnBuilder.append(this.shape);
    returnBuilder.insert(1, this.innerShape);
    returnBuilder.insert(1, this.color);
    
    String returnString = returnBuilder.toString();
    if(size == PieceSize.BIG){
      returnString = returnString.toUpperCase();
    }
    
    return returnString;
  }
  
}
