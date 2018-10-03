import java.awt.image.BufferedImage;

public abstract class GamePiece {
	protected String name;
	protected BufferedImage image;
	GamePiece(String name) {
		this.name = name;
	}
	public void logName() {
		System.out.println(name);
	}
	public BufferedImage getImage() {
		return image;
	}
	abstract int[][] showAvailableSpots();
}