package quarto.players;

import quarto.Piece;

/**
 * Implementing players are capable of sending
 */
public interface IRemotePlayer {
  public void receiveSelectedPiece(Piece piece);
  
  public void receiveMove();
}
