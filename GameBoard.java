import javax.swing.JPanel;
import javax.swing.JMenu;
import java.awt.GridBagLayout;

abstract public class GameBoard extends JPanel {
	protected GamePackFrame gamePackFrame;
	protected GameBoard(GamePackFrame gamePackFrame) {
		super(new GridBagLayout());
		this.gamePackFrame = gamePackFrame;
	}
	abstract JMenu createMenu();
	abstract void createBoard();
}