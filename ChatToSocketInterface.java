public class ChatToSocketInterface {
	private ChatBox chatBox;
	private GameSocket gameSocket;
	private GameSocketController gameSocketController;

	public ChatToSocketInterface(GameSocketController gameSocketController, ChatBox chatBox) {
		this.gameSocketController = gameSocketController;
		this.chatBox = chatBox;
		gameSocketController.setChatToSocketInterface(this);
		chatBox.setChatToSocketInterface(this);
		//gameSocketController.connectToSocket(this);
		//chatBox.connectToSocket(this);
	}

	public void sendToChat(String[] message) {
		//if (message[0].equals("privatemsg")) {
			chatBox.displayMessage(message[1]);
		//}
	}

	public void sendToSocket(String[] message) {
		if (gameSocket != null) {
			gameSocket.sendMessage(message);
		}
		else {
			System.out.println("error");
		}
	}

	public void connectToSocket() {
		gameSocket = gameSocketController.getSocket();
		gameSocket.setChatToSocketInterface(this);
	}

}