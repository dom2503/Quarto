/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quarto.players;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
  private ServerSocket serverSocket;

  public RemotePlayer(Board board) {
    super(board);
    scanner = new Scanner(System.in);
    int portNumber = -1;

    System.out.println("Please specify the port that should be opened for connecting to the remote player:");
    while (portNumber < 0) {
      portNumber = scanner.nextInt();
      try {
        serverSocket = new ServerSocket(portNumber);

        //wait for the player to connect
        serverSocket.setSoTimeout(30000);
        System.out.println("Waiting for a connection of the remote player.");
        socket = serverSocket.accept();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }

    if (socket != null) {
      try {
        outgoing = new PrintWriter(socket.getOutputStream());
        incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException ex) {
        System.out.println("Error establishing the connection.");
      }
    } else {
      System.out.println("Sorry, no connection could be established. Exiting now.");
      System.exit(0);
    }
  }

  @Override
  public Point makeMove() {
    Point move = null;
    if (socket.isConnected()) {
      this.sendMessage("Please send your move in format: \"x, y\". 0, 0 is upper left corner, 4, 4 lower right.\n");
      String inputLine = this.receiveMessage();

      List<String> items = Arrays.asList(inputLine.split("\\s*,\\s*"));
      int xPosition = Integer.parseInt(items.get(0));
      int yPosition = Integer.parseInt(items.get(1));

      this.getBoard().setField(xPosition, yPosition, this.getGivenPiece());
      move = new Point(xPosition, yPosition);

    }
    return move;
  }

  @Override
  public Piece selectPieceForOpponent() {
    Piece selectedPiece = null;
    if (socket.isConnected()) {
      this.sendMessage("Please send piece in a binary format => 1100 (1/0 => Black/Red, Small/Big, Flat/Hole, Round/Squaer).\n");
      String inputLine = this.receiveMessage();

      char[] binaryValues = inputLine.toCharArray();

      Piece describedPiece = new Piece(
              PieceColor.values()[Character.getNumericValue(binaryValues[0])],
              PieceSize.values()[Character.getNumericValue(binaryValues[1])],
              PieceInnerShape.values()[Character.getNumericValue(binaryValues[2])],
              PieceShape.values()[Character.getNumericValue(binaryValues[3])]);

      ArrayList<Piece> leftPieces = this.getBoard().getLeftoverPieces();

      for (Piece currentPiece : leftPieces) {
        if (describedPiece.equals(currentPiece)) {
          selectedPiece = currentPiece;
        }
      }

    }
    if (selectedPiece != null) {
      this.getBoard().takePieceForOpponent(selectedPiece);
    }
    return selectedPiece;
  }

  @Override
  public void setGivenPiece(Piece piece) {
    super.setGivenPiece(piece);
    if (piece == null) {
      this.sendMessage("\n");
      return;
    }
    String piecebinaryRepresentation = "" + piece.color.ordinal() + piece.size.ordinal() + piece.innerShape.ordinal() + piece.shape.ordinal();

    this.sendMessage(piecebinaryRepresentation);
  }

  @Override
  public void sendMove(int x, int y) {
    this.sendMessage(x + ", " + y);
  }

  @Override
  public void sendMessage(String message) {
    if (socket.isConnected()) {
      outgoing.print(message);
      outgoing.flush();
    }
  }

  @Override
  public String receiveMessage() {
    String result = "";
    if (socket.isConnected()) {
      try {
        result = incoming.readLine();
      } catch (IOException ex) {
        result = "Couldn't read from the player.";
      }
    }
    return result;
  }

  @Override
  public void close() {
    try {
      outgoing.close();
      incoming.close();
      socket.close();
      serverSocket.close();
    } catch (IOException ex) {
      System.out.println("An error occured while closing the connections.");
    }
  }

  @Override
  public void reset() {
    super.reset();
    this.sendMessage("Please reset yourself for the next game.\n");
  }
}
