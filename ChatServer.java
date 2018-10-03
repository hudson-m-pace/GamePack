import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer implements GameSocket {
	private ChatBox chatBox;
	private Thread receiver, connectionListener;
	private int portNumber;
	private ArrayList<BufferedReader> inList = new ArrayList<BufferedReader>();
	private ArrayList<PrintWriter> outList = new ArrayList<PrintWriter>();
	private ChatToSocketInterface chatToSocketInterface;
	private Boolean socketConnected;

	public ChatServer(int portNumber) throws IOException { //ChatBox chatBox) throws IOException {

		//this.chatBox = chatBox;
		this.portNumber = portNumber;
		//this.chatBox.setSocket(this);

		socketConnected = false;

		connectionListener = new Thread(new ConnectionListener());
		connectionListener.start();
	}

	public void receiveMessage(String[] message) {
		if (chatToSocketInterface != null) {
			chatToSocketInterface.sendToChat(message);
		}
	}

	public void sendMessage(String[] message) {
		sendToClients(message, -1);
	}

	public void sendToClients(String[] message, int source) {
		for (int i = 0; i < outList.size(); i++) {
			if (i != source) {
				outList.get(i).println(message[0]);
				outList.get(i).println(message[1]);
			}
		}
	}

	public void close() {
		receiver.interrupt();
		connectionListener.interrupt();
		if (chatToSocketInterface != null) {
			chatToSocketInterface.sendToChat(new String[] {"privatemsg", "server closed"});
		}
	}

	public Boolean stopAllowingConnections() {
		if (connectionListener.isAlive()) {
			connectionListener.interrupt();
			return true;
		}
		return false;
	}
	public Boolean continueAllowingConnections() {
		if (!connectionListener.isAlive()) {
			connectionListener.start();
			return true;
		}
		return false;
	}

		

	private class Receiver implements Runnable {

		public void run() {
			try {
				ArrayList<BufferedReader> currentList = new ArrayList<BufferedReader>(inList);
				String[] message = new String[2];

				while (!Thread.interrupted()) {
					if (currentList.equals(inList)) {
						for (int i = 0; i < currentList.size(); i++) {
							if (currentList.get(i).ready()) {
								message[0] = currentList.get(i).readLine();
								message[1] = currentList.get(i).readLine();
								sendToClients(message, i);
								receiveMessage(message);
							}
						} 
					}
					else {
						currentList = new ArrayList<BufferedReader>(inList);
					}
					Thread.sleep(200);
				}

			} catch (IOException e) {
				System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
				System.out.println(e.getMessage());
			} catch (InterruptedException e) {
				System.out.println("Thread was interrupted!");
			}
		}
	}

	private class ConnectionListener implements Runnable {

		public void run() {
			try {
				receiveMessage(new String[] {"privatemsg", "starting server..."});
				ServerSocket serverSocket = new ServerSocket(portNumber);
				receiveMessage(new String[] {"privatemsg", "server started on port " + portNumber + "."});

				receiver = new Thread(new Receiver());
				receiver.start();

				while (!Thread.interrupted()) {
					Socket clientSocket = serverSocket.accept();
					receiveMessage(new String[] {"chatmsg", "connection from " + clientSocket.getInetAddress() + "."});
					outList.add(new PrintWriter(clientSocket.getOutputStream(), true));
					inList.add(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
				}
			} catch(IOException e) {
				receiveMessage(new String[] {"chatmsg", "Exception caught when trying to listen on port " + portNumber + " or listening for a connection"});
				receiveMessage(new String[] {"chatmsg", e.getMessage()});
			}
		}
	}

	public void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface) {
		this.chatToSocketInterface = chatToSocketInterface;
		//chatToSocketInterface.connectToSocket();
	}
}