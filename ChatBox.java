import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ChatBox extends JDialog {

	private JTextArea messageBox;
	private JFrame frame;
	private GameSocket socket;
	private JTextField entryLine;
	private String currentRole, screenName;
	private ChatToSocketInterface chatToSocketInterface;
	private Boolean connectedToSocket;
	public static String PRIVATE_MESSAGE = "privatemsg";
	public static String PUBLIC_MESSAGE = "publicmsg";

	public ChatBox() {
		currentRole = "";
		add(createChatBox());
		setSize(400, 300);
		setVisible(false);
		setTitle("chat-box");
	}

	public JMenu createMenu() { // creates the menu options for opening and closing the chat box. Added to the menubar in gamepackframe
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
		entryLine = new JTextField(20);
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

	public void sendMessage(String[] message) {
		if (!message[1].equals("")) {
			if (message[1].charAt(0) == '/') {
				runCommand(message[1].substring(1));
			}
			else {
				message[1] = screenName + ": " + message[1];
				displayMessage(message[1]);
				if (chatToSocketInterface != null) { //socket != null) {
					//socket.sendMessage(message);
					chatToSocketInterface.sendToSocket(message);
				}
			}
		}
	}

	public void setSocket(GameSocket socket) {
		this.socket = socket;
	}

	public String getRole() {
		return currentRole;
	}

	

	public void displayMessage(String message) {
		messageBox.append(message + "\n");
		messageBox.setCaretPosition(messageBox.getDocument().getLength());
	}

	//public void setGameBoard(GameBoard gameBoard) {
	//	this.gameBoard = gameBoard;
	//}

	

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
			default:
				displayMessage("command \"/" + command + "\" is not known. Type \"/help\" for a list of commands.");
				break;
		}
	}

	public void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface) {
		this.chatToSocketInterface = chatToSocketInterface;
	}
}