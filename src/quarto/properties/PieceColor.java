package quarto.properties;

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
}
