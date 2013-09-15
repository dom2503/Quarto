package quarto.players;

import quarto.Piece;

/**
 * Implementing players are capable of sending
 */
public interface IRemotePlayer {
  public void sendMove(int x, int y, Piece piece);
}
