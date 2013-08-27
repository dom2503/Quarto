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
  private PieceSize isBig;
  private PieceInnerShape hasHole;
  private PieceShape isRound;
  
  public Piece(PieceColor color, PieceSize isBig, PieceInnerShape hasHole, PieceShape isRound){
    this.color = color;
    this.isBig = isBig;
    this.hasHole = hasHole;
    this.isRound = isRound;
  }
  
  public boolean get
}
