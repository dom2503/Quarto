package quarto.players;

import java.util.ArrayList;
import quarto.Board;
import quarto.Piece;
import quarto.players.minimax.BoardEvaluator;
import quarto.players.minimax.MinimaxNode;
import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 * Uses the minimax algorithm to determine the next Quarto move.
 *
 * The first few moves are made randomly, minimax is only employed during the later stages of the
 * game.
 */
public class MinimaxPlayer extends QuartoPlayer {

  final private static int RANDOM_MOVES = 8;
  final private int depth;
  final private BoardEvaluator evaluator;
  final private RandomPlayer randomPlayer;
  private MinimaxNode bestMove = null;
  private MinimaxNode rootNode;

  public MinimaxPlayer(Board board) {
    this(board, 3);
  }

  public MinimaxPlayer(Board board, int depth) {
    super(board);

    this.depth = depth;
    this.evaluator = new BoardEvaluator();
    this.randomPlayer = new RandomPlayer(board);
  }

  @Override
  public void makeMove() {
    System.out.println(this.getBoard().getMoveCount());
    System.out.println(this.getBoard().getLeftoverPieceCount());
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      this.randomPlayer.makeMove();
    } else {
      this.makeMinimaxMove();
    }
  }

  @Override
  public void setGivenPiece(Piece piece){
    super.setGivenPiece(piece);
    this.randomPlayer.setGivenPiece(piece);
  }
  
  private void makeMinimaxMove() {
    rootNode = new MinimaxNode(null, this.getBoard(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    this.minimaxPruned(rootNode, this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
  }

  /**
   *
   * @param node
   * @param depth
   * @param alpha
   * @param beta
   * @param maximizing
   * @return
   */
  private double minimaxPruned(MinimaxNode node, int depth, double alpha, double beta, boolean maximizing) {
    //have we reached a leaf or was the game already won?
    //then evaluate how good the current state is 
    if (node.getBoard().gameWasWon() || depth <= 0) {
      double evaluation = this.evaluator.evaluateBoard(node.getBoard());
      return evaluation;
    }

    if (maximizing) {
      ArrayList<Piece> piecesToCheck;

      //when we are still at the start, we have to work with the piece that was given to us
      if (node == rootNode) {
        piecesToCheck = new ArrayList<Piece>();
        piecesToCheck.add(this.getGivenPiece());
      } else {
        piecesToCheck = node.getBoard().getLeftoverPieces();
      }

      for (Piece currentPiece : piecesToCheck) {
        
        //walk through all possible moves on the board
        for (int xCoordinate = 0; xCoordinate < Board.BOARD_LENGTH; xCoordinate++) {
          for (int yCoordinate = 0; yCoordinate < Board.BOARD_LENGTH; yCoordinate++) {

            //check if the chosen field is really a valid move
            if (node.getBoard().fieldCanBeSet(xCoordinate, yCoordinate)) {
              //prepare the next node, this time minimizing
              Board nextBoard = new Board(node.getBoard());
              nextBoard.takePieceForOpponent(currentPiece);
              nextBoard.setField(xCoordinate, yCoordinate, currentPiece);
              MinimaxNode minimizeChild = new MinimaxNode(node, nextBoard, alpha, beta);
              alpha = Math.max(alpha, minimaxPruned(minimizeChild, depth - 1, alpha, beta, false));
              
              //prune here
              if(alpha >= beta){
                return alpha;
              }
            }
          }
        }
      }
      return alpha;
    } else {
      ArrayList<Piece> piecesToCheck = node.getBoard().getLeftoverPieces();
      for (Piece currentPiece : piecesToCheck) {
        
        //walk through all possible moves on the board
        for (int xCoordinate = 0; xCoordinate < Board.BOARD_LENGTH; xCoordinate++) {
          for (int yCoordinate = 0; yCoordinate < Board.BOARD_LENGTH; yCoordinate++) {

            //check if the chosen field is really a valid move
            if (node.getBoard().fieldCanBeSet(xCoordinate, yCoordinate)) {
              //prepare the next node, this time maximizing
              Board nextBoard = new Board(node.getBoard());
              nextBoard.takePieceForOpponent(currentPiece);
              nextBoard.setField(xCoordinate, yCoordinate, currentPiece);
              MinimaxNode maximizeChild = new MinimaxNode(node, nextBoard, alpha, beta);
              beta = Math.min(beta, minimaxPruned(maximizeChild, depth - 1, alpha, beta, true));
              
              //prune here
              if(beta <= alpha){
                return beta;
              }
            }
          }
        }
      }
      return beta;
    }
  }

  @Override
  public Piece selectPieceForOpponent() {
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      return this.randomPlayer.selectPieceForOpponent();
    } else {
      return this.selectPieceMinimax();
    }
  }

  private Piece selectPieceMinimax() {
    return new Piece(PieceColor.BLACK, PieceSize.BIG, PieceInnerShape.FLAT, PieceShape.ROUND);
  }
}
