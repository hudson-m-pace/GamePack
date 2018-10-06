import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer implements GameSocket {

	private Thread receiver, connectionListener;
	private int portNumber;
	private ChatBox chatBox;
	private ArrayList<BufferedReader> inList = new ArrayList<BufferedReader>();
	private ArrayList<PrintWriter> outList = new ArrayList<PrintWriter>();
	private ArrayList<String[]> userList = new ArrayList<String[]>();

	public ChatServer(int portNumber, ChatBox chatBox) throws IOException {

		this.portNumber = portNumber;
		this.chatBox = chatBox;

		connectionListener = new Thread(new ConnectionListener());
		connectionListener.start();
	}

	public void receiveMessage(String[] message) {
		if (message[0].equals(ChatBox.HOST_REQUEST)) {
			processHostRequest(message, -1);
		}
		else {
			chatBox.displayMessage(message[1]);
		}
	}

	public void sendMessage(String[] message) {
		if (message[0].equals(ChatBox.HOST_REQUEST)) {
			processHostRequest(message, -1);
		}
		else {
			sendToClients(message, -1);
		}
	}

	public void sendToClients(String[] message, int source) {
		for (int i = 0; i < outList.size(); i++) {
			if (i != source) {
				outList.get(i).println(message[1]);
			}
		}
	}
	public void sendToClient(String[] message, int destination) {
		outList.get(destination).println(message[1]);
	}


	public void close() {
		receiver.interrupt();
		connectionListener.interrupt();
		chatBox.displayMessage("Server closed.");
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

	public void processHostRequest(String[] request, int j) {
		String[] args = request[1].split(" ");
		switch (args[0]) {
			case "list":
				String userListString = "";
				for (int i = 0; i < userList.size(); i++) {
					if (i != 0) {
						userListString += "\n";
					}
					userListString = userListString + userList.get(i)[1] + "  :     " + userList.get(i)[0];
				}
				if (j == -1) {
					receiveMessage(new String[]{ChatBox.PUBLIC_MESSAGE, userListString});
				}
				else {
					sendToClient(new String[]{ChatBox.PUBLIC_MESSAGE, userListString}, j);
				}
				break;
			case "setname":
				userList.get(j)[1] = args[1];
				break;
		}
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
								if (message[0].equals(ChatBox.PUBLIC_MESSAGE)) {
									sendToClients(message, i);
									receiveMessage(message);
								}
								else if (message[0].equals(ChatBox.HOST_REQUEST)) {
									processHostRequest(message, i);
								}
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
				receiveMessage(new String[]{ChatBox.PRIVATE_MESSAGE, "starting server..."});
				ServerSocket serverSocket = new ServerSocket(portNumber);
				receiveMessage(new String[]{ChatBox.PRIVATE_MESSAGE, "server started on port " + portNumber + "."});

				receiver = new Thread(new Receiver());
				receiver.start();

				while (!Thread.interrupted()) {
					Socket clientSocket = serverSocket.accept();
					receiveMessage(new String[]{ChatBox.PRIVATE_MESSAGE, "connection from " + clientSocket.getInetAddress().getHostAddress() + "."});
					userList.add(new String[]{clientSocket.getInetAddress().getHostAddress(), ""});
					outList.add(new PrintWriter(clientSocket.getOutputStream(), true));
					inList.add(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
				}
			} catch(IOException e) {
				receiveMessage(new String[]{ChatBox.PRIVATE_MESSAGE, "Exception caught when trying to listen on port " + portNumber + " or listening for a connection"});
				receiveMessage(new String[]{ChatBox.PRIVATE_MESSAGE, e.getMessage()});
			}
		}
	}
}