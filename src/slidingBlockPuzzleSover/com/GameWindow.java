package slidingBlockPuzzleSover.com;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;


/**
 * This is the main game window class. It will display the game board and allow the user to execute 
 * and run a number of different game types. 
 * @author paul
 */

public class GameWindow {

	/** Gui panels and frames */
	public JPanel mainPanel; 
	public JFrame mainFrame; 
	public JPanel gridPanel; 
	
	/** The main gameboard from which all games are derived */
	public GameBoard gameBoard = new GameBoard(new ArrayList<ArrayList<Integer>>());
	
	/** Buttons we listen to for actions */ 
	private JButton btnRunAi;
	private JButton btnChangeGrid;	
	
	/** Ai buttons for types and options */
	private JRadioButton btnAiDfs;
	private JRadioButton btnAiBfs;
	private JRadioButton btnAiAStar;
	private JRadioButton btnSaveTime;
	private JRadioButton btnSaveSpace;
	private JRadioButton btnHeurOffset;
	private JRadioButton btnHeurPlace; 
	
	/** End game labels */
	private JLabel aiMoves = new JLabel();
	private JLabel aiTime = new JLabel();
	private JLabel boardsIns = new JLabel(); 
	
	/** Entry box for a user to put in the grid size he or she wants */
	private JTextField gridSizeField;
	
	public GameWindow() {
		// Create main panel and frame
		this.mainFrame = new JFrame("Sliding Block Puzzle Solver");
		
		// 2 rows 1 column
		// First row holds the buttons, second the grid.
		this.mainFrame.setLayout(new GridLayout(2, 1));
		
		// Set some style info
		this.mainFrame.setSize(900, 700);
		//this.mainFrame.pack();
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Create main panel
		this.mainPanel = new JPanel(); 

		// Add GUI items to panel
		setupPanel(); 
		
		// Setup grid
		setupGrid(); 
		
		// Add panel to frame
		this.mainFrame.add(this.mainPanel); 
		this.mainFrame.add(this.gridPanel); 

	}

	public void showWindow(){                                                        
		
		// Show frame
		this.mainFrame.setVisible(true); 
		
	}

	/**
	 * Setup gui elements for the panel
	 */
	private void setupPanel() {

		// Main options
		JPanel homeEntry = new JPanel();
		homeEntry.setLayout(new FlowLayout());
		
		// AI options
		JPanel lowerRow = new JPanel();
		lowerRow.setLayout(new FlowLayout());
		
		// Label Describing box
		JLabel entryLabel = new JLabel("Enter in grid size. Just enter in single number for NxN grid");
		homeEntry.add(entryLabel); 
				
		gridSizeField = new JTextField(5);
		homeEntry.add(gridSizeField); 
		
		
		// Allow user to change grid size to whatever the number they entered in the textfield
		btnChangeGrid = new JButton("Change Grid");       
		btnChangeGrid.setSize(20, 20);
		
		btnChangeGrid.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	// Only change grid size if the user provided something
            	if(!gridSizeField.getText().trim().isEmpty()){
            		// Change grid size
            		setGridSize(Integer.parseInt(gridSizeField.getText())); 
            		refreshView(); 
            	} else {
            		System.out.print("\nYou must provide a size!\n");
            	}
            }

        });  
		
		homeEntry.add(btnChangeGrid);
				
		// Checkboxes, only allow one button to be selected at a time
		ButtonGroup checkBoxGroup = new ButtonGroup();
		
		JRadioButton checkHuman = new JRadioButton("Human");
		JRadioButton checkAi = new JRadioButton("AI");
		
		// Disable run ai on button selection
		checkHuman.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
            	btnRunAi.setEnabled(false);         	
            }
        });   
		
		// Enable run ai on button selection
		checkAi.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
            	btnRunAi.setEnabled(true); 
            }
        });   
		
		checkBoxGroup.add(checkHuman);
		checkBoxGroup.add(checkAi); 
		
		// Add the buttons to the button panel
		homeEntry.add(checkHuman); 
		homeEntry.add(checkAi); 
		
	    // Will always start on Human panel
		checkHuman.setSelected(true);
		
		// Button to run the ai solver
		btnRunAi = new JButton("Run Solver");       
		btnRunAi.setSize(20, 20);
		
		btnRunAi.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	// Execute solver AI
            	// If we are doing an a* search...
            	if(btnAiAStar.isSelected()){
            		// Set the gameboard with the heuristic we are using
            		if(btnHeurOffset.isEnabled()){
            			gameBoard.setHeuristicType(Heuristic.OFFSETPOS);
            		}
            		if(btnHeurPlace.isEnabled()){
            			gameBoard.setHeuristicType(Heuristic.WRONGPLACECOUNT);
            		}
            		
            	}
            	
            	// Get the selected AI type
            	// Time how long it takes (roughly)
            	long start = System.nanoTime();    
            	ArrayList<Integer> moveList = AiSolvers.runAiDispath(getSelectedAi(), gameBoard, btnSaveSpace.isSelected());
            	long elapsedTime = System.nanoTime() - start;          	
            	
            	if(moveList == null){
            		// We did not find a solution
            		System.out.print("Did not find a solution");
            	} else {
                	// Show the winning moves being made
            		// Last move on the list is the number of inspected boards
            		int numInspected = moveList.remove(moveList.size() - 1);
            		
                	showEndGame(moveList, elapsedTime, numInspected);
            	}          
            }

        });   

		// Start disabled
		btnRunAi.setEnabled(false); 
		
		homeEntry.add(btnRunAi);
		
		// Options to select particular ai
		ButtonGroup aiCheckBoxGroup = new ButtonGroup();
		
		btnAiDfs = new JRadioButton("DFS");
		btnAiBfs = new JRadioButton("BFS");
		btnAiAStar = new JRadioButton("A*");
		
		btnAiDfs.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	//btnRunAi.setEnabled(false); 
            }
        });   
		
		btnAiBfs.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	//btnRunAi.setEnabled(false); 
            }
        });   
		
		btnAiAStar.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	//btnRunAi.setEnabled(false); 
            }
        });   
		
		// Add radio buttons to group
		aiCheckBoxGroup.add(btnAiDfs);
		aiCheckBoxGroup.add(btnAiBfs);
		aiCheckBoxGroup.add(btnAiAStar);
		
		// Add buttons to panel
		homeEntry.add(btnAiDfs);
		homeEntry.add(btnAiBfs);
		homeEntry.add(btnAiAStar);
		
		// Always start on DFS
		btnAiDfs.setSelected(true); 
		
		// Switch to use space saving option
		ButtonGroup spaceCheckBoxGroup = new ButtonGroup();
		
		btnSaveTime = new JRadioButton("Better Time");
		btnSaveSpace = new JRadioButton("Better Space");
		
		// Add to group
		spaceCheckBoxGroup.add(btnSaveTime);
		spaceCheckBoxGroup.add(btnSaveSpace);
		
		// Start on save time
		btnSaveTime.setSelected(true); 
		
		// Add to panel
		lowerRow.add(btnSaveTime);
		lowerRow.add(btnSaveSpace);
		
		// Buttons for selecting heuristic type
		ButtonGroup heuristicTypeGrp = new ButtonGroup();
		
		btnHeurOffset = new JRadioButton("Total Offset Position");
		btnHeurPlace = new JRadioButton("Total Numbers Out of Place");
		
		heuristicTypeGrp.add(btnHeurOffset);
		heuristicTypeGrp.add(btnHeurPlace);
		
		// Start with offset
		btnHeurOffset.setSelected(true);
		
		// Add to panel
		lowerRow.add(btnHeurOffset);
		lowerRow.add(btnHeurPlace); 
		
		// Add the panel to the main panel
		this.mainPanel.add(homeEntry); 
		this.mainPanel.add(lowerRow);
		
		// These Labels will show end game info
		JPanel bottomRow = new JPanel();
		bottomRow.setLayout(new FlowLayout());		
		bottomRow.add(aiMoves);
		
		JPanel bottomRow2 = new JPanel();
		bottomRow2.setLayout(new FlowLayout());	
		
		JPanel bottomRow3 = new JPanel();
		bottomRow3.setLayout(new FlowLayout());	
		
		//this.mainPanel.add(aiMoves);
		bottomRow2.add(aiTime);
		bottomRow3.add(boardsIns); 
		
		this.mainPanel.add(bottomRow);
		this.mainPanel.add(bottomRow2);
		this.mainPanel.add(bottomRow3);
	}
	
	/**
	 * Create a grid of buttons
	 */
	private void setupGrid() {
		this.gridPanel = new JPanel(); 	
		//this.gridPanel.setSize(20, 20); 
		this.gridPanel.setBorder(new EmptyBorder(25,25,25,25));
		
		// Start with a simple 2X2 grid
		setGridSize(2); 
		
	}

	/**
	 * Tear down the old grid and create this new grid
	 *@param n, the size of the grid 
	 */
	private void setGridSize(int n) {
		
		// Clear out outdated values if this is the 
		// second time we use this function
		this.gridPanel.removeAll();
		
		// Clear old game board data
		this.gameBoard.resetGameBoard(); 
		
		// Set new size grid
		this.gridPanel.setLayout(new GridLayout(n, n));

		// Array of buttons to form the grid
		JButton[][] grid = new JButton[n][n]; 
			
		// Generate all the number between 0 and n^2
		ArrayList<Integer> intArray = new ArrayList<Integer>();
		
		for(int i = 1; i <= (n * n) - 1; i++) {	
			intArray.add(i); 
		}
		// Mix up the list
		// We need to have this list be solvable
		// Keep mixing until we have a solvable array
		Collections.shuffle(intArray); 
		while(!isSolvable(intArray, n)) {
			Collections.shuffle(intArray); 
		}
		
		// Counter to control when we dispense a number
		int numCounter = 0; 
		
		// List will hold values for a row
		ArrayList<Integer> row = new ArrayList<Integer>(); 
		for(int i = 0; i < n; i++) {			
				
			row.clear(); 
			for(int j = 0; j < n; j++){				
				
				// First button is blank for the square a player can move to
				if(i == 0 && j == 0){
					grid[i][j] = new JButton(" "); 
					row.add(-1); 
					this.gridPanel.add(grid[i][j]); 
					continue; 
				}
				
				
				// Add that to the grid row list
				JButton numButton = new JButton(String.valueOf(intArray.get(numCounter))); 
				
				// Add an action listener to each button
				// On click try to move the number into the open space
				numButton.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	
		            	// Do not allow a user to make a move if they 
		            	// ai run option is selected
		            	if(btnRunAi.isEnabled()){
		            		return; 
		            	}
		            	
		            	// See if move is valid
		            	Boolean result = 
		            			moveNumber(Integer.parseInt(((JButton)e.getSource()).getText())); 
		            	
		            	if(!result){
		            		// Bad move
		            		JDialog warnDialog = new JDialog();
		            		warnDialog.setSize(450,100);
		            		warnDialog.setTitle("Warning");
		            		warnDialog.add(new Label("Bad Move. Select a number directly adjacent to the open space")); 
		            		warnDialog.setVisible(true); 
		            	} 
		            	
		            	// Valid move, refresh the view
		            	refreshGridView(); 
		            	
		            	// Check for a win after each move
		            	result = gameBoard.checkForWin();
		            	
		            	// Game is won
		            	if(result){
		            		JDialog winDialog = new JDialog();
		            		winDialog.setSize(450,100);
		            		winDialog.setTitle("Winning");
		            		winDialog.add(new Label("You won!")); 
		            		winDialog.setVisible(true); 
		            	}
		            }


		        });  
				
				grid[i][j] = numButton; 
				this.gridPanel.add(grid[i][j]); 
				row.add(intArray.get(numCounter)); 
				numCounter++;
			}
			
			// Add row to list
			ArrayList<Integer> copyRow = new ArrayList<Integer>(); 
			for(int item : row){			
				copyRow.add(item);				
			}
			
			this.gameBoard.getGameList().add(copyRow); 
		}
		
		System.out.print("New grid is: " + this.gameBoard.getGameList().toString() + "\n"); 
    	//List<Integer> posMoves = AiUtils.getNextMove(this.gameList);
		
	}
	
	/** 
	 * We need to ensure a puzzle is solvable. To do so
	 * take the list and count the number of inversions.
	 * @param intArray
	 * @param width, the width of the grid 
	 * @return boolean, true for solvable, false otherwise
	 * 
	 * Source : http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
	 */
	private boolean isSolvable(ArrayList<Integer> intArray, int width) {
		int inversions = 0;
		
		for(int i = 0; i < intArray.size(); i++){
			int num = intArray.get(i);
			
			// Starting at the position the number is in
			// count the numbers below it remaining in the list
			for (int j = i; j < intArray.size(); j++){
				if(intArray.get(j) < num) inversions++; 
			}

		}
		
		// Grid width is odd
		if(width % 2 != 0){
			// Number of inversions must be even to
			// be solvable 
			return (inversions % 2 == 0);
		} else {
			// Since empty spot is always on even row
			// then the number of inversions must be odd
			return (inversions % 2 != 0);
		}
		
		// Even inversions counts are solvable odd are not
		//return (inversions % 2 == 0);
		
	}

	/** 
	 * User has entered a new grid value, refresh the view
	 */
	private void refreshView() {
		this.mainFrame.revalidate(); 
	}

	/**
	 * Move a number from one button to the empty space
	 * @param num, the number trying to be moved
	 */
	private boolean moveNumber(int num) {
		//System.out.print("Number is " + num); 
		
		// Get the number grid and look and see if this is a valid move
		// find the number the player is trying to move
		int posR = -1, posC = -1;
		for(int i = 0; i < this.gameBoard.getGameList().size(); i++){
			for(int j = 0; j < this.gameBoard.getGameList().get(i).size(); j++){
				if(this.gameBoard.getGameList().get(i).get(j) == num){
					posR = i;
					posC = j; 		
				}
			}
		}
		
		// Sanity check, should have found a number
		if(posR == -1 || posC == -1){
			throw new Error("Did not find the number you clicked!");
		}
		
		// Try to access empty space near found number
		// If we get an exception, outside array, ignore. 
		try {
			if(this.gameBoard.getGameList().get(posR).get(posC + 1) == -1) {
				swapEmptySpace(posR, posC);
				return true; 
			}
		} catch(Exception e){
		}
		try {
			if(this.gameBoard.getGameList().get(posR).get(posC - 1) == -1) {
				swapEmptySpace(posR, posC);
				return true; 
			}
		} catch(Exception e){
		}
		try {
			if(this.gameBoard.getGameList().get(posR + 1).get(posC) == -1) {
				swapEmptySpace(posR, posC);
				return true;
			}
		} catch(Exception e){
		}
		try {
			if(this.gameBoard.getGameList().get(posR - 1).get(posC) == -1) {
				swapEmptySpace(posR, posC);
				return true;
			}
		} catch(Exception e){
		}

		
		// Was unable to swap, bad move
		return false;
	}

	/**
	 * Swap the empty space with the value a user clicked
	 * @param posR, row value to be swapped is in
	 * @param posC, column value to be swapped is in 
	 */
	private void swapEmptySpace(int posR, int posC) {		
		
		int emptyR = gameBoard.getEmptyRow();  
		int emptyC = gameBoard.getEmptyCol();
		
		// Swap empty space with new number 
		this.gameBoard.getGameList().get(emptyR).set(emptyC, this.gameBoard.getGameList().get(posR).get(posC)); 
		
		// Swap new number with empty space 
		this.gameBoard.getGameList().get(posR).set(posC, -1); 
		
		// Update gameboard with new empty space number
		gameBoard.setEmptyRow(posR);
		gameBoard.setEmptyCol(posC); 
		
		// Increment swaps
		gameBoard.incrementSwaps(); 
	}
	
	/**
	 * We have moved a piece on the board, rebuild the board
	 */
	private void refreshGridView() {
		
		// Clear out grid panel
		this.gridPanel.removeAll(); 
		
		// Array of buttons to form the grid
		JButton[][] grid = new JButton[gameBoard.getGameList().size()][gameBoard.getGameList().size()]; 
		
		
		for(int i = 0; i < gameBoard.getGameList().size(); i++) {			

			for(int j = 0; j < gameBoard.getGameList().get(i).size(); j++){				
				
				// When the gamelist's num is -1 that is the blank space
				if(gameBoard.getGameList().get(i).get(j) == -1){
					grid[i][j] = new JButton(" "); 
					this.gridPanel.add(grid[i][j]); 
					continue; 
				}
				
				JButton numButton = new JButton(String.valueOf(gameBoard.getGameList().get(i).get(j))); 
				
				// Add an action listener to each button
				// On click try to move the number into the open space
				numButton.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	// Do not allow a user to make a move if they 
		            	// ai run option is selected
		            	if(btnRunAi.isEnabled()){
		            		return; 
		            	}
		            	
		            	// Check if move is valid
		            	Boolean result = 
		            			moveNumber(Integer.parseInt(((JButton)e.getSource()).getText())); 
		            	
		            	if(!result){
		            		// Bad move
		            		JDialog warnDialog = new JDialog();
		            		warnDialog.setSize(450,100);
		            		warnDialog.setTitle("Warning");
		            		warnDialog.add(new Label("Bad Move. Select a number directly adjacent to the open space")); 
		            		warnDialog.setVisible(true); 
		            	} 
		            	
		            	// Valid move, refresh the view
		            	refreshGridView(); 
		            	
		            	// Check for a win after each move
		            	result = gameBoard.checkForWin();
		            	
		            	// Game is won
		            	if(result){
		            		JDialog winDialog = new JDialog();
		            		winDialog.setSize(450,100);
		            		winDialog.setTitle("Winning");
		            		winDialog.add(new Label("You won!")); 
		            		winDialog.setVisible(true); 
		            	}
		            }


		        });  
				
				grid[i][j] = numButton; 
				this.gridPanel.add(grid[i][j]); 
			}
			
		}
		
		// Rebuilt grid so refresh view
		refreshView(); 
		
	}

	/** 
	 * Look through the ai buttons and 
	 * return the selected ai type enum
	 */
	private AiType getSelectedAi() {
	
		if (btnAiDfs.isSelected()) return AiType.DFS;
		if (btnAiBfs.isSelected()) return AiType.BFS; 	
		//if (aiDfs.isSelected()) return AiType.ASTAR;
		
		// If it is not the others it must be this one
		return AiType.ASTAR;
		
	}
	
	/**
	 * Given a list of numbers to move make each swap
	 * well waiting a period of time before making the next one.
	 * When finished showing the moves, print some endgame stats in a popup
	 * 
	 * @param moveList, a list of moves to make in order
	 * @param elapsedTime, the amount of time in nanoseconds the ai took
	 * @param inspectedBoards, the number of boards the ai looked at
	 */
	private void showEndGame(ArrayList<Integer> moveList, double elapsedTime, int numInspected){
		
    	for (int move : moveList){
			//System.out.print("Moved the: " + String.valueOf(move) + "\n");
			AiUtils.makeMove(move, gameBoard);
			refreshGridView(); 
    	}

    	// Print out some stats in the GUI
    	printEndGameStats(moveList, elapsedTime, numInspected);
    	
    	// FIXME GET THIS WORKING
//    	ActionListener taskPerformer = new ActionListener() {
//    		public void actionPerformed(ActionEvent evt) {
//    			refreshGridView();
//    		}
//    	};
//
//
//    	Timer guiTimer = new Timer(500, taskPerformer);
//    	guiTimer.setRepeats(true);
//    	guiTimer.setInitialDelay(500); 
//    	
//    	//boolean nextNum = false;
//		for (int move : moveList){
//			final int myMove = move;  
//			SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					System.out.print("Moved the: " + String.valueOf(myMove) + "\n");
//					AiUtils.makeMove(myMove, gameBoard);
//					refreshGridView();  
//					
//					try {
//						//refreshGridView();
//						Thread.sleep(500);
//						//nextNum = true;
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//				}
//			});

			//AiUtils.makeMove(move, gameBoard);
			
			// Print the move we just did
			//System.out.print("Moved the: " + String.valueOf(move) + "\n");
			
//			Thread appThread = new Thread() {
//				public void run() {
//					try {
//						SwingUtilities.invokeLater(doHelloWorld);
//					}
//					catch (Exception e) {
//						e.printStackTrace();
//					}
//					//System.out.println("Finished on " + Thread.currentThread());
//				}
//			};
//			appThread.start();
//
//			try {
//				//refreshGridView();
//				Thread.sleep(500);
//				//nextNum = true;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			
//			SwingUtilities.invokeLater(new Runnable() {
//			    public void run() {
//			    	System.out.print("Printing");
//			    }
//			});
			
//			refreshGridView();
		    	

		
		// All done print some game stats
	}

	/**
	 * Print some end game stats, updating the GUI
	 * @param moveList, a list of moves to make in order
	 * @param elapsedTime, the amount of time in nanoseconds the ai took
	 * @param inspectedBoards, the number of boards the ai looked at
	 */
	public void printEndGameStats(ArrayList<Integer> moveList, double elapsedTime, int numInspected){
		
		// Convert nanoseconds to seconds 
		double seconds = (double)elapsedTime / 1000000000.0;

		System.out.print("\nNumber of inspected game boards: " + String.valueOf(numInspected) + "\n");
    	System.out.print("\nIt took roughly:" + String.valueOf(seconds) + "\n");
		
		aiMoves.setText("The moves to win: " + moveList.toString() + "\n");
		aiTime.setText("\nIt took roughly:" + String.valueOf(seconds) + " Seconds\n");
		boardsIns.setText("\nNumber of inspected game boards: " + String.valueOf(numInspected) + "\n");
		
		refreshView();
	}
	

}
