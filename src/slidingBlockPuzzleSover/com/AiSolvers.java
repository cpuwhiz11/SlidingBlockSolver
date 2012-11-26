package slidingBlockPuzzleSover.com;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Contains all the implementations of the various ai solvers
 * @author paul
 *
 */
public class AiSolvers {

	/**
	 * Start the ai indicated by the enum
	 */
	public static ArrayList<Integer> runAiDispath(AiType type, GameBoard gameBoard){
		
		switch (type) {
		case DFS:
			return runDfs(gameBoard);
		case BFS:
			return runBfs(gameBoard);
			
		case ASTAR:
			return runAStar(gameBoard);

		default:
			throw new Error("Unknown AI type selected!"); 
		}
		
	}
	
	/**
	 * Astar solving algorithm
	 * @param gameBoard
	 */
	public static ArrayList<Integer> runAStar(GameBoard gameBoard) {
		// List of winning moves
		ArrayList<Integer> winMoves = new ArrayList<Integer>(); 
		
		return winMoves; 
		
	}

	/**
	 * Breadth first search algorithm 
	 * @param gameBoard
	 */
	public static ArrayList<Integer> runBfs(GameBoard gameBoard) {
		// List of winning moves
		ArrayList<Integer> winMoves = new ArrayList<Integer>(); 
		
		// Counter of how many boards inspected
		int numInspected = 0;
		
		// Create a queue to hold all the gameboards
		AbstractQueue<GameBoard> gameQueue = new PriorityQueue<GameBoard>(); 
		
		// Add initial gameboard
		gameQueue.add(gameBoard); 
		
		while (!gameQueue.isEmpty()){
			// Get first gameboard
//			GameBoard inspectBoard = null;
//			try {
//				inspectBoard = (GameBoard) gameQueue.poll().clone();
//			} catch (CloneNotSupportedException e) {
//				e.printStackTrace();
//				throw new Error("Did not clone board correctly on queue pop"); 
//			}
			
			GameBoard inspectBoard = gameQueue.poll();
			if (inspectBoard.checkForWin()){
				// Set winMoves to be the address of the movelist
				// that contains the winning moves from the gameboard
				winMoves = inspectBoard.getMoveList(); 
				break;
			}
			
			 // Get all the possible moves 
			 List<Integer> posMoveList = new ArrayList<Integer>(); 
			 posMoveList = AiUtils.getAllPossibleMoves(inspectBoard);
			 
			 // Make each move and add unique gameboard to queue
			 for(int move : posMoveList){
				 // Each new move creates a new game board which is added to the queue
//				 GameBoard newBoard = null;
//				 try {
//					newBoard = inspectBoard.clone();
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//					throw new Error("Did not clone board correctly on move loop"); 
//				}
				 
				 // Create a new gameBoard from the insepectBoard obj
				 // pedantically copy the objects since java IS PASS BY REFERENCE
				 GameBoard newBoard = new GameBoard(new ArrayList<ArrayList<Integer>>());				 			 
				 ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
				 // Copy numbers overs to newBoard
				 for(int i = 0; i < inspectBoard.getGameList().size(); i++){

					 // For each row add the column of numbers to the list
					 ArrayList<Integer> copyRow = new ArrayList<Integer>(); 
					 for(int item : inspectBoard.getGameList().get(i)){			
						 copyRow.add(item);				
					 }

					 newList.add(copyRow); 
				 }
				 
				 // Copy over past moves				 
				 newBoard.setMoveList(new ArrayList<Integer>(inspectBoard.getMoveList()));
				 
				 newBoard.setGameList(newList); 
				 newBoard.setEmptyCol(inspectBoard.getEmptyCol());
				 newBoard.setEmptyRow(inspectBoard.getEmptyRow());
				 newBoard.setSwaps(inspectBoard.getSwaps()); 
				 
				 AiUtils.makeMove(move, newBoard);
				 
				 // Each new gameboard gets the move added to the solution array
				 newBoard.addToMoveList(move); 
				 
				 // Add new board to queue 
				 gameQueue.add(newBoard);
				 
				 numInspected++;
			 }
			 
		}
		
		// Add the number of inspected game boards to the end of the list
		winMoves.add(numInspected);
		return winMoves; 
		
	}
	
	/**
	 * Depth first search algorithm
	 * @param gameBoard
	 */
	public static ArrayList<Integer> runDfs(GameBoard gameBoard) {
		// List of winning moves
		ArrayList<Integer> winMoves = new ArrayList<Integer>(); 
		
		return winMoves; 
		
	}
	
	
	
}
