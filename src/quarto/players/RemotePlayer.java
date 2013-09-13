/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quarto.players;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import quarto.Board;
import quarto.Piece;

/**
 *
 * @author dominik
 */
public class RemotePlayer extends QuartoPlayer implements IRemotePlayer{

  private int port;
  private ServerSocket socket;
  private Socket port;

  public RemotePlayer(Board board, int port) throws IOException {
    super(board);

    if (port > -1) {
      if (port < 20000) {
        this.socket = new ServerSocket(port);
        this.port = this.socket.accept();
      } else {
        throw new IllegalArgumentException("The port number is invalid.");
      }
    }
  }

  @Override
  public String makeMove() {
    this.por
  }

  @Override
  public Piece selectPieceForOpponent() {
    //
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
