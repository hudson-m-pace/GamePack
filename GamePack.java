import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GamePack {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new GamePackFrame();
			}
		});
	}
}