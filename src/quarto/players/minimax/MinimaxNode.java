package quarto.players.minimax;

import quarto.Board;

/**
 * Represents a node in the minimax search tree.
 */
public class MinimaxNode {

  final private MinimaxNode parent;
  final private Board board;

  public MinimaxNode(MinimaxNode parent, Board board) {
    this.parent = parent;
    this.board = board;
  }
  
  public Board getBoard(){
    return this.board;
  }
  
  public MinimaxNode getParent(){
    return this.parent;
  }
}