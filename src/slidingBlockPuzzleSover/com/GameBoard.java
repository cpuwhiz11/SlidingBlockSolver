
package slidingBlockPuzzleSover.com;

import java.util.ArrayList;


/**
 * Class to hold a gameBoard objects. 
 * Tracks the empty space, the location of numbers, and the number
 * of swaps. 
 * @author paul
 *
 */
public class GameBoard {
	/** Holds the game board's distribution of numbers */
	private ArrayList<ArrayList<Integer>> gameList;
	
	/** Tracks the position of the empty space
	 * Always starts at 0,0 */	
	private int emptyRow = 0;  
	private int emptyCol = 0;
	
	/** Track the number of swaps a player makes */
	private int swaps = 0;
	
	/**
	 * Reset game board values to default
	 */
	public void resetGameBoard() {
		// Blank space
		this.setEmptyCol(0);
		this.setEmptyRow(0);
		
		// Number of swaps
		this.setSwaps(0);
		
		// Original game board
		this.getGameList().clear(); 
	}
	
	/**
	 * Check the existing gameList array to see if the numbers 
	 * are arranged to produce a win
	 */
	public boolean checkForWin() {
		
		// Loop through making sure each number is only 1 more than the previous
		// skip the empty space.
        int count = 0;
		for(int i = 0; i < gameList.size(); i++) {			
			for(int j = 0; j < gameList.get(i).size(); j++){				
				int num = gameList.get(i).get(j);
				// When the gamelist's num is -1 that is the blank space
				if(num == -1){
					continue; 
				}
				
				// If count is 0 first iteration just plug in first number
				if(count == 0){
					count = num;
				} else {
					// The next number should be larger than count by 1.
					// If not the game is not won.
					if(count + 1 != num) {
						return false; 
					} else {
						// The last checked number becomes the new count
						count = num; 
					}
				}
					
			}
		}
		
		// Check completed successfully
		return true;
	}
	
	public int getEmptyRow() {
		return emptyRow;
	}

	public void setEmptyRow(int emptyRow) {
		this.emptyRow = emptyRow;
		
		// Every time this number changes a swap occurs
		// update the swap value.
		this.swaps++; 
	}

	public int getEmptyCol() {
		return emptyCol;
	}

	public void setEmptyCol(int emptyCol) {
		this.emptyCol = emptyCol;
	}

	public GameBoard(ArrayList<ArrayList<Integer>> gameList) {
		this.gameList = gameList;
	}

	public ArrayList<ArrayList<Integer>> getGameList() {
		return gameList;
	}

	public void setGameList(ArrayList<ArrayList<Integer>> gameList) {
		this.gameList = gameList;
	}

	public int getSwaps() {
		return swaps;
	}

	public void setSwaps(int swaps) {
		this.swaps = swaps;
	}

}