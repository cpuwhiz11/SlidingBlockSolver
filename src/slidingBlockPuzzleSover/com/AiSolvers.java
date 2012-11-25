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
		
		// Create a queue to hold all the gameboards
		AbstractQueue<GameBoard> gameQueue = new PriorityQueue<GameBoard>(); 
		
		// Add initial gameboard
		gameQueue.add(gameBoard); 
		
		while (!gameQueue.isEmpty()){
			// Get first gameboard
			GameBoard inspectBoard = gameQueue.poll();
			
			if (inspectBoard.checkForWin()){
				break;
			}
			
			// Get all the possible moves 
			 List<Integer> posMoveList = new ArrayList<Integer>(); 
			 posMoveList = AiUtils.getAllPossibleMoves(inspectBoard);
			 
			 // Make each move and add unique gameboard to queue
			 for(int move : posMoveList){
				 AiUtils.makeMove(move, inspectBoard);
				 
				 // FIXME this is not quite right yet 
				 gameQueue.add(inspectBoard);
			 }
			 
		}
		
		
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
