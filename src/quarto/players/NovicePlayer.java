package quarto.players;

import java.util.ArrayList;
import java.util.Iterator;
import quarto.Board;
import quarto.Piece;

/**
 * This player makes it moves according to thes rules (taken from 
 * the task document):
 * 
 * It always places its piece in a winning formation, if possible. 
 * Otherwise, it chooses randomly among the existing open cells.
 * 
 * If given a choice among pieces to give the opponent, it always chooses 
 * one that cannot be used to immediately win the game, if such a piece 
 * is available.
 */
public class NovicePlayer extends QuartoPlayer {

    public final static int BOARD_LENGTH = 4;
    private ArrayList<Piece> pieces;
    private RandomPlayer random;

    public NovicePlayer(Board board) {
        super(board);
        random = new RandomPlayer(this.getBoard());

    }

    @Override
    public String makeMove() {
        boolean moveMade = false;

        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Board newBoard = new Board(this.getBoard());


                if (newBoard.setField(i, j, this.getGivenPiece()) && newBoard.gameWasWon() && !moveMade) {
                    this.getBoard().setField(i, j, this.getGivenPiece());
                    moveMade = true;
                    return "I made my move to " + (i + 1) + (char) (j + 65);
                }
            }
        }
        if (!moveMade) {
            random.makeMove();
            this.setGivenPiece(null);
        }
        return "";
    }

    @Override
    public Piece selectPieceForOpponent() {
        this.pieces = new ArrayList<Piece>(this.getBoard().getLeftoverPieces());
        Iterator<Piece> it = pieces.iterator();

        while (it.hasNext()) {
            Piece piece = it.next();

            for (int i = 0; i < BOARD_LENGTH; i++) {
                for (int j = 0; j < BOARD_LENGTH; j++) {
        Board newBoard = new Board(this.getBoard());

                    if (newBoard.setField(i, j, piece) && !newBoard.gameWasWon() ) {
                        piece = this.getBoard().takePieceForOpponent(piece);
                        return piece;
                    }
                }
            }





        }
        return random.selectPieceForOpponent();

    }
}
