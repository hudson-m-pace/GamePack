import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameSocketController {
	private GamePackFrame gamePackFrame;
	private GameSocket socket;
	private String currentRole, screenName;
	private ChatToSocketInterface chatToSocketInterface;

	public GameSocketController(ChatToSocketInterface chatToSocketInterface, GamePackFrame gamePackFrame) {
		this.chatToSocketInterface = chatToSocketInterface;
		this.gamePackFrame = gamePackFrame;
		currentRole = "";
	}

	
	public void createMenu(JMenu connectionMenu) {

		JMenu hostMenu = new JMenu("host");
		JMenuItem hostServer = new JMenuItem("host a server");
		JMenuItem closeServer = new JMenuItem("close your server");
		JMenuItem enableJoining = new JMenuItem("allow others to join");
		JMenuItem disableJoining = new JMenuItem("stop others from joining");

		closeServer.setEnabled(false);
		enableJoining.setEnabled(false);
		disableJoining.setEnabled(false);

		hostMenu.add(hostServer);
		hostMenu.add(closeServer);
		hostMenu.add(enableJoining);
		hostMenu.add(disableJoining);
		connectionMenu.add(hostMenu);


		JMenu joinMenu = new JMenu("join");
		JMenuItem joinServer = new JMenuItem("join a server");
		JMenuItem leaveServer = new JMenuItem("disconnect from server");

		leaveServer.setEnabled(false);

		joinMenu.add(joinServer);
		joinMenu.add(leaveServer);
		connectionMenu.add(joinMenu);



		hostServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hostServer(getPortNumber())) {
					hostServer.setEnabled(false);
					closeServer.setEnabled(true);
					enableJoining.setEnabled(true);
					joinServer.setEnabled(false);
				}
			}
		});
		closeServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (closeServer()) {
					hostServer.setEnabled(true);
					closeServer.setEnabled(false);
					enableJoining.setEnabled(false);
					disableJoining.setEnabled(false);
					joinServer.setEnabled(true);
				}
			}
		});
		enableJoining.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableJoining.setEnabled(false);
				disableJoining.setEnabled(true);
			}
		});
		disableJoining.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableJoining.setEnabled(true);
				disableJoining.setEnabled(false);
			}
		});

		

		joinServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (connectToServer(getHostName(), getPortNumber())) {
					joinServer.setEnabled(false);
					leaveServer.setEnabled(true);
					hostServer.setEnabled(false);
				}
			}
		});
		leaveServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (disconnect()) {
					joinServer.setEnabled(true);
					leaveServer.setEnabled(false);
					hostServer.setEnabled(true);
				}
			}
		});
	}

	public int getPortNumber() {
		String input = (String)JOptionPane.showInputDialog(
			(JFrame)gamePackFrame,
			"port number:",
			"port getter",
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			"");
		try {
			int portChoice = Integer.parseInt(input);
			if (portChoice > 0) {
				return portChoice;
			}
			else {
				chatToSocketInterface.sendToChat(new String[] {ChatBox.PRIVATE_MESSAGE, "You must choose a positive port."});
			}
		} catch(NumberFormatException e) {
			chatToSocketInterface.sendToChat(new String[] {ChatBox.PRIVATE_MESSAGE, "The port number must be an integer."});
		}
		return -1;
	}


	public Boolean hostServer(int portNumber) {
		if (portNumber == -1) {
			return false;
		}
		try {
			socket = new ChatServer(portNumber);
			screenName = "host: ";
			currentRole = "host";
			chatToSocketInterface.connectToSocket(socket);
		} catch(IOException e) {
			chatToSocketInterface.sendToChat(new String[] {ChatBox.PRIVATE_MESSAGE, "Exception caught while listening on port " + portNumber + "."});
			chatToSocketInterface.sendToChat(new String[] {ChatBox.PRIVATE_MESSAGE, e.getMessage()});
			return false;
		}
		return true;
	}

	public String getHostName() {
		String input = (String)JOptionPane.showInputDialog(
			gamePackFrame,
			"Enter the host name",
			"host name getter",
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			"");
		return input;
	}
	public Boolean connectToServer(String hostName, int portNumber) {
		if (portNumber == -1) {
			return false;
		}
		try {
			socket = new ChatClient(hostName, portNumber); //, chatBox);
			screenName = "client: ";
			currentRole = "client";
			chatToSocketInterface.connectToSocket(socket);
		} catch(IOException e) {
			//chatBox.displayMessage("Exception caught when trying to listen on port " + portNumber + ".");
			//chatBox.displayMessage(e.getMessage());
			chatToSocketInterface.sendToChat(new String[] {ChatBox.PRIVATE_MESSAGE, "Could not connect."});
			return false;
		}
		return true;
	}

	public Boolean closeServer() {
		if (currentRole == "host") {
			socket.close();
			currentRole = "";
			return true;
		}
		return false;
	}

	public Boolean disconnect() {
		if (currentRole == "client") {
			socket.close();
			currentRole = "";
			return true;
		}
		return false;
	}

	public GameSocket getSocket() {
		return socket;
	}
}