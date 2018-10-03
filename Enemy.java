import java.util.Random;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class Enemy {
	private ArrayList<int[]> remainingGrid;
	private Random random;
	private Boat[] boatList;
	private int[] boatSizes = {5, 4, 3, 3, 2};
	private String[] boatNames = {"aircraft carrier(5)", "battleship(4)", "submarine(3)", "cruiser(3)", "destroyer(2)"};
	private GameSquare[][] panel;
	private BattleshipBoard battleshipBoard;

	public Enemy(GameSquare[][] panel, BattleshipBoard battleshipBoard) {
		random = new Random();
		this.battleshipBoard = battleshipBoard;
		remainingGrid = new ArrayList<int[]>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				remainingGrid.add(new int[]{i, j});
			}
		}
		this.panel = panel;

		boatList = new Boat[5];
		for (int i = 0; i < boatList.length; i++) {
			if (random.nextInt(2) == 0) {
				boatList[i] = new Boat(boatSizes[i], Boat.EAST, boatNames[i]);
			}
			else {
				boatList[i] = new Boat(boatSizes[i], Boat.SOUTH, boatNames[i]);
			}
		}
	}
	public int[] takeTurn() {
		int index = random.nextInt(remainingGrid.size());
		int[] arr = remainingGrid.get(index);
		remainingGrid.remove(index);
		return arr;
	}

	public void placeBoats() {//GameSquare[][] enemyPanel) {
		
		for (int i = 0; i < boatSizes.length; i++) {
			while (!boatList[i].place(panel, panel[random.nextInt(10)][random.nextInt(10)])) {
			}
		}
	}

	//TEMP TEMP TEMP
	public void checkForNewSinks() {
		for (int i = 0; i < boatList.length; i++) {
			if (boatList[i].checkForNewSink(panel)) {
				battleshipBoard.updateTargetList();
				battleshipBoard.checkForWin(boatList, "enemy");
			}
		}
	}

	public Boat[] getBoatList() {
		return boatList;
	}
}