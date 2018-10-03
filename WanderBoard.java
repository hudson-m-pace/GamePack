import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WanderBoard extends GameBoard {
	private GamePackFrame gamePackFrame;
	private Color green, blue, pink, orange;
	public WanderBoard(GamePackFrame gamePackFrame) {
		super(gamePackFrame);

		green = new Color(112, 255, 40);
		blue = new Color(40, 183, 255);
		pink = new Color(255, 40, 112);
		orange = new Color(255, 112, 40);

		setBackground(blue);

		JButton newGameButton = new JButton("Start A New Game");
		newGameButton.addActionListener(new NewGameButtonListener());
		add(newGameButton);

	}
	public JMenu createMenu() {
		return new JMenu();
	}
	public void createBoard() {
	}

	@Override
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
	}

	private class NewGameButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Wander.gameLoop();
		}
	}
}