import java.awt.*;
import javax.swing.*;

public class GameSquare extends Box {
	public static final Color DEFAULT = Color.LIGHT_GRAY;
	public static final Color HIT = Color.RED;
	public static final Color MISS = Color.BLUE;
	public static final Color HAS_BOAT = Color.GREEN;
	public static final Color SUNK = Color.ORANGE;
	private Color currentColor;
	protected Boolean hasBoat;
	private int squareX, squareY;
	private Boolean hasBeenHit;
	private String role;

	public GameSquare(int squareX, int squareY, String role) {

		super(BoxLayout.X_AXIS);

		currentColor = DEFAULT;
		hasBoat = false;
		this.squareX = squareX;
		this.squareY = squareY;
		hasBeenHit = false;
		this.role = role;

		setBorder(BorderFactory.createRaisedBevelBorder());
		setOpaque(true);
		setBackground(currentColor);

	}

	public int getSquareX() {
		return squareX;
	}
	public int getSquareY() {
		return squareY;
	}
	public Boolean getHasBeenHit() {
		return hasBeenHit;
	}
	public Boolean setHit() {
		hasBeenHit = true;
		if (this.hasBoat) {
			this.setColor(HIT);
			return true;
		}
		else {
			this.setColor(MISS);
			return false;
		}
	}
	public String getRole() {
		return role;
	}

	public void setColor(Color currentColor) {
		this.currentColor = currentColor;
		this.setBackground(currentColor);
	}

	public void select() {
		this.setBackground(currentColor.darker());
	}
	public void deselect() {
		this.setBackground(currentColor);
	}

	public void addBoat() {
		hasBoat = true;
		setColor(HAS_BOAT);
	}

	public boolean hasBoat() {
		if (hasBoat) {
			return true;
		}
		return false;
	}
}