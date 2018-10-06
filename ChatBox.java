import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class ChatBox extends JDialog {

	private JTextArea messageBox;
	private String screenName;
	private GameSocketController gameSocketController;
	public static String PRIVATE_MESSAGE = "privatemsg";
	public static String PUBLIC_MESSAGE = "publicmsg";
	public static String HOST_REQUEST = "hostrequest";


	public ChatBox(GameSocketController gameSocketController) {
		this.gameSocketController = gameSocketController;

		add(createChatBox());
		setSize(600, 300);
		setVisible(false);
		setTitle("chat-box");
	}


	// Makes the chat menu and adds options for opening and closing the chat box. 
	public JMenu createMenu() {

		JMenu connectionMenu = new JMenu("connection");
		JMenuItem openChatWindowItem = new JMenuItem("open chat window");
		openChatWindowItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
			}
		});
		connectionMenu.add(openChatWindowItem);

		JMenuItem closeChatWindowItem = new JMenuItem("close chat window");
		closeChatWindowItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		connectionMenu.add(closeChatWindowItem);

		return connectionMenu;
	}


	public JPanel createChatBox() {

		JScrollPane scrollPane;
		JTextField entryLine;
		entryLine = new JTextField(20);

		//create the content-pane-to-be
		JPanel chatBox = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		chatBox.setOpaque(true);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 20;
		c.gridwidth = 2;

		//create a scrolled text area
		messageBox = new JTextArea(20, 30);
		messageBox.setEditable(false);
		messageBox.setLineWrap(true);
		scrollPane = new JScrollPane(messageBox);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		c.gridx = 0;
		c.gridy = 0;
		chatBox.add(scrollPane, c);

		Action sendMessageAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(new String[] {PUBLIC_MESSAGE, entryLine.getText()});
				entryLine.setText("");
			}
		};
		
		entryLine.addActionListener(sendMessageAction);
		c.gridy = 1;
		c.gridwidth = 1;
		c.weighty = 1;
		c.weightx = 8;
		chatBox.add(entryLine, c);

		JButton button = new JButton("send", null);
		button.addActionListener(sendMessageAction);
		button.setMnemonic(KeyEvent.VK_ENTER);
		c.gridx = 1;
		c.weightx = 2;
		chatBox.add(button, c);

		return chatBox;
	}


	public String getScreenName() {
		return screenName;
	}


	// Write a message to the chatbox.
	public void displayMessage(String message) {
		messageBox.append(message + "\n");
		messageBox.setCaretPosition(messageBox.getDocument().getLength());
	}


	// Runs when a user sends a message from their chat box. If it's a command, run it. Otherwise, send it to the interface.
	public void sendMessage(String[] message) {
		if (message[0].equals(PUBLIC_MESSAGE) && !message[1].equals("")) {
			if (message[1].charAt(0) == '/') {
				runCommand(message[1].substring(1));
			}
			else {
				message[1] = screenName + ": " + message[1];
				displayMessage(message[1]);
				if (!gameSocketController.getRole().equals("")) {
					gameSocketController.getSocket().sendMessage(message);
				}
			}
		}
		else if (message[0].equals(HOST_REQUEST)) {
			gameSocketController.getSocket().sendMessage(message);
		}
	}

	
	// Controls all commands for the chat box.
	public void runCommand(String command) {
		String[] commandArgs = command.split(" ");

		commandArgs[0] = commandArgs[0].toLowerCase();

		switch (commandArgs[0]) {

			case "help":
				displayMessage("/help: Display this message.");
				displayMessage("/closeServer: Close the server you're hosting.");
				displayMessage("/disconnect: Disconnect from the server you're on.");
				displayMessage("/host <port number>: Start a server on the specified port.");
				displayMessage("/join <host name> <port number>: Join a server at a specified host name and port number.");
				displayMessage("/kick <ip address>: Disconnect the specified user. Host only.");
				displayMessage("/list: List all of the currently connected users.");
				displayMessage("/pm <ip address> <message>: Send a private message to the specified ip.");
				displayMessage("/setname <new name>: Change your screen name to the specified value.");
				break;

			case "host":
				if (!gameSocketController.getRole().equals("")) {
					displayMessage("You have to leave your current server before opening a new one.");
					break;
				}
				if (commandArgs.length != 2) {
					displayMessage("Usage: /host <port number>");
					break;
				}
				try {
					gameSocketController.hostServer(Integer.parseInt(commandArgs[1]));
				} catch(NumberFormatException e) {
					displayMessage("The port number must be an integer.");
				}
				break;

			case "join":
				if (!gameSocketController.getRole().equals("")) {
					displayMessage("You have to leave your current server before joining a new one.");
					break;
				}
				if (commandArgs.length != 3) {
					displayMessage("Usage: /join <host name> <port number>");
					break;
				}
				try {
					gameSocketController.connectToServer(commandArgs[1], Integer.parseInt(commandArgs[2]));
				} catch(NumberFormatException e) {
					displayMessage("The port number must be an integer.");
				}
				break;

			case "kick":
				if (gameSocketController.getRole().equals("")) {
					displayMessage("You aren't hosting a server. There's nobody to kick");
				}
				else if (!gameSocketController.getRole().equals("host")) {
					displayMessage("Only the host can kick people.");
				}
				else {
					if (commandArgs.length == 2) {

					}
					else {
						displayMessage("usage: /kick <ip address>");
					}
				}
				break;

			case "list":
				if (gameSocketController.getRole().equals("")) {
					displayMessage("You're not connected to a server.");
				}
				else if (gameSocketController.getRole().equals("host")) {
					sendMessage(new String[]{HOST_REQUEST, "list"});
				}
				else {
					sendMessage(new String[]{HOST_REQUEST, "list"});
				}
				break;
				
			case "setname":
				if (!gameSocketController.getRole().equals("")) {
					displayMessage("You can't change your name while connected to a server.");
					break;
				}
				if (commandArgs.length == 2) {
					screenName = commandArgs[1];
					displayMessage("name set to " + screenName);
				}
				else {
					displayMessage("usage: /setname <new name>");
				}
				break;

			default:
				displayMessage("command \"/" + command + "\" is not known. Type \"/help\" for a list of commands.");
				break;
		}
	}
}