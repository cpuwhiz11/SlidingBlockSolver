package slidingBlockPuzzleSover;

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
	
	/** Holds the game board's distribution of numbers */
	public ArrayList<ArrayList<Integer>> gameList = new ArrayList<ArrayList<Integer>>();
	
	/** Buttons we listen to for actions */ 
	private JButton btnRunAi;
	private JButton btnChangeGrid;
	private JTextField gridSizeField;
	
	public GameWindow() {
		// Create main panel and frame
		this.mainFrame = new JFrame("Sliding Block Puzzle Solver");
		
		// 2 rows 1 column
		// First row holds the buttons, second the grid.
		this.mainFrame.setLayout(new GridLayout(2, 1));
		
		// Set some style info
		this.mainFrame.setSize(1000, 700);
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

		JPanel homeEntry = new JPanel();
		homeEntry.setLayout(new FlowLayout());
		
		// Label Describing box
		JLabel entryLabel = new JLabel("Enter in grid size. Just enter in single number for NxN grid");
		homeEntry.add(entryLabel); 
				
		gridSizeField = new JTextField(5);
		homeEntry.add(gridSizeField); 
		
		
		// Allow user to change grid size to whatver the number they entered in the textfield
		btnChangeGrid = new JButton("Change Grid");       
		btnChangeGrid.setSize(20, 20);
		
		btnChangeGrid.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	// Execute solver AI
            	setGridSize(Integer.parseInt(gridSizeField.getText())); 
            	refreshView(); 
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
		
		//checkAi.addActionListener(this);
		
		checkBoxGroup.add(checkHuman);
		checkBoxGroup.add(checkAi); 
		
		// Add the buttons to the button panel
		homeEntry.add(checkHuman); 
		homeEntry.add(checkAi); 
		
		// Button to run the ai solver
		btnRunAi = new JButton("Run Solver");       
		btnRunAi.setSize(20, 20);
		
		btnRunAi.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	// Execute solver AI
            	throw new Error("HAVE NOT DONE THIS YET"); 
            }
        });   

		// Start disabled
		btnRunAi.setEnabled(false); 
		
		homeEntry.add(btnRunAi);
		
	    // Will always start on Human panel
		checkHuman.setSelected(true);
		
		// Add the panel to the main panel
		this.mainPanel.add(homeEntry); 
		
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
		
		// Clear old list if needed
		this.gameList.clear(); 
		
		this.gridPanel.setLayout(new GridLayout(n, n));

		// Array of buttons to form the grid
		JButton[][] grid = new JButton[n][n]; 
			
		// Generate all the number between 0 and n^2
		ArrayList<Integer> intArray = new ArrayList<Integer>();
		
		for(int i = 1; i <= (n * n) - 1; i++) {	
			intArray.add(i); 
		}
		// Mix up the list
		Collections.shuffle(intArray); 
		
		// Counter to control when we dispense a number
		int numCounter = 0; 
		
		// List will hold values for a row
		ArrayList<Integer> row = new ArrayList<Integer>(); 
		for(int i = 0; i < n; i++) {			
				
			row.clear(); 
			for(int j = 0; j < n; j++){				
				
				// First button is blank for the square a player can move to
				if(i == 0 && j == 0){
					//tempBtn.setText(" "); 
					//grid[i][j] = tempBtn;
					grid[i][j] = new JButton(" "); 
					row.add(-1); 
					this.gridPanel.add(grid[i][j]); 
					continue; 
				}
				
				
				// Add that to the grid row list
				//tempBtn.setText(String.valueOf(intArray.get(numCounter))); 
				//grid[i][j] = tempBtn;
				JButton numButton = new JButton(String.valueOf(intArray.get(numCounter))); 
				
				// Add an action listener to each button
				// On click try to move the number into the open space
				numButton.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	// Execute solver AI
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
			
			this.gameList.add(copyRow); 
		}
		
		System.out.print("New grid is: " + this.gameList.toString()); 
		
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
		for(int i = 0; i < this.gameList.size(); i++){
			for(int j = 0; j < this.gameList.get(i).size(); j++){
				if(this.gameList.get(i).get(j) == num){
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
		try {
			if(this.gameList.get(posR).get(posC + 1) == -1) {
				swapEmptySpace(posR, posC);
				return true; 
			}
		} catch(Exception e){
		}
		try {
			if(this.gameList.get(posR).get(posC - 1) == -1) {
				swapEmptySpace(posR, posC);
				return true; 
			}
		} catch(Exception e){
		}
		try {
			if(this.gameList.get(posR + 1).get(posC) == -1) {
				swapEmptySpace(posR, posC);
				return true;
			}
		} catch(Exception e){
		}
		try {
			if(this.gameList.get(posR - 1).get(posC) == -1) {
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
		// Find -1 which indicates the empty space 
		int emptyR = -1, emptyC = -1;
		for(int i = 0; i < this.gameList.size(); i++){
			for(int j = 0; j < this.gameList.get(i).size(); j++){
				if(this.gameList.get(i).get(j) == -1){
					emptyR = i;
					emptyC = j; 		
				}
			}
		}
		
		// Sanity check, should have found empty space
		if(emptyR == -1 || emptyC == -1){
			throw new Error("Did not find the empty space!");
		}
		
		// Swap empty space with new number 
		this.gameList.get(emptyR).set(emptyC, this.gameList.get(posR).get(posC)); 
		
		// Swap new number with empty space 
		this.gameList.get(posR).set(posC, -1); 
	}
	
	/**
	 * We have moved a piece on the board, rebuild the board
	 */
	private void refreshGridView() {
		
		// Clear out grid panel
		this.gridPanel.removeAll(); 
		
		// Array of buttons to form the grid
		JButton[][] grid = new JButton[gameList.size()][gameList.size()]; 
		
		
		for(int i = 0; i < gameList.size(); i++) {			

			for(int j = 0; j < gameList.get(i).size(); j++){				
				
				// When the gamelist's num is -1 that is the blank space
				if(gameList.get(i).get(j) == -1){
					grid[i][j] = new JButton(" "); 
					this.gridPanel.add(grid[i][j]); 
					continue; 
				}
				
				JButton numButton = new JButton(String.valueOf(gameList.get(i).get(j))); 
				
				// Add an action listener to each button
				// On click try to move the number into the open space
				numButton.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	// Execute solver AI
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
		            }


		        });  
				
				grid[i][j] = numButton; 
				this.gridPanel.add(grid[i][j]); 
			}
			
		}
		
		// Rebuilt grid so refresh view
		refreshView(); 
		
	}

}