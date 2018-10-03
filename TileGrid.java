import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TileGrid extends JPanel {
	private int width, height;
	private List<Integer> remainingValues;
	private Tile[][] grid;
	private Tile missingTile;
	public TileGrid(int width, int height) {
		super(new GridLayout(width, height));
		this.width = width;
		this.height = height;
		remainingValues = new ArrayList<Integer>();
		generateValues(width, height);
		grid = new Tile[width][height];

		createGrid();
	}
	public void generateValues(int width, int height) { //assumes height == width
		for (int i = 1; i <= (width * height); i++) {
			remainingValues.add(i);
		}
		Collections.shuffle(remainingValues);
		while (!isSolvable(remainingValues, width)) {
			Collections.shuffle(remainingValues);
		}

	}
	public Boolean isSolvable(List<Integer> values, int width) {
		int inversions = 0;
		int missingTileLocation = 0;
		for (int i = 0; i < values.size(); i++) {
			int valueToCompare = values.get(i);
			if (valueToCompare == values.size()) {
				missingTileLocation = i;
			}
			for (int j = i + 1; j < values.size(); j++) {
				if (valueToCompare == values.size()) {
					missingTileLocation = i;
					break;
				}
				if (valueToCompare > values.get(j)) {
					inversions++;
				}
			}
		}
		if ((width % 2 == 1) && (inversions % 2 == 0)) {
			return true;
		}
		else if (width % 2 == 0) {
			if (((width - (missingTileLocation / width)) % 2 == 0) && (inversions % 2 == 1)) {
				return true;
			}
			else if (((width - (missingTileLocation / width)) % 2 == 1) && (inversions % 2 == 0)) {
				return true;
			}
		}
		return false;
	}
	public void createGrid() {

		for (int i = 0; i < (width * height); i++) {
			int x = (i) % width;
			int y = (i) / width;
			grid[x][y] = new Tile(x, y, remainingValues.get(0), (width * height));
			add(grid[x][y]);
			remainingValues.remove(0);
			if (grid[x][y].getValue() == (width * height)) {
				missingTile = grid[x][y];
			}
		}
	}

	public void move(String direction) {
		Tile tileToSwap = missingTile;
		Boolean valid = false;
		if (direction.equals("down")) {
			if ((missingTile.getGridY() - 1) >= 0) {
				tileToSwap = grid[missingTile.getGridX()][missingTile.getGridY() - 1];
				valid = true;
			}
		}
		else if (direction.equals("up")) {
			if ((missingTile.getGridY() + 1) < height) {
				tileToSwap = grid[missingTile.getGridX()][missingTile.getGridY() + 1];
				valid = true;
			}
		}
		else if (direction.equals("right")) {
			if ((missingTile.getGridX() - 1) >= 0) {
				tileToSwap = grid[missingTile.getGridX() - 1][missingTile.getGridY()];
				valid = true;
			}
		}
		else if (direction.equals("left")) {
			if ((missingTile.getGridX() + 1) < width) {
				tileToSwap = grid[missingTile.getGridX() + 1][missingTile.getGridY()];
				valid = true;
			}
		}
		if (valid) {
			missingTile.setValue(tileToSwap.getValue());
			Tile newTile = missingTile;
			tileToSwap.setValue(width * height);
			missingTile = tileToSwap;
			if (((newTile.getGridY() * width) + newTile.getGridX()) + 1 == newTile.getValue()) {
				checkForWin();
			}
		}
	}

	public void checkForWin() {
		Boolean gameIsWon = true;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].getValue() != ((j * width) + i + 1)) {
					gameIsWon = false;
					break;
				}
			}
			if (!gameIsWon) {
				break;
			}
		}
		if (gameIsWon) {
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					grid[i][j].changeBorderColor();
				}
			}
		}
	}
}