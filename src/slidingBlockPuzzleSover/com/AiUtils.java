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
		int sizeR = gameBoard.getGameList().size();
		
		// All the columns will be the same size
		int sizeC = gameBoard.getGameList().get(0).size();
		
		// Temp number holding
		int num = 0;
		
		// The number we last moved.
		// Ignore moves that move the number we just moved
		int moveListSize = gameBoard.getMoveList().size();
		
		int lastMove = 0;
		if (moveListSize > 0) {
			// The last move is the most recent move added to the list
			lastMove = gameBoard.getMoveList().get(moveListSize - 1); 
		}				
		
		// Only get valid moves
		// more efficient than ignoring exceptions
		if (emptyC + 1 < sizeC){
			num = gameBoard.getGameList().get(emptyR).get(emptyC + 1);
			
			if (lastMove != num){
				posMoves.add(num);
			}			
		}
		
		if (emptyC - 1 >= 0){
			
			num = gameBoard.getGameList().get(emptyR).get(emptyC - 1);
			if (lastMove != num){
				posMoves.add(num);
			}	
		}
		
		if (emptyR + 1 < sizeR){
			num = gameBoard.getGameList().get(emptyR + 1).get(emptyC);
			
			if (lastMove != num){
				posMoves.add(num);
			}	
		}
		
		if (emptyR - 1 >= 0){
			num = gameBoard.getGameList().get(emptyR - 1).get(emptyC);
			
			if (lastMove != num){
				posMoves.add(num);
			}	
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

	/**
	 * Using a heuristic calculate a score for this board.
	 * The lower the score the better.
	 * @param newBoard
	 */
	public static void getBoardScore(GameBoard newBoard) {
		//newBoard.setHeuristicScore(0);

		// Calculate for each piece the total number of spaces it is out of place
		// flatten the board
		int[] flatBoard = new int[newBoard.getGameList().size() * newBoard.getGameList().size()]; 
		int listIter = 0;
		for(ArrayList<Integer> list : newBoard.getGameList()){
			for(int i = 0; i < list.size(); i++){
				flatBoard[i + list.size() * listIter] = list.get(i);
			}
			listIter++;
		}
		
		int count = 0;
		for(int i = 0; i < flatBoard.length; i++){
			int num = flatBoard[i];
			if(num != -1 && num != i + 1){
				count = count + Math.abs(i - num);
			}
			
		}
		
		newBoard.setHeuristicScore(count); 
	}

}
