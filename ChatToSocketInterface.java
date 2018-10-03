import java.util.ArrayList;
import java.io.PrintWriter;

public class ChatToSocketInterface {
	private ChatBox chatBox;
	private GameSocket gameSocket;

	public ChatToSocketInterface(ChatBox chatBox) {
		this.chatBox = chatBox;
		chatBox.setChatToSocketInterface(this);
	}

	public void sendToChat(String[] message) {
		if (message[0].equals("privatemsg")) {
			chatBox.displayMessage(message[1]);
		}
		else if (message[1].equals("publicmsg")) {
			ArrayList<PrintWriter> outList = gameSocket.getOutputList();
			chatBox.displayMessage(message[1]);
			for (int i = 0; i < outList.size(); i++) {
				outList.get(i).println(message[1]);
			}
		}
	}

	public void sendToSocket(String[] message) {
		if (gameSocket != null) {
			gameSocket.sendMessage(message);
		}
	}

	public void connectToSocket(GameSocket socket) {
		gameSocket = socket;
		gameSocket.setChatToSocketInterface(this);
	}

}