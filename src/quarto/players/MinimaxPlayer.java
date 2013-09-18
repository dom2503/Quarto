/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quarto.players;

import java.awt.Point;
import java.util.ArrayList;
import quarto.Board;
import quarto.Piece;
import quarto.players.minimax.BoardEvaluator;

/**
 *
 * @author dominik
 */
public class MinimaxPlayer extends QuartoPlayer {

  final private static int OUTSOURCED_MOVES = 3; // the number of moves that should be done randomly in the beginning
  final private Player startPlayer;
  final private int depth;
  final private BoardEvaluator evaluator;
  private int evaluated;
  private Point bestMove;
  private Piece bestPiece = null;

  /**
   * @param board The current state of the game.
   * @param depth The number of moves that should be evaluated during the minimax search.
   */
  public MinimaxPlayer(Board board, int depth) {
    super(board);

    this.depth = depth;
    this.evaluator = new BoardEvaluator();
    this.startPlayer = new NovicePlayer(board);
  }

  @Override
  public void setGivenPiece(Piece piece) {
    super.setGivenPiece(piece);
    this.startPlayer.setGivenPiece(piece);
  }

  @Override
  public Point makeMove() {
    if (this.getBoard().getMoveCount() < OUTSOURCED_MOVES) {
      Point result = this.startPlayer.makeMove();
      this.setGivenPiece(null);
      return result;
    } else {
      Point result = this.makeMaximizeMove();
      return result;
    }
  }

  private Point makeMaximizeMove() {
    this.bestMove = null;
    this.bestPiece = null;
    this.maximizeMove(this.getBoard(), this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, this.getGivenPiece());
    this.getBoard().setField(this.bestMove.x, this.bestMove.y, this.getGivenPiece());
    this.setGivenPiece(null);
    return this.bestMove;
  }

  private double maximizeMove(Board state, int depth, double alpha, double beta, Piece givenPiece) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return this.evaluator.evaluateBoard(state);
    }

    ArrayList<Piece> leftPieces = state.getLeftoverPieces();
    
    //initalize best piece with first piece of the left pieces
    if(state == this.getBoard() && this.bestPiece == null){
      this.bestPiece = leftPieces.get(0);
    }
    
    for (int x = 0; x < Board.BOARD_LENGTH; x++) {
      for (int y = 0; y < Board.BOARD_LENGTH; y++) {
        Board nextBoard = this.prepareNextBoard(state, x, y, givenPiece);
        if (nextBoard != null) {
          if (state == this.getBoard() && this.bestMove == null) {
            this.bestMove = new Point(x, y);
          }

          for (Piece currentPiece : leftPieces) {
            double result = minimizeMove(nextBoard, depth - 1, alpha, beta, currentPiece);

            if (result > alpha) {
              alpha = result;
              if (state == this.getBoard()) {
                this.bestMove = new Point(x, y);
                this.bestPiece = currentPiece;
              }
              if (alpha >= beta) {
                return alpha;
              }
            }
          }
        }
      }
    }

    return alpha;
  }

  private double minimizeMove(Board state, int depth, double alpha, double beta, Piece givenPiece) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return -1 * this.evaluator.evaluateBoard(state);
    }

    ArrayList<Piece> leftPieces = this.getBoard().getLeftoverPieces();

    for (int x = 0; x < Board.BOARD_LENGTH; x++) {
      for (int y = 0; y < Board.BOARD_LENGTH; y++) {
        Board nextBoard = this.prepareNextBoard(state, x, y, givenPiece);

        for (Piece currentPiece : leftPieces) {
          if (nextBoard != null) {
            double result = this.maximizeMove(nextBoard, depth - 1, alpha, beta, currentPiece);

            if (result < beta) {
              beta = result;
            }
            if (beta <= alpha) {
              return beta;
            }
          }
        }
      }
    }
    return beta;
  }

  private Board prepareNextBoard(Board oldBoard, int xPosition, int yPosition, Piece pieceToSet) {
    Board nextBoard = null;
    if (oldBoard.fieldCanBeSet(xPosition, yPosition)) {
      nextBoard = new Board(oldBoard);
      nextBoard.takePieceForOpponent(pieceToSet);
      nextBoard.setField(xPosition, yPosition, pieceToSet);
    }
    return nextBoard;
  }

  @Override
  public Piece selectPieceForOpponent() {
    if (this.getBoard().getMoveCount() < OUTSOURCED_MOVES || this.bestPiece == null) {
      return this.startPlayer.selectPieceForOpponent();
    } else {
      return this.bestPiece;
    }
  }

  public Piece selectMinimaxPiece() {
    this.bestMove = null;
    this.bestPiece = null;
    ArrayList<Piece> leftPieces = this.getBoard().getLeftoverPieces();
    double worstResult = Double.POSITIVE_INFINITY;
    for (Piece currentPiece : leftPieces) {
      double currentResult = this.maximizeMove(this.getBoard(), this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, currentPiece);
      if (currentResult > worstResult) {
        this.bestPiece = currentPiece;
      }
    }
    this.getBoard().takePieceForOpponent(this.bestPiece);
    return this.bestPiece;
  }
  
  @Override
  public void reset(){
    this.startPlayer.reset();
    this.bestPiece = null;
    this.bestMove = null;
  }
}
