import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class BattleshipBoard extends GameBoard {
	private String currentMode;
	public static String PLACE_SHIPS = "place";
	private static String ATTACK = "attack";
	private static String WAIT_FOR_ATTACK = "waitForAttack";
	public static String MAIN_GAME = "mainGame";
	private GameSquare[][] enemyPanel, playerPanel;
	private Boat currentBoat;
	private int remainingBoats;
	private JTextArea messageBox;
	private JFrame frame;
	private JTextField entryLine;
	private String currentRole, screenName;
	private GameBoard gameBoard;
	private JLabel gameText;
	private Enemy enemy;
	private Player player;
	private JPanel textPanel;


	public BattleshipBoard(GamePackFrame gamePackFrame) {
		super(gamePackFrame);

		currentMode = PLACE_SHIPS;
		
		enemyPanel = new GameSquare[10][10];
		playerPanel = new GameSquare[10][10];
		gameText = new JLabel();
		
		enemy = new Enemy(enemyPanel, this);
		player = new Player(playerPanel, this);

		createBoard();

	}

	public JMenu createMenu() {
		JMenu menu = new JMenu("battleship");
		JMenuItem exitMenuItem = new JMenuItem("return to menu");
		menu.add(exitMenuItem);
		return menu;
	}

	public void createBoard() {
		GridBagConstraints c = new GridBagConstraints();
		JPanel board = new JPanel(new GridBagLayout());
		textPanel = new JPanel(new GridBagLayout());
		board.setOpaque(true);
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		board.add(createBoardSection(enemyPanel, "enemy"), c);
		c.gridy = 1;
		c.weightx = .1;
		c.weighty = .1;
		textPanel.add(gameText);
		board.add(textPanel, c);
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		board.add(createBoardSection(playerPanel, "player"), c);

		c.gridx = 0;
		c.gridy = 0;
		add(board, c);
	}

	public Container createBoardSection(GameSquare[][] squares, String role) {
		JPanel board = new JPanel(new GridBagLayout());
		board.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		board.setOpaque(true);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < squares.length; i++) {
			c.gridx = i;
			for (int j = 0; j < squares[i].length; j++) {

				if (role.equals("enemy")) {
					squares[i][j] = new EnemyGameSquare(i, j, role, playerPanel, enemy, this, player);
				}
				else if (role.equals("player")) {
					squares[i][j] = new PlayerGameSquare(i, j, role, playerPanel, this, player, enemy);
				}

				c.gridy = j;
				board.add(squares[i][j], c);
			}
		}
		return board;
	}


	public void changeGameMode() {
		currentMode = MAIN_GAME;
	}

	public void updateTargetList() {
		Boat[] boatList = enemy.getBoatList();
		String targetListString = "enemy ships:";
		for (int i = 0; i < boatList.length; i++) {
			targetListString += "    " + boatList[i].getName() + "[ " + boatList[i].getIsSunk() + " ]";
		}
		gameText.setText(targetListString);
	}

	public Boolean tryToHit(GameSquare current) {
		if (current.hasBoat()) {
			current.setColor(GameSquare.HIT);
			current.select();
			current.setHit();
			return true;
		}
		else {
			current.setColor(GameSquare.MISS);
			current.select();
			current.setHit();
			return false;
		}
	}

	public void enemyShowResult(String[] result) {
		if (result[1].equals("hit")) {
			enemyPanel[Integer.parseInt(result[2])][Integer.parseInt(result[3])].setColor(GameSquare.HIT);
		}
		else if (result[1].equals("miss")) {
			enemyPanel[Integer.parseInt(result[2])][Integer.parseInt(result[3])].setColor(GameSquare.MISS);
		}
	}

	public String getCurrentMode() {
		return currentMode;
	}
	public Boat getCurrentBoat() {
		return currentBoat;
	}
	public void checkForWin(Boat[] boatList, String playerName) {
		//Boat[] boatList = enemy.getBoatList();
		for (int i = 0; i < boatList.length; i++) {
			if (boatList[i].getIsSunk().equals(" ")) {
				return;
			}
		}
		if (playerName.equals("player")) {
			gameText.setText("you lose.");
		}
		else {
			gameText.setText("you win.");
		}
	}

	public void setGameText(String newText) {
		gameText.setText(newText);
	}
}