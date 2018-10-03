import java.io.*;
import java.net.*;
import java.util.ArrayList;

abstract public class ChatSocket {
	protected static String KEY_CONSTANT = "coordinates";
	//protected GameBoard gameBoard;

	abstract public void close();

	abstract public void sendMessage(String[] message);

	abstract public void receiveMessage(String[] message);

	/*public void sendAttackCoordinates(int squareX, int squareY) {
		sendMessage(KEY_CONSTANT + " " + squareX + squareY);
	}*/

	/*public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}*/

}