package quarto.properties;

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
}
