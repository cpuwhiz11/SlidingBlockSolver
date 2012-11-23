package slidingBlockPuzzleSover;

/**
 * Contains all the implementations of the various ai solvers
 * @author paul
 *
 */
public class AiSolvers {

	/**
	 * Start the ai indicated by the enum
	 */
	public void runAiDispath(AiType type, GameBoard gameBoard){
		if (AiType.DFS.equals(type)) runDfs(gameBoard);
		if (AiType.BFS.equals(type)) runBfs(gameBoard);
		
		// If not the other two than this one
		runAStar(gameBoard); 
		
	}

	private void runAStar(GameBoard gameBoard) {
		// TODO Auto-generated method stub
		
	}

	private void runBfs(GameBoard gameBoard) {
		// TODO Auto-generated method stub
		
	}

	private void runDfs(GameBoard gameBoard) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
