import java.util.ArrayList;
import java.io.PrintWriter;

public interface GameSocket {
	void close();
	void sendMessage(String[] message);
	void receiveMessage(String[] message);
	void setChatToSocketInterface(ChatToSocketInterface chatToSocketInterface);
	ArrayList<PrintWriter> getOutputList();
}