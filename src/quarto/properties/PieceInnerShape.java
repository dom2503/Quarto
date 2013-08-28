package quarto.properties;

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
}
