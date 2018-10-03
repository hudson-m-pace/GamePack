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

	public GameSocketController(GamePackFrame gamePackFrame) {
		this.gamePackFrame = gamePackFrame;
		currentRole = "";
	}

	
	public void createMenu(JMenu connectionMenu) {
		JMenu hostMenu = new JMenu("host");
		JMenuItem hostServer = new JMenuItem("host a server");
		JMenuItem closeServer = new JMenuItem("close your server");
		closeServer.setEnabled(false);
		JMenuItem enableJoining = new JMenuItem("allow others to join");
		enableJoining.setEnabled(false);
		JMenuItem disableJoining = new JMenuItem("stop others from joining");
		disableJoining.setEnabled(false);

		hostMenu.add(hostServer);
		hostMenu.add(closeServer);
		hostMenu.add(enableJoining);
		hostMenu.add(disableJoining);
		connectionMenu.add(hostMenu);

		hostServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hostServer(getPortNumber())) {
					hostServer.setEnabled(false);
					closeServer.setEnabled(true);
					disableJoining.setEnabled(true);
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
				}
			}
		});
		enableJoining.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		disableJoining.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JMenu joinMenu = new JMenu("join");
		JMenuItem joinServer = new JMenuItem("join a server");
		JMenuItem leaveServer = new JMenuItem("disconnect from server");
		leaveServer.setEnabled(false);

		joinMenu.add(joinServer);
		joinMenu.add(leaveServer);
		connectionMenu.add(joinMenu);

		joinServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (connectToServer(getHostName(), getPortNumber())) {
					joinServer.setEnabled(false);
					leaveServer.setEnabled(true);
				}
			}
		});
		leaveServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (disconnect()) {
					joinServer.setEnabled(true);
					leaveServer.setEnabled(false);
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
				//chatBox.displayMessage("You must choose a positive port.");
			}
		} catch(NumberFormatException e) {
			//chatBox.displayMessage("Invalid input. The port number must be an integer.");
		}
		return -1;
	}


	public Boolean hostServer(int portNumber) {
		if (portNumber == -1) {
			return false;
		}
		try {
			socket = new ChatServer(portNumber); //, chatBox);
			screenName = "host: ";
			currentRole = "host";
			chatToSocketInterface.connectToSocket();
		} catch(IOException e) {
			//chatBox.displayMessage("Exception caught when trying to listen on port " + portNumber + ".");
			//chatBox.displayMessage(e.getMessage());
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
			chatToSocketInterface.connectToSocket();
		} catch(IOException e) {
			//chatBox.displayMessage("Exception caught when trying to listen on port " + portNumber + ".");
			//chatBox.displayMessage(e.getMessage());
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

	public void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface) {
		this.chatToSocketInterface = chatToSocketInterface;
	}
}