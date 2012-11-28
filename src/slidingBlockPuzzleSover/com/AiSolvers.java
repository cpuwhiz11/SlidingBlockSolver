package slidingBlockPuzzleSover.com;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Contains all the implementations of the various ai solvers
 * @author paul
 *
 */
public class AiSolvers {

	/**
	 * Start the ai indicated by the enum
	 * @param type, the type of AI to use as specified by the enum
	 * @param gameBoard, the starting gameboard
	 * @param bOption, enable the option or not
	 */
	public static ArrayList<Integer> runAiDispath(AiType type, GameBoard gameBoard, boolean bOption){
		
		switch (type) {
		case DFS:
			return runDfs(gameBoard);
		case BFS:
			return runBfs(gameBoard, bOption);
			
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
		
		// Possible heuristics
		// 1. Number of pieces in wrong position
		// 2. Sum of the distance all the pieces would have to move to be in the right place 
		// 3. Manhattan distance of some pieces. 
		
		// List of winning moves
		ArrayList<Integer> winMoves = null; 
		
		// Counter of how many boards inspected
		int numInspected = 0;
		
		ArrayList<GameBoard> gameBoardList = new ArrayList<GameBoard>(); 
		
		// Create a list to hold all the boards we have ever looked at.
		// This is needed to prevent the same board structure being looked at 
		// more than once. Will only be used if the spaceTradeoff == True
		AbstractQueue<ArrayList<ArrayList<Integer>>> pastGameListQueue 
			= new ConcurrentLinkedQueue<ArrayList<ArrayList<Integer>>>(); 
		
		// Add initial gameboard
		// Set really high score
		gameBoard.setHeuristicScore(1000);
		gameBoardList.add(gameBoard); 
		
		while (!gameBoardList.isEmpty()){
			// Get first gameboard		
			
			// Flip through and get the gameboard with the lowest score
			int min = 1000;
			int pos = 0;
			for(int i = 0; i < gameBoardList.size(); i++){
				GameBoard board = gameBoardList.get(i); 
				
				// Only get boards we have not looked at
				if(!board.isLooked() && board.getHeuristicScore() < min){
					min = board.getHeuristicScore();
					pos = i; 
				}
			}
			//System.out.print("\nScore is:" + String.valueOf(min));
			
			// Look at the lowest score one first
			GameBoard inspectBoard = gameBoardList.get(pos);
			
			// Looked at this board
			inspectBoard.setLooked(true);
			
			//System.out.print(inspectBoard.getGameList().toString()); 
			
			// Increment number of inspected boards
			numInspected++;
			
			// Check for win
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
				 
				 // Check to see if the board that move created has already been looked at
				 // this is possible for large boards when the numbers start circling. E.g. 3 2 1, 3 2 1, etc			 
				 if(isOriginalBoard(newBoard.getGameList(), pastGameListQueue)){					 
					 // Each new gameboard gets the move added to the solution array
					 newBoard.addToMoveList(move); 

					 pastGameListQueue.add(newBoard.getGameList());
				 } else {
					 // Already looked at board before, ignore it
					 continue;
				 }
				 
				 //newBoard.addToMoveList(move); 
				 // Calculate this boards heuristic score
				 AiUtils.getBoardScore(newBoard); 

				 // Add new board to queue 
				 gameBoardList.add(newBoard);
			 }
			 
		}
		
		// Add the number of inspected game boards to the end of the list
		// Only do so if we found a solution
		if (winMoves != null){
			winMoves.add(numInspected);
		}
	
		return winMoves; 
		
	}

	/**
	 * Breadth first search algorithm 
	 * @param gameBoard
	 * @param spaceTradeoff, true to use space saving measures (at the cost of time)
	 */
	public static ArrayList<Integer> runBfs(GameBoard gameBoard, boolean spaceTradeoff) {
		// List of winning moves
		ArrayList<Integer> winMoves = null; 
		
		// Counter of how many boards inspected
		int numInspected = 0;
		
		// Create a queue to hold all the gameboards we are inspecting
		AbstractQueue<GameBoard> gameQueue = new PriorityQueue<GameBoard>(); 
		
		// Create a list to hold all the boards we have ever looked at.
		// This is needed to prevent the same board structure being looked at 
		// more than once. Will only be used if the spaceTradeoff == True
		AbstractQueue<ArrayList<ArrayList<Integer>>> pastGameListQueue 
			= new ConcurrentLinkedQueue<ArrayList<ArrayList<Integer>>>(); 
		
		// Add initial gameboard
		gameQueue.add(gameBoard); 
		
		while (!gameQueue.isEmpty()){
			// Get first gameboard				
			GameBoard inspectBoard = gameQueue.poll();
			
			// Increment number of inspected boards
			numInspected++;
			
			// Check for win
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
				 
				 // Check to see if the board that move created has already been looked at
				 // this is possible for large boards when the numbers start circling. E.g. 3 2 1, 3 2 1, etc
//				 // First flatten the array
//				 int[] flatBoard = new int[newBoard.getGameList().size() * newBoard.getGameList().size()]; 
//				 int listIter = 0;
//				 for(ArrayList<Integer> list : newBoard.getGameList()){
//					 for(int i = 0; i < list.size(); i++){
//						 flatBoard[i + list.size() * listIter] = list.get(i);
//					 }
//					 listIter++;
//				 }
				 
				 // Are we using the spaceTradeoff
				 if(spaceTradeoff){
					 if(isOriginalBoard(newBoard.getGameList(), pastGameListQueue)){					 
						 // Each new gameboard gets the move added to the solution array
						 newBoard.addToMoveList(move); 

						 pastGameListQueue.add(newBoard.getGameList());
					 } else {
						 // Already looked at board before, ignore it
						 continue;
					 }
				 } else {
					 // Take less time at the cost of space
					 // Doing so could overflow the stack for large boards
					 newBoard.addToMoveList(move); 
				 }
				 
				 // Add new board to queue 
				 gameQueue.add(newBoard);
			 }
			 
		}
		
		// Add the number of inspected game boards to the end of the list
		// Only do so if we found a solution
		if (winMoves != null){
			winMoves.add(numInspected);
		}
	
		return winMoves; 
		
	}
	
	/**
	 * Look at the gameList part of a gameboard and determine if it has
	 * been looked at once before or not
	 * @param gameList in question
	 * @param the queue holding all the past boards
	 * @return true if original gameboard, false otherwise
	 */
	private static boolean isOriginalBoard(ArrayList<ArrayList<Integer>> gameList,
			AbstractQueue<ArrayList<ArrayList<Integer>>> pastBoards) {
		
		return !(pastBoards.contains(gameList));		
	}

	/**
	 * Depth first search algorithm
	 * @param gameBoard
	 */
	public static ArrayList<Integer> runDfs(GameBoard gameBoard) {
		// List of winning moves
		ArrayList<Integer> winMoves = null; 
		
		// Counter of how many boards inspected
		int numInspected = 0;
		
		// Create a queue to hold all the gameboards
		Stack<GameBoard> gameStack = new Stack<GameBoard>(); 
		
		// Add initial gameboard
		gameStack.add(gameBoard); 
		
		while (!gameStack.isEmpty()){
			// Get first gameboard				
			GameBoard inspectBoard = gameStack.pop();
			
			// Increment number of inspected boards
			numInspected++;
			
			// Check for win
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
				 gameStack.add(newBoard);
			 }
			 
		}
		
		// Add the number of inspected game boards to the end of the list
		// Only do so if we found a solution
		if (winMoves != null){
			winMoves.add(numInspected);
		}
	
		return winMoves; 
		
	}	
	
}
