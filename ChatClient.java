import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatClient implements GameSocket {

	private String hostName;
	private int portNumber;
	private ChatBox chatBox;
	private Thread receiver;
	private PrintWriter out;
	private BufferedReader in;
	private Boolean socketConnected;

	public ChatClient(String hostName, int portNumber, ChatBox chatBox) throws IOException {

		this.hostName = hostName;
		this.portNumber = portNumber;
		this.chatBox = chatBox;

		socketConnected = false;

		receiver = new Thread(new Receiver());
		receiver.start();
	}

	public void close() {
		receiver.interrupt();
		receiveMessage(new String[] {"chatmsg", "disconnected."});
	}

	public void sendMessage(String[] message) {
		out.println(message[0]);
		out.println(message[1]);
	}

	public void receiveMessage(String[] message) {
		chatBox.displayMessage(message[1]);
	}


	private class Receiver implements Runnable {

		public void run() {
			try {
				chatBox.displayMessage("Trying to connect to server...");
				Socket server = new Socket(hostName, portNumber);
				chatBox.displayMessage("Connected.");

				out = new PrintWriter(server.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				try {
					while (!Thread.interrupted()) {
						String[] message = new String[2];
						if (in.ready()) {
							message[0] = in.readLine();
							message[1] = in.readLine();
							receiveMessage(message);
						}	
						Thread.sleep(200);
					}
					return;
				} catch (IOException e) {
					chatBox.displayMessage("Exception caught while trying to listen on port " + portNumber + ".");
					return;
				} catch (InterruptedException e) {
					chatBox.displayMessage("Thread was interrupted.");
				}

			} catch (UnknownHostException e) {
				chatBox.displayMessage("Don't know about host " + hostName + ".");
				return;
			} catch (IOException e) {
				chatBox.displayMessage("Couldn't get I/O for the connection to " + hostName + ".");
				return;
			}
		}
	}
}