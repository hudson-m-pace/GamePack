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
	private ChatToSocketInterface chatToSocketInterface;
	private Boolean socketConnected;

	public ChatClient(String hostName, int portNumber) throws IOException {

		this.hostName = hostName;
		this.portNumber = portNumber;

		socketConnected = false;

		receiver = new Thread(new Receiver());
		receiver.start();
	}

	public ArrayList<PrintWriter> getOutputList() {
		return null;
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
		//chatBox.displayMessage(message[1]);
		if (socketConnected) {
			chatToSocketInterface.sendToChat(message);
		}
	}


	private class Receiver implements Runnable {

		public void run() {
			try {
				receiveMessage(new String[] {"chatmsg", "trying to connect to server..."});
				Socket server = new Socket(hostName, portNumber);
				receiveMessage(new String[] {"chatmsg", "connected."});

				out = new PrintWriter(server.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				try {
					while (!Thread.interrupted()) {
						String[] message = new String[2];
						if (in.ready()) {
							message[0] = in.readLine();
							message[1] = in.readLine();
							//sendMessage(message);
							receiveMessage(message);
						}	
						Thread.sleep(200);
					}
					return;
				} catch (IOException e) {
					receiveMessage(new String[] {"chatmsg", "exception caught while trying to listen on port " + portNumber + "."});
					return;
				} catch (InterruptedException e) {
					receiveMessage(new String[] {"chatmsg", "thread was interrupted!"});
				}

			} catch (UnknownHostException e) {
				receiveMessage(new String[] {"chatmsg", "Don't know about host " + hostName + "."});
				return;
			} catch (IOException e) {
				receiveMessage(new String[] {"chatmsg", "Couldn't get I/O for the connection to " + hostName + "."});
				return;
			}
		}
	}

	public void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface) {
		this.chatToSocketInterface = chatToSocketInterface;
		socketConnected = true;
		//chatToSocketInterface.connectToSocket();
	}
}