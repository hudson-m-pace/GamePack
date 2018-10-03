public class Boat {
	private int length;
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	private int orientation;
	private int x;
	private int y;
	private Boolean isSunk;
	private String name;

	public Boat(int length, int orientation, String name) {
		this.length = length;
		this.orientation = orientation;
		isSunk = false;
		this.name = name;
	}

	public int getLength() {
		return length;
	}
	public String getName() {
		return name;
	}
	private void switchOrientation() {
		switch(orientation) {
			case EAST:
				orientation = SOUTH;
				break;
			case SOUTH:
				orientation = WEST;
				break;
			case WEST:
				orientation = NORTH;
				break;
			case NORTH:
				orientation = EAST;
				break;
		}
	}
	public int getOrientation() {
		return orientation;
	}
	public String getIsSunk() {
		if (isSunk) {
			return "X";
		}
		return " ";
	}
	public Boolean checkForNewSink(GameSquare[][] panel) {
		if (isSunk) {
			return false;
		}
		if (orientation == EAST) {
			for (int i = 0; i < length; i++) {
				if (!panel[x + i][y].getHasBeenHit()) {
					return false;
				}
			}
		}
		else if (orientation == WEST) {
			for (int i = 0; i < length; i++) {
				if (!panel[x - i][y].getHasBeenHit()) {
					return false;
				}
			}
		}
		else if (orientation == SOUTH) {
			for (int i = 0; i < length; i++) {
				if (!panel[x][y + i].getHasBeenHit()) {
					return false;
				}
			}
		}
		else if (orientation == NORTH) {
			for (int i = 0; i < length; i++) {
				if (!panel[x][y - i].getHasBeenHit()) {
					return false;
				}
			}
		}
		sink(panel);
		return true;
	}
	public void sink(GameSquare[][] panel) {
		isSunk = true;
		if (orientation == EAST) {
			for (int i = 0; i < length; i++) {
				panel[x + i][y].setColor(GameSquare.SUNK);
			}
		}
		else if (orientation == WEST) {
			for (int i = 0; i < length; i++) {
				panel[x - i][y].setColor(GameSquare.SUNK);
			}
		}
		else if (orientation == SOUTH) {
			for (int i = 0; i < length; i++) {
				panel[x][y + i].setColor(GameSquare.SUNK);
			}
		}
		else if (orientation == NORTH) {
			for (int i = 0; i < length; i++) {
				panel[x][y - 1].setColor(GameSquare.SUNK);
			}
		}

	}

	public void rotate(GameSquare[][] playerPanel, GameSquare currentSquare) {
		deselect(playerPanel, currentSquare);
		switchOrientation();
		select(playerPanel, currentSquare);
	}

	public void select(GameSquare[][] playerPanel, GameSquare currentSquare) {
		int x = currentSquare.getSquareX();
		int y = currentSquare.getSquareY();
		if (getOrientation() == EAST) {
			for (int i = 0; i < getLength(); i++) {
				if ((x + i) < 10) {
					playerPanel[x + i][y].select();
				}
			}
		}
		else if (getOrientation() == WEST) {
			for (int i = 0; i < getLength(); i++) {
				if ((x - i) > -1) {
					playerPanel[x - i][y].select();
				}
			}
		}
		else if (getOrientation() == SOUTH) {
			for (int i = 0; i < getLength(); i++) {
				if ((y + i) < 10) {
					playerPanel[x][y + i].select();
				}
			}
		}
		else if (getOrientation() == NORTH) {
			for (int i = 0; i < getLength(); i++) {
				if ((y - i) > -1) {
					playerPanel[x][y - i].select();
				}
			}
		}
	}

	public void deselect(GameSquare[][] playerPanel, GameSquare currentSquare) {
		int x = currentSquare.getSquareX();
		int y = currentSquare.getSquareY();
		if (getOrientation() == EAST) {
			for (int i = 0; i < getLength(); i++) {
				if ((x + i) < 10) {
					playerPanel[x + i][y].deselect();
				}
			}
		}
		else if (getOrientation() == WEST) {
			for (int i = 0; i < getLength(); i++) {
				if ((x - i) > -1) {
					playerPanel[x - i][y].deselect();
				}
			}
		}
		else if (getOrientation() == SOUTH) {
			for (int i = 0; i < getLength(); i++) {
				if ((y + i) < 10) {
					playerPanel[x][y + i].deselect();
				}
			}
		}
		else if (getOrientation() == NORTH) {
			for (int i = 0; i < getLength(); i++) {
				if ((y - i) > -1) {
					playerPanel[x][y - i].deselect();
				}
			}
		}
	}

	public Boolean place(GameSquare[][] playerPanel, GameSquare currentSquare) {
		int x = currentSquare.getSquareX();
		int y = currentSquare.getSquareY();

		if (getOrientation() == EAST) {
			if (x + getLength() - 1 < 10) {
				for (int i = 0; i < getLength(); i++) {
					 if (playerPanel[x + i][y].hasBoat()) {
					 	return false;
					 }
				}
				for (int i = 0; i < getLength(); i++) {
					playerPanel[x + i][y].addBoat();
				}
			}
			else {
				return false;
			}
		}
		else if (getOrientation() == WEST) {
			if (x - getLength() + 1 > -1) {
				for (int i = 0; i < getLength(); i++) {
					 if (playerPanel[x - i][y].hasBoat()) {
					 	return false;
					 }
				}
				for (int i = 0; i < getLength(); i++) {
					playerPanel[x - i][y].addBoat();
				}
			}
			else {
				return false;
			}
		}
		else if (getOrientation() == SOUTH) {
			if (y + getLength() - 1 < 10) {

				for (int i = 0; i < getLength(); i++) {
					 if (playerPanel[x][y + i].hasBoat()) {
					 	return false;
					 }
				}
				for (int i = 0; i < getLength(); i++) {
					playerPanel[x][y + i].addBoat();
				}
			}
			else {
				return false;
			}
		}
		else if (getOrientation() == NORTH) {
			if (y - getLength() + 1 > -1) {

				for (int i = 0; i < getLength(); i++) {
					 if (playerPanel[x][y - i].hasBoat()) {
					 	return false;
					 }
				}
				for (int i = 0; i < getLength(); i++) {
					playerPanel[x][y - i].addBoat();
				}
			}
			else {
				return false;
			}
		}
		this.x = x;
		this.y = y;
		return true;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
}