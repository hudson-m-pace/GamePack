import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.CardLayout;

public class GamePackFrame extends JFrame {

	private JPanel cards;
	private JPanel gameCard;
	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem returnMenuItem, newGameMenuItem;
	private GameSocketController gameSocketController;

	public GamePackFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gameSocketController = new GameSocketController(this);
		createMenu();
		createComponents();
		setSize(1000, 1000);
		setVisible(true);
		setTitle("Game Pack");
	}

	private void createMenu() {
		menuBar = new JMenuBar();

		JMenu gameMenu = new JMenu("game");
		menuBar.add(gameMenu);

		JMenuItem exitMenuItem = new JMenuItem("exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		gameMenu.add(exitMenuItem);

		returnMenuItem = new JMenuItem("return to menu");
		returnMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameCard != null) {
					closeGame();
				}
			}
		});
		gameMenu.add(returnMenuItem);

		
		JMenu connectionMenu = gameSocketController.createMenu();
		menuBar.add(connectionMenu);

		setJMenuBar(menuBar);
	}

	private void createComponents() {
		JPanel pane = new JPanel();
		String[][] tileNames = {{"battleship", "fifteen", "wander", "chess"},
							{"", "", "", ""},
							{"", "", "", ""},
							{"", "", "", ""}};

		JPanel menuCard = new JPanel(new GridLayout(tileNames.length, tileNames[0].length));
		menuCard.setOpaque(true);

		for (int i = 0; i < tileNames.length; i++) {
			for (int j = 0; j < tileNames[i].length; j++) {
				menuCard.add(new GamePackMenuChoice(this, tileNames[j][i]));
			}
		}


		cards = new JPanel(new CardLayout());

		cards.add(menuCard, "menuCard");

		add(cards);

	}

	public void startGame(String gameName) {
		Boolean validGame = true;
		if (gameCard != null) {
			cards.remove(gameCard);
		}
		switch(gameName) {
			case "battleship":
				gameCard = new BattleshipBoard(this, gameSocketController);
				break;
			case "fifteen":
				gameCard = new FifteenBoard(this);
				break;
			case "wander":
				gameCard = new WanderBoard(this);
				break;
			case "chess":
				gameCard = new ChessBoard(this);
				break;
			default:
				validGame = false;
				break;
		}
		if (validGame) {
			gameMenu = ((GameBoard)gameCard).createMenu();
			menuBar.add(gameMenu);
			cards.add(gameCard, "gameCard");
			CardLayout cl = (CardLayout)(cards.getLayout());
	    	cl.show(cards, "gameCard");
	    }
	}
	public void closeGame() {
		createMenu();
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.show(cards, "menuCard");
		cards.remove(gameCard);
		gameCard = null;
	}
}
