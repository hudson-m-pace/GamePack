import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameSocketController {
	private GamePackFrame gamePackFrame;
	private GameSocket socket;
	private ChatBox chatBox;
	private String role;

	public GameSocketController(GamePackFrame gamePackFrame) {
		this.chatBox = new ChatBox(this);
		this.gamePackFrame = gamePackFrame;
		role = "";
	}

	
	public JMenu createMenu() {

		JMenu connectionMenu = chatBox.createMenu();

		JMenu hostMenu = new JMenu("host");
		JMenuItem hostServer = new JMenuItem("host a server");
		JMenuItem closeServer = new JMenuItem("close your server");
		JMenuItem enableJoining = new JMenuItem("allow others to join");
		JMenuItem disableJoining = new JMenuItem("stop others from joining");

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
			public void actionPerformed(ActionEvent e) {
				if (role.equals("")) {
					hostServer(getPortNumber());
				}
			}
		});
		closeServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (role.equals("host")) {
					closeServer();
				}
			}
		});
		enableJoining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		disableJoining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		
		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (role.equals("")) {
					connectToServer(getHostName(), getPortNumber());
				}
			}
		});
		leaveServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (role.equals("client")) {
					disconnect();
				}
			}
		});

		return connectionMenu;
	}


	public GameSocket getSocket() {
		return socket;
	}


	public String getRole() {
		return role;
	}


	// Gets a port number from user.
	private int getPortNumber() {
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
	private String getHostName() {
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
			role = "host";
		} catch(IOException e) {
			chatBox.displayMessage("Exception caught while listening on port " + portNumber + ".");
			chatBox.displayMessage(e.getMessage());
			return false;
		}
		return true;
	}


	public Boolean closeServer() {
		if (role == "host") {
			socket.close();
			role = "";
			return true;
		}
		return false;
	}

	
	// Creates a new ChatClient based on the give host and port number.
	public Boolean connectToServer(String hostName, int portNumber) {
		if (portNumber < 0) {
			return false;
		}
		try {
			socket = new ChatClient(hostName, portNumber, chatBox);
			role = "client";
		} catch(IOException e) {
			chatBox.displayMessage("Exception caught while attempting to connect to " + hostName + ":" + portNumber + ".");
			chatBox.displayMessage(e.getMessage());
			return false;
		}
		return true;
	}


	public Boolean disconnect() {
		if (role == "client") {
			socket.close();
			role = "";
			return true;
		}
		return false;
	}
}