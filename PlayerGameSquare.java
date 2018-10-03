import javax.swing.SwingUtilities;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class PlayerGameSquare extends GameSquare {
	private GameSquare[][] playerPanel;
	private BattleshipBoard battleshipBoard;
	private Player player;
	private Enemy enemy;

	public PlayerGameSquare(int squareX, int squareY, String role, GameSquare[][] playerPanel, BattleshipBoard battleshipBoard, Player player, Enemy enemy) {
		super(squareX, squareY, role);
		addMouseListener(new PlayerGameSquareListener(this));
		this.playerPanel = playerPanel;
		this.player = player;
		this.battleshipBoard = battleshipBoard;
		this.enemy = enemy;
	}

	class PlayerGameSquareListener implements MouseListener {
		private GameSquare currentSquare;

		public PlayerGameSquareListener(GameSquare currentSquare) {
			this.currentSquare = currentSquare;
		}

		public void mouseEntered(MouseEvent e) {
			if (battleshipBoard.getCurrentMode().equals(BattleshipBoard.PLACE_SHIPS)) {
				player.selectBoat(currentSquare);
			}
			else {
				select();
			}
		}

		public void mouseExited(MouseEvent e) {
			if (battleshipBoard.getCurrentMode().equals(BattleshipBoard.PLACE_SHIPS)) {
				player.deselectBoat(currentSquare);
			}
			else {
				deselect();
			}
		}

		public void mousePressed(MouseEvent e) {

			//Boat currentBoat = gameBoard.getCurrentBoat();
			if (battleshipBoard.getCurrentMode().equals(BattleshipBoard.PLACE_SHIPS)) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					/*if (currentBoat.place(playerPanel, currentSquare)) {
						gameBoard.getNextBoat();
					}*/
					if (player.placeBoat(currentSquare)) {
						enemy.placeBoats();
						battleshipBoard.changeGameMode();
						battleshipBoard.updateTargetList();
					}
				}
				else if (SwingUtilities.isRightMouseButton(e)) {
					//currentBoat.rotate(playerPanel, currentSquare);
					player.rotateBoat(currentSquare);
				}
			}
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseClicked(MouseEvent e) {
		}
	}
}