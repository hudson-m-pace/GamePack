import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

public class ChessSquare extends JPanel {
	GamePiece currentGamePiece = null;

	public ChessSquare(String color) {
		if (color == "black") {
			setBackground(Color.GRAY);
		}
		else if (color == "white") {
			setBackground(Color.WHITE);
		}

		addMouseListener(new ChessSquareListener());
	}
	public Boolean addGamePiece(GamePiece piece) {
		if (currentGamePiece == null) {
			currentGamePiece = piece;
			return true;
		}
		return false;
	}
	public void interact() {
		if (currentGamePiece == null) {
			System.out.println("no piece");
		}
		else {
			currentGamePiece.logName();
		}
	}

	class ChessSquareListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			interact();
		}
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (currentGamePiece != null) {
			g.drawImage(currentGamePiece.getImage(), 0, 0, this);
		}
	}
}