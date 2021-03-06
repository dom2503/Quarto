package quarto.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import quarto.Board;
import quarto.Piece;

/**
 * This player makes it moves according to these rules (taken from the task document):
 *
 * It always places its piece in a winning formation, if possible. Otherwise, it chooses randomly
 * among the existing open cells.
 *
 * If given a choice among pieces to give the opponent, it always chooses one that cannot be used to
 * immediately win the game, if such a piece is available.
 */
public class NovicePlayer extends QuartoPlayer {

  public final static int BOARD_LENGTH = 4;
  private RandomPlayer random;

  public NovicePlayer(Board board) {
    super(board);
    random = new RandomPlayer(board);
  }

  @Override
  public void setGivenPiece(Piece piece) {
    super.setGivenPiece(piece);
    this.random.setGivenPiece(piece);
  }

  @Override
  public Point makeMove() {
    for (int i = 0; i < BOARD_LENGTH; i++) {
      for (int j = 0; j < BOARD_LENGTH; j++) {
        Board newBoard = new Board(this.getBoard());

        if (newBoard.setField(i, j, this.getGivenPiece()) && newBoard.gameWasWon()) {
          this.getBoard().setField(i, j, this.getGivenPiece());
          return new Point(i, j);
        }
      }
    }
    Point result = random.makeMove();
    this.setGivenPiece(null);
    return result;
  }

  @Override
  public Piece selectPieceForOpponent() {
    ArrayList<Piece> pieces = this.getBoard().getLeftoverPieces();
    Iterator<Piece> it = pieces.iterator();
    Piece selectedPiece = null;

    while (it.hasNext()) {
      Piece piece = it.next();
      boolean wasLoss = false;
      
      for (int i = 0; i < BOARD_LENGTH; i++) {
        for (int j = 0; j < BOARD_LENGTH; j++) {
          Board newBoard = new Board(this.getBoard());

          if (newBoard.setField(i, j, piece) && newBoard.gameWasWon()) {
            wasLoss = true;
          } 
        }
      }
      if(!wasLoss){
        selectedPiece = piece;
      }
    }
    if (selectedPiece == null) {
      selectedPiece = random.selectPieceForOpponent();
    }
    this.getBoard().takePieceForOpponent(selectedPiece);
    return selectedPiece;

  }
}
