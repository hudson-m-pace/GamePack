/*import java.io.*;
import javax.swing.*;
import java.net.*;

public class GameClient extends GameSocket {

	private String hostName;
	private int portNumber;
	private GameBoard gameBoard;
	private Thread receiver;
	private BufferedReader in;
	private PrintWriter out;

	public GameClient(String hostName, int portNumber, GameBoard gameBoard) throws IOException {

		this.hostName = hostName;
		this.portNumber = portNumber;
		this.gameBoard = gameBoard;
		receiver = new Thread(new Receiver());
		receiver.start();
	}

	public void close() {
		receiver.interrupt();
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public void receiveMessage(String message) {
		if (message.equals("ready")) {
			gameBoard.getReady();
		}
		else if (message.substring(0, 3).equals("hit")) {
			int thisOneX = Integer.parseInt(message.substring(3, 4));
			int thisOneY = Integer.parseInt(message.substring(4, 5));
			gameBoard.setMyTurn();

			if (gameBoard.tryToHit(gameBoard.playerPanel[thisOneX][thisOneY])) {
				sendMessage("result hit " + thisOneX + " " + thisOneY);
			}
			else {
				sendMessage("result miss " + thisOneX + " " + thisOneY);
			}

		}
		else if (message.substring(0, 6).equals("result")) {
			gameBoard.enemyShowResult(message.split(" "));
		}
		//gameBoard.gameText.setText("Client" + message);
	}

	private class Receiver implements Runnable {
		public void run() {
			try {
				Socket client = new Socket(hostName, portNumber);

				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				try {
					while (!Thread.interrupted()) {
						if (in.ready()) {
							receiveMessage(in.readLine());
						}	
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							
						}
					}
					return;
				} catch (IOException e) {
					receiveMessage("exception caught while trying to listen on port " + portNumber + ".");
					return;
				}

			} catch (UnknownHostException e) {
				receiveMessage("Don't know about host " + hostName + ".");
				return;
			} catch (IOException e) {
				receiveMessage("Couldn't get I/O for the connection to " + hostName + ".");
				return;
			}
		}
	}
}*/