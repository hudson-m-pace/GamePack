import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class Pawn extends GamePiece {
	private Boolean inStartingPosition = true;
	private int xPosition, yPosition;

	public Pawn(int x, int y) {
		super("pawn");

		this.xPosition = x;
		this.yPosition = y;

		try {
			image = ImageIO.read(new File("./pawn.png"));
		} catch(IOException e) {
			System.out.println("couldn't read image...");
		}
	}

	public int[][] showAvailableSpots() {
		return null;
	}
}