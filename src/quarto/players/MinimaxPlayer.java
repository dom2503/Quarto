package quarto.players;

import java.util.ArrayList;
import quarto.Board;
import quarto.Piece;
import quarto.players.minimax.BoardEvaluator;
import quarto.players.minimax.MinimaxNode;

/**
 * Uses the minimax algorithm to determine the next Quarto move.
 *
 * The first few moves are made randomly, minimax is only employed during the later stages of the
 * game.
 */
public class MinimaxPlayer extends QuartoPlayer {

  final private static int RANDOM_MOVES = 4; // the number of moves that should be done randomly in the beginning
  final private static boolean DEBUG = false;
  final private int depth;
  final private BoardEvaluator evaluator;
  final private RandomPlayer randomPlayer;
  private Board bestMove = null;
  private Piece bestPiece = null;
  private MinimaxNode rootNode;
  private int evaluated;

  /**
   * Default constructor with a search depth of 3.
   *
   * @param board
   */
  public MinimaxPlayer(Board board) {
    this(board, 3);
  }

  /**
   * @param board The current state of the game.
   * @param depth The number of moves that should be evaluated during the minimax search.
   */
  public MinimaxPlayer(Board board, int depth) {
    super(board);

    this.depth = depth;
    this.evaluator = new BoardEvaluator();
    this.randomPlayer = new RandomPlayer(board);
    this.rootNode = new MinimaxNode(null, this.getBoard());
  }

  private void resetState() {
    this.bestMove = null;
    this.bestPiece = null;
    this.evaluated = 0;
  }

  /**
   * Decides if a random or minimax move should be made.
   */
  @Override
  public String makeMove() {
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      String result = this.randomPlayer.makeMove();
      this.setGivenPiece(null);
      return result;
    } else {
      return this.makeMinimaxMove();
    }
  }

  /**
   * When the given piece changes, it also needs to be handed over to the random player.
   */
  @Override
  public void setGivenPiece(Piece piece) {
    super.setGivenPiece(piece);
    this.randomPlayer.setGivenPiece(piece);
  }

  /**
   * Runs the minimax algorithm on the current state of the game and makes the the appropriate move.
   */
  private String makeMinimaxMove() {
    this.resetState();

    this.maximize(this.rootNode, this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    System.out.println("Evaluated states: " + this.evaluated);
    return this.compareBoardsAndMakeMove(this.getBoard(), this.bestMove);
  }

  private double maximize(MinimaxNode node, int depth, double alpha, double beta) {
    //have we reached depth limit or was the game already won?
    //then evaluate how good the current state is 
    if (node.getBoard().gameWasWon() || node.getBoard().isDraw() || depth <= 0) {
      this.evaluated++;
      return -1 * this.evaluator.evaluateBoard(node.getBoard());
    }

    ArrayList<Piece> piecesToCheck;
    //when we are still at the start, we have to work with the piece that was given to us
    //otherwise we check with every piece
    if (node == this.rootNode && this.getGivenPiece() != null) {
      piecesToCheck = new ArrayList<Piece>();
      piecesToCheck.add(this.getGivenPiece());
    } else {
      piecesToCheck = node.getBoard().getLeftoverPieces();
    }

    for (Piece currentPiece : piecesToCheck) {
      //walk through all possible moves on the board
      for (int xCoordinate = 0; xCoordinate < Board.BOARD_LENGTH; xCoordinate++) {
        for (int yCoordinate = 0; yCoordinate < Board.BOARD_LENGTH; yCoordinate++) {

          Board nextBoard = this.prepareNextBoard(node.getBoard(), currentPiece, xCoordinate, yCoordinate);
          if (nextBoard != null) {
            if (node == this.rootNode && (bestMove == null || bestPiece == null)) {
              bestMove = nextBoard;
              bestPiece = currentPiece;
            }
            MinimaxNode minimizeChild = new MinimaxNode(node, nextBoard);
            double result = minimize(minimizeChild, depth - 1, alpha, beta);

            if (result > alpha) {
              alpha = result;
              if (node == rootNode) {
                this.bestMove = nextBoard;
                this.bestPiece = currentPiece;
              }
            }

            //prune here
            if (alpha >= beta) {
              return alpha;
            }
          }
        }
      }
    }
    return alpha;
  }

  private String compareBoardsAndMakeMove(Board oldBoard, Board newBoard) {
    Piece[][] oldFields = oldBoard.getBoard();
    Piece[][] newFields = newBoard.getBoard();
    String result = "";

    for (int i = 0; i < Board.BOARD_LENGTH; i++) {
      for (int j = 0; j < Board.BOARD_LENGTH; j++) {
        if (oldFields[i][j] != newFields[i][j]) {
          oldBoard.setField(i, j, this.getGivenPiece());
          this.setGivenPiece(null);
          result = "I made my move to " + (i + 1) + (char) (j + 65);
        }
      }
    }

    return result;
  }

  private double minimize(MinimaxNode node, int depth, double alpha, double beta) {
    //have we reached a leaf or was the game already won?
    //then evaluate how good the current state is 
    if (node.getBoard().gameWasWon() || node.getBoard().isDraw() || depth <= 0) {
      this.evaluated++;
      return this.evaluator.evaluateBoard(node.getBoard());
    }

    ArrayList<Piece> piecesToCheck = node.getBoard().getLeftoverPieces();
    for (Piece currentPiece : piecesToCheck) {

      //walk through all possible moves on the board
      for (int xCoordinate = 0; xCoordinate < Board.BOARD_LENGTH; xCoordinate++) {
        for (int yCoordinate = 0; yCoordinate < Board.BOARD_LENGTH; yCoordinate++) {

          Board nextBoard = this.prepareNextBoard(node.getBoard(), currentPiece, xCoordinate, yCoordinate);
          if (nextBoard != null) {
            MinimaxNode maximizeChild = new MinimaxNode(node, nextBoard);
            double result = maximize(maximizeChild, depth - 1, alpha, beta);

            if (result < beta) {
              beta = result;
            }

            //prune here
            if (beta <= alpha) {
              return beta;
            }
          }
        }
      }
    }
    return beta;
  }

  /**
   * Copies the old board, makes a move with the given piece and coordinates on the new board and
   * returns the new board.
   */
  private Board prepareNextBoard(Board oldBoard, Piece pieceToSet, int xCoordinate, int yCoordinate) {
    Board newBoard = null;

    //check if the chosen field is really a valid move
    if (oldBoard.fieldCanBeSet(xCoordinate, yCoordinate)) {
      newBoard = new Board(oldBoard);
      newBoard.takePieceForOpponent(pieceToSet);
      newBoard.setField(xCoordinate, yCoordinate, pieceToSet);
    }

    return newBoard;
  }

  @Override
  public Piece selectPieceForOpponent() {
    if (this.getBoard().getMoveCount() < RANDOM_MOVES) {
      return this.randomPlayer.selectPieceForOpponent();
    } else {
      return this.selectMinimaxPiece();
    }
  }

  private Piece selectMinimaxPiece() {
    this.resetState();

    this.maximize(this.rootNode, this.depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    this.getBoard().takePieceForOpponent(this.bestPiece);
    return this.bestPiece;
  }
}
