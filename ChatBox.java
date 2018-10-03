import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ChatBox extends JDialog {

	private JTextArea messageBox;
	private String screenName;
	private GameSocketController gameSocketController;
	public static String PRIVATE_MESSAGE = "privatemsg";
	public static String PUBLIC_MESSAGE = "publicmsg";


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


	// Write a message to the chatbox.
	public void displayMessage(String message) {
		messageBox.append(message + "\n");
		messageBox.setCaretPosition(messageBox.getDocument().getLength());
	}


	// Runs when a user sends a message from their chat box. If it's a command, run it. Otherwise, send it to the interface.
	public void sendMessage(String[] message) {
		if (!message[1].equals("")) {
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
	}

	
	// Controls all commands for the chat box.
	public void runCommand(String command) {
		String[] commandArgs = command.split(" ");

		commandArgs[0] = commandArgs[0].toLowerCase();

		switch (commandArgs[0]) {
			case "setname":
				if (commandArgs.length == 2) {
					screenName = commandArgs[1];
					displayMessage("name set to " + screenName);
				}
				else {
					displayMessage("usage: /setname <new name>");
				}
				break;
			case "help":
				displayMessage("/help: display this message");
				displayMessage("/setname <new name>: changes your screen name to the specified value");
				break;
			default:
				displayMessage("command \"/" + command + "\" is not known. Type \"/help\" for a list of commands.");
				break;
		}
	}
}