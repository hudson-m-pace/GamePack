import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class EnemyGameSquare extends GameSquare {
	private GameSquare[][] playerPanel;
	private Enemy enemy;
	private String currentMode;
	private BattleshipBoard battleshipBoard;
	private Player player;

	public EnemyGameSquare(int squareX, int squareY, String role, GameSquare[][] playerPanel, Enemy enemy, BattleshipBoard battleshipBoard, Player player) {
		super(squareX, squareY, role);
		addMouseListener(new EnemyGameSquareListener());
		this.playerPanel = playerPanel;
		this.enemy = enemy;
		this.battleshipBoard = battleshipBoard;
		this.player = player;
	}

	public void addBoat() {
		hasBoat = true;
		//setColor(HAS_BOAT);
	}

	class EnemyGameSquareListener implements MouseListener {

		public void mouseEntered(MouseEvent e) {
			select();
		}
		public void mouseExited(MouseEvent e) {
			deselect();
		}
		public void mousePressed(MouseEvent e) {
			if (battleshipBoard.getCurrentMode().equals(BattleshipBoard.MAIN_GAME)) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (!getHasBeenHit()) {
						setHit();
						enemy.checkForNewSinks();
						int[] target = enemy.takeTurn();
						playerPanel[target[0]][target[1]].setHit();
						player.checkForNewSinks();
						select();
					}
				}
			}
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseClicked(MouseEvent e) {
		}
	}
}