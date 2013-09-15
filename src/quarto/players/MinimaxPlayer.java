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

  final private static int RANDOM_MOVES = 3; // the number of moves that should be done randomly in the beginning
  final private RandomPlayer randomPlayer;
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
    this.randomPlayer = new RandomPlayer(board);
  }

  @Override
  public void setGivenPiece(Piece piece) {
    super.setGivenPiece(piece);
    this.randomPlayer.setGivenPiece(piece);
  }

  @Override
  public String makeMove() {
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      String result = this.randomPlayer.makeMove();
      this.setGivenPiece(null);
      //System.out.println("Left pieces: " + this.getBoard().getLeftoverPieces());
      return result;
    } else {
      String result = this.makeMaximizeMove();
      //System.out.println("Left pieces: " + this.getBoard().getLeftoverPieces());
      //System.out.println("Evaluated " + this.evaluated + " nodes.");
      return result;
    }
  }

  private String makeMaximizeMove() {
    this.bestMove = null;
    this.bestPiece = null;
    this.maximizeMove(this.getBoard(), this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    this.getBoard().setField(this.bestMove.x, this.bestMove.y, this.getGivenPiece());
    this.setGivenPiece(null);
    return "I made my move to " + this.bestMove;
  }

  private double maximizeMove(Board state, int depth, double alpha, double beta) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return -1 * this.evaluator.evaluateBoard(state, depth);
    }

    ArrayList<Piece> leftPieces = new ArrayList<Piece>();
    if (state == this.getBoard() && this.getGivenPiece() != null) {
      leftPieces.add(this.getGivenPiece());
    } else {
      leftPieces = state.getLeftoverPieces();
    }

    for (Piece currentPiece : leftPieces) {
      for (int x = 0; x < Board.BOARD_LENGTH; x++) {
        for (int y = 0; y < Board.BOARD_LENGTH; y++) {
          Board nextBoard = this.prepareNextBoard(state, x, y, currentPiece);

          if (nextBoard != null) {
            if (state == this.getBoard() && this.bestMove == null) {
              this.bestMove = new Point(x, y);
            }
            double result = minimizeMove(nextBoard, depth - 1, alpha, beta);

            if (result > alpha) {
              alpha = result;
              if (state == this.getBoard()) {
                this.bestMove = new Point(x, y);
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

  private double minimizeMove(Board state, int depth, double alpha, double beta) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return this.evaluator.evaluateBoard(state, depth);
    }

    ArrayList<Piece> leftPieces = this.getBoard().getLeftoverPieces();

    for (Piece currentPiece : leftPieces) {
      for (int x = 0; x < Board.BOARD_LENGTH; x++) {
        for (int y = 0; y < Board.BOARD_LENGTH; y++) {
          Board nextBoard = this.prepareNextBoard(state, x, y, currentPiece);
          if (nextBoard != null) {
            double result = this.maximizeMove(nextBoard, depth - 1, alpha, beta);

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
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      return this.randomPlayer.selectPieceForOpponent();
    } else {
      return this.selectMinimaxPiece();
    }
  }

  public Piece selectMinimaxPiece() {
    this.bestMove = null;
    this.bestPiece = null;
    this.maximizePiece(this.getBoard(), this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    this.getBoard().takePieceForOpponent(this.bestPiece);
    return this.bestPiece;
  }

  private double maximizePiece(Board state, int depth, double alpha, double beta) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return -1 * this.evaluator.evaluateBoard(state, depth);
    }

    ArrayList<Piece> leftPieces = state.getLeftoverPieces();

    for (Piece currentPiece : leftPieces) {
      if (state == this.getBoard() && this.bestPiece == null) {
        this.bestPiece = currentPiece;
      }
      double localMinimum = Double.POSITIVE_INFINITY;

      for (int x = 0; x < Board.BOARD_LENGTH; x++) {
        for (int y = 0; y < Board.BOARD_LENGTH; y++) {
          Board nextBoard = this.prepareNextBoard(state, x, y, currentPiece);

          if (nextBoard != null) {
            double result = minimizePiece(nextBoard, depth - 1, alpha, beta);

            if (result < localMinimum) {
              localMinimum = result;
            }
          }
        }
      }
      if (localMinimum > alpha) {
        this.bestPiece = currentPiece;
        alpha = localMinimum;
      }
      if (alpha >= beta) {
        return alpha;
      }
    }

    return alpha;
  }

  private double minimizePiece(Board state, int depth, double alpha, double beta) {
    if (state.gameWasWon() || state.isDraw() || depth <= 0) {
      this.evaluated++;
      return this.evaluator.evaluateBoard(state, depth);
    }

    ArrayList<Piece> leftPieces = this.getBoard().getLeftoverPieces();

    for (Piece currentPiece : leftPieces) {
      for (int x = 0; x < Board.BOARD_LENGTH; x++) {
        for (int y = 0; y < Board.BOARD_LENGTH; y++) {
          Board nextBoard = this.prepareNextBoard(state, x, y, currentPiece);
          if (nextBoard != null) {
            double result = this.maximizePiece(nextBoard, depth - 1, alpha, beta);

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
}
