package quarto.players.minimax;

import java.util.ArrayList;
import quarto.Board;

/**
 * Represents a node in the minimax search tree.
 */
public class MinimaxNode {

  final private MinimaxNode parent;
  final private Board board;
  private double alpha;
  private double beta;
  private ArrayList<MinimaxNode> children;

  public MinimaxNode(MinimaxNode parent, Board board, double alpha, double beta) {
    this.parent = parent;
    this.board = board;
    this.alpha = alpha;
    this.beta = beta;
    this.children = new ArrayList<MinimaxNode>();
  }

  public double getAlpha() {
    return this.alpha;
  }

  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }
  
  public double getBeta(){
    return this.beta;
  }
  
  public void setBeta(double beta){
    this.beta = beta;
  }
  
  public Board getBoard(){
    return this.board;
  }
  
  public MinimaxNode getParent(){
    return this.parent;
  }
  
  public void addChild(MinimaxNode child){
    this.children.add(child);
  }
  
  public MinimaxNode[] getChildren(){
    return (MinimaxNode[]) this.children.toArray();
  }
}