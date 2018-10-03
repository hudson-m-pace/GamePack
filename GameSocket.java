public interface GameSocket {
	void close();
	void sendMessage(String[] message);
	void receiveMessage(String[] message);
	void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface);
}