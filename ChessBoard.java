import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class ChessBoard extends GameBoard {
	private JPanel board;
	private ChessSquare[][] squares;

	public ChessBoard(GamePackFrame gamePackFrame) {
		super(gamePackFrame);

		createBoard();
	}

	public JMenu createMenu() {
		return new JMenu();
	}

	public void createBoard() {
		setLayout(new GridLayout(8, 8));
		Boolean whiteSquare = true;
		squares = new ChessSquare[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				squares[i][j] = new ChessSquare(whiteSquare ? "white" : "black");
				add(squares[i][j]);
				whiteSquare = !whiteSquare;
			}
			whiteSquare = !whiteSquare;
		}

		for (int i = 0; i < 8; i++) {
			//squares[1][i].addGamePiece(new Pawn());
			//squares[6][i].addGamePiece(new Pawn());
		}
	}
}