import java.util.ArrayList;
import java.util.List;

package slidingBlockPuzzleSover;
/**
 * Contains utilities to assist the solving ai
 * @author paul
 *
 */
public class AiUtils {
	
	/**
	 * Get a list on numbers that indicate possible next moves
	 * @param gameList, the game board
	 */
	public static List<Integer> getNextMove(ArrayList<ArrayList<Integer>> gameList){
		// Contains all the possible moves
		List<Integer> posMoves = new ArrayList<Integer>();
		
		// Find the blank space (-1 on the list)
		int emptyR = -1, emptyC = -1;
		for(int i = 0; i < gameList.size(); i++){
			for(int j = 0; j < gameList.get(i).size(); j++){
				if(gameList.get(i).get(j) == -1){
					emptyR = i;
					emptyC = j; 		
				}
			}
		}
		
		// Sanity check, should have found empty space
		if(emptyR == -1 || emptyC == -1){
			throw new Error("Did not find the empty space!");
		}
		
		// Try to get numbers +1 in any direction from empty space
		// If we get an exception, outside array, ignore. 
		try {
			posMoves.add(gameList.get(emptyR).get(emptyC + 1));
		} catch(Exception e){
		}
		try {
			posMoves.add(gameList.get(emptyR).get(emptyC - 1) );

		} catch(Exception e){
		}
		try {
			posMoves.add(gameList.get(emptyR + 1).get(emptyC));
		} catch(Exception e){
		}
		try {
			posMoves.add(gameList.get(emptyR - 1).get(emptyC));
		} catch(Exception e){
		}

		return posMoves; 
	}
	
	/**
	 * Swap the number specified with the empty space
	 * @param numToMove, the number to move to the empty space
	 * @param gameList, the playing board
	 */
	public static void makeMove(int numToMove, ArrayList<ArrayList<Integer>> gameList){
		// Find -1 which indicates the empty space 
		int emptyR = -1, emptyC = -1;
		for(int i = 0; i < gameList.size(); i++){
			for(int j = 0; j < gameList.get(i).size(); j++){
				if(gameList.get(i).get(j) == -1){
					emptyR = i;
					emptyC = j; 		
				}
			}
		}
		
		// Sanity check, should have found empty space
		if(emptyR == -1 || emptyC == -1){
			throw new Error("Did not find the empty space!");
		}
		
		// Find number to swap 
		int swapR = -1, swapC = -1;
		for(int i = 0; i < gameList.size(); i++){
			for(int j = 0; j < gameList.get(i).size(); j++){
				if(gameList.get(i).get(j) == -1){
					swapR = i;
					swapC = j; 		
				}
			}
		}
		
		// Sanity check, should have found empty space
		if(swapR == -1 || swapC == -1){
			throw new Error("Did not find the empty space!");
		}
		
		// Swap empty space with new number 
		gameList.get(emptyR).set(emptyC, gameList.get(swapR).get(swapC)); 
		
		// Swap new number with empty space 
		gameList.get(swapR).set(swapC, -1); 
	}

}
