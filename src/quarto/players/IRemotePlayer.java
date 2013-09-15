package quarto.players;

/**
 * Implementing players are capable of sending and receiving data about moves and pieces
 * from a remote location.
 * 
 * All normal methods of the Player interface all need to send and receive data.
 */
public interface IRemotePlayer extends Player {
  
  /**
   * Sends a move the opponent made the remote player.
   * 
   * @param x
   * @param y
   * @param piece 
   */
  public void sendMove(int x, int y);
  
  /**
   * Allows for sending arbitrary messages.
   * @param message 
   */
  public void sendMessage(String message);
  
  /**
   * Retrieves some message from the player.
   */
  public String receiveMessage();
  
  /**
   * Closes the connection to the remote player.
   */
  public void close();
}
