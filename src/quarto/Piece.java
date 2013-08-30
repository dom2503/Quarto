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
  private PieceColor color;
  private PieceSize size;
  private PieceInnerShape innerShape;
  private PieceShape shape;
  
  public Piece(PieceColor color, PieceSize size, PieceInnerShape innerShape, PieceShape shape){
    this.color = color;
    this.size = size;
    this.innerShape = innerShape;
    this.shape = shape;
  }
  
  @Override
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
