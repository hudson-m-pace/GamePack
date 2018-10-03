import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameSocketController {
	private GamePackFrame gamePackFrame;
	private GameSocket socket;
	private ChatBox chatBox;
	private String currentRole, screenName;

	public GameSocketController(GamePackFrame gamePackFrame) {
		this.chatBox = new ChatBox(this);
		this.gamePackFrame = gamePackFrame;
		currentRole = "";
	}

	
	public JMenu createMenu() {

		JMenu connectionMenu = chatBox.createMenu();

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

		return connectionMenu;
	}


	public GameSocket getSocket() {
		return socket;
	}


	public String getRole() {
		return currentRole;
	}


	// Gets a port number from user.
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
				chatBox.displayMessage("You must choose a positive port.");
			}
		} catch(NumberFormatException e) {
			chatBox.displayMessage("The port number must be an integer");
		}
		return -1;
	}


	// Gets a host name from user.
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


	// Creates a new ChatServer on the given port.
	public Boolean hostServer(int portNumber) {
		if (portNumber == -1) {
			return false;
		}
		try {
			socket = new ChatServer(portNumber, chatBox);
			screenName = "host: ";
			currentRole = "host";
		} catch(IOException e) {
			chatBox.displayMessage("Exception caught while listening on port " + portNumber + ".");
			chatBox.displayMessage(e.getMessage());
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

	
	// Creates a new ChatClient based on the give host and port number.
	public Boolean connectToServer(String hostName, int portNumber) {
		if (portNumber == -1) {
			return false;
		}
		try {
			socket = new ChatClient(hostName, portNumber, chatBox);
			screenName = "client: ";
			currentRole = "client";
		} catch(IOException e) {
			chatBox.displayMessage("Exception caught while attempting to connect to " + hostName + ":" + portNumber + ".");
			chatBox.displayMessage(e.getMessage());
			return false;
		}
		return true;
	}


	public Boolean disconnect() {
		if (currentRole == "client") {
			socket.close();
			currentRole = "";
			return true;
		}
		return false;
	}
}