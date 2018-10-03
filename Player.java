public class Player {
	private Boat[] boatList;
	private int[] boatSizes;
	private String[] boatNames;
	private int boatCounter;
	private GameSquare[][] panel;
	private BattleshipBoard battleshipBoard;
	private Enemy enemy;

	public Player(GameSquare[][] panel, BattleshipBoard battleshipBoard) {
		boatSizes = new int[] {5, 4, 3, 3, 2};
		boatNames = new String[] {"aircraft carrier", "battleship", "submarine", "cruiser", "destroyer"};
		boatList = new Boat[boatSizes.length];
		boatCounter = 0;
		this.panel = panel;
		this.battleshipBoard = battleshipBoard;
		this.enemy = enemy;

		makeBoats();
		battleshipBoard.setGameText("Place your " + boatList[boatCounter].getName() + ".");
	}

	private void makeBoats() {
		for (int i = 0; i < boatSizes.length; i++) {
			boatList[i] = new Boat(boatSizes[i], Boat.EAST, boatNames[i]);
		}
	}
	public Boolean placeBoat(GameSquare currentSquare) {
		if (boatList[boatCounter].place(panel, currentSquare)) {
			boatList[boatCounter].deselect(panel, currentSquare);
			boatCounter++;
			if (boatCounter == boatList.length) {
				return true;
			}
			boatList[boatCounter].setOrientation(boatList[boatCounter - 1].getOrientation());
			boatList[boatCounter].select(panel, currentSquare);
			battleshipBoard.setGameText("Place your " + boatList[boatCounter].getName() + ".");
		}
		return false;
	}
	public void rotateBoat(GameSquare currentSquare) {
		boatList[boatCounter].rotate(panel, currentSquare);
	}
	public void selectBoat(GameSquare currentSquare) {
		boatList[boatCounter].select(panel, currentSquare);
	}
	public void deselectBoat(GameSquare currentSquare) {
		boatList[boatCounter].deselect(panel, currentSquare);
	}

	public void checkForNewSinks() {
		for (int i = 0; i < boatList.length; i++) {
			if (boatList[i].checkForNewSink(panel)) {
				battleshipBoard.checkForWin(boatList, "player");
			}
		}
	}
}