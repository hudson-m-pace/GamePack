import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FifteenBoard extends GameBoard {
	private TileGrid tileGrid;
	private int width, height;
	private GamePackFrame gamePackFrame;

	public FifteenBoard(GamePackFrame gamePackFrame) {
		super(gamePackFrame);
 
		this.gamePackFrame = gamePackFrame;

		width = 4;
		height = 4;
		createBoard();
		setKeyBindings();
	}

	public JMenu createMenu() {
		JMenu fifteenMenu = new JMenu("fifteen");
		JMenuItem startNewGame = new JMenuItem("start a new game");
		JMenuItem changeBoardSize = new JMenuItem("change the board size");

		startNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createBoard();
			}
		});
		changeBoardSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getNewBoardSize()) {
					createBoard();
				}
			}
		});

		fifteenMenu.add(startNewGame);
		fifteenMenu.add(changeBoardSize);

		return fifteenMenu;
	}

	public void createBoard() {
		if (tileGrid != null) {
			remove(tileGrid);
		}
		tileGrid = new TileGrid(width, height);
		tileGrid.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(tileGrid, c);
		revalidate();
	}
	class GridMoveAction extends AbstractAction {
		private String direction;
		public GridMoveAction(String direction) {
			this.direction = direction;
		}
		public void actionPerformed(ActionEvent e) {
			tileGrid.move(direction);
		}
	}
	private void setKeyBindings() {
		ActionMap actionMap = getActionMap();
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = getInputMap(condition);

		inputMap.put(KeyStroke.getKeyStroke("UP"), "upPressed");
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");

		actionMap.put("upPressed", new GridMoveAction("up"));
		actionMap.put("downPressed", new GridMoveAction("down"));
		actionMap.put("leftPressed", new GridMoveAction("left"));
		actionMap.put("rightPressed", new GridMoveAction("right"));
	}
	private Boolean getNewBoardSize() {
		String input = (String)JOptionPane.showInputDialog("How big should the board be?");
		try {
			int newSize = Integer.parseInt(input);
			if (newSize < 1) {
				System.out.println("negative/zero input is not valid");
			}
			else {
				height = newSize;
				width = newSize;
				return true;
			}
		}
		catch (NumberFormatException e) {
			System.out.println("invalid input");
		}
		return false;
	}
}