/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quarto.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import quarto.Board;
import quarto.Piece;
import quarto.properties.PieceColor;
import quarto.properties.PieceInnerShape;
import quarto.properties.PieceShape;
import quarto.properties.PieceSize;

/**
 *
 * @author dominik
 */
public class RemotePlayer extends QuartoPlayer implements IRemotePlayer {

  private Socket socket;
  private Scanner scanner;
  private BufferedReader incoming;
  private PrintWriter outgoing;

  public RemotePlayer(Board board) {
    super(board);
    scanner = new Scanner(System.in);
    int portNumber = -1;

    System.out.println("Please specify the socket that should be used for connecting to the remote player (49152-65535):");
    int connectionTries = 0;
    while (portNumber < 0 && socket == null) {
      portNumber = scanner.nextInt();
      try {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        serverSocket.setSoTimeout(30000);
        System.out.println("Waiting for a connection of the remote player.");
        socket = serverSocket.accept();

        outgoing = new PrintWriter(socket.getOutputStream(), true);
        incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException e) {
        if (connectionTries <= 5) {
          connectionTries++;
          System.out.println("No connection could be established.");
        } else {
          break;
        }
      }
    }
    if (socket == null) {
      System.out.println("Sorry, no connection could be established. Exiting now.");
      System.exit(0);
    }
  }

  @Override
  public String makeMove() {
    if (socket.isConnected()) {
      try {
        String inputLine = incoming.readLine();
        
        //parse input line here
        
      } catch (IOException ex) {
        System.out.println("Couldn't read from the remote player.");
      }
    }
    return "";
  }

  @Override
  public Piece selectPieceForOpponent() {
    return new Piece(PieceColor.BLACK, PieceSize.BIG, PieceInnerShape.FLAT, PieceShape.ROUND);
  }

  @Override
  public void setGivenPiece(Piece piece) {
  }

  @Override
  public void sendMove(int x, int y, Piece pieceToSet) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
