package slidingBlockPuzzleSover.com;


import java.util.ArrayList;
import java.util.List;

/**
 * Contains utilities to assist the solving ai
 * @author paul
 *
 */
public class AiUtils {
	
	/**
	 * Get a list on numbers that indicate possible next moves
	 * @param gameBoard, the game board
	 */
	public static List<Integer> getAllPossibleMoves(GameBoard gameBoard){
		// Contains all the possible moves
		List<Integer> posMoves = new ArrayList<Integer>();
		
		// Try to get numbers +1 in any direction from empty space
		// If we get an exception, outside array, ignore. 
		int emptyR = gameBoard.getEmptyRow();
		int emptyC = gameBoard.getEmptyCol();
		
		try {
			posMoves.add(gameBoard.getGameList().get(emptyR).get(emptyC + 1));
		} catch(Exception e){
		}
		try {
			posMoves.add(gameBoard.getGameList().get(emptyR).get(emptyC - 1) );

		} catch(Exception e){
		}
		try {
			posMoves.add(gameBoard.getGameList().get(emptyR + 1).get(emptyC));
		} catch(Exception e){
		}
		try {
			posMoves.add(gameBoard.getGameList().get(emptyR - 1).get(emptyC));
		} catch(Exception e){
		}

		return posMoves; 
	}
	
	/**
	 * Swap the number specified with the empty space
	 * @param numToMove, the number to move to the empty space
	 * @param gameList, the playing board
	 */
	public static void makeMove(int numToMove, GameBoard gameBoard){
		int emptyR = gameBoard.getEmptyRow();
		int emptyC = gameBoard.getEmptyCol();
		
		// Find number to swap 
		int swapR = -1, swapC = -1;
		for(int i = 0; i < gameBoard.getGameList().size(); i++){
			for(int j = 0; j < gameBoard.getGameList().get(i).size(); j++){
				if(gameBoard.getGameList().get(i).get(j) == numToMove){
					swapR = i;
					swapC = j; 		
				}
			}
		}
		
		// Sanity check, should have found empty space
		if(swapR == -1 || swapC == -1){
			throw new Error("Did not find the number: " + String.valueOf(numToMove) + " to swap on the list:" + gameBoard.getGameList().toString());
		}
		
		// Swap empty space with new number 
		gameBoard.getGameList().get(emptyR).set(emptyC, gameBoard.getGameList().get(swapR).get(swapC)); 
		
		// Swap new number with empty space 
		gameBoard.getGameList().get(swapR).set(swapC, -1); 
		
		// Increment swap count
		gameBoard.incrementSwaps(); 
		
		// Set location of new empty space
		// this was the location of the number we swapped
		gameBoard.setEmptyCol(swapC);
		gameBoard.setEmptyRow(swapR); 
	}

}
