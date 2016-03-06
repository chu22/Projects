/* 
 * Grid.java
 * 
 *  This class represents the main playing grid for Minesweeper.
 *  It contains all cells as well as other game data, like the number of cells clicked
 *  and the game state.
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JPanel;

public class Grid extends JPanel{
	private Cell grid[][];		//main board
	private Cell bombs[];		//contains each bomb
	private int clicked;  		//number of valid Cells clicked so far, game ends if this reaches 90
	private int nFlags;   		//number of "unused" flags
	private boolean explode; 	//true if user clicks on a bomb
	private boolean firstClick;	//true if user has not clicked
	
	private int lastPressedr;	//row of the cell the user last left clicked
	private int lastPressedc;	//col of the cell the user last left clicked
	
	//game constants
	private final int CELL_DIM = 16;
	private final int N_ROWS = 10;
	private final int N_COLS = 10;
	private final int N_BOMBS = 10;
	
	public static final int height = 160;
	public static final int width = 160;
	
	/* CONSTRUCTOR/INITIALIZATION FUNCTIONS */
	
	public Grid(){
		grid = new Cell[N_ROWS][N_COLS];
		bombs = new Cell[N_BOMBS];
		clicked = 0;
		nFlags = N_BOMBS;
		explode = false;
		firstClick = true;
		for(int i = 0;i<N_ROWS;i++){
			for(int j = 0;j<N_COLS;j++){
				grid[i][j]= new Cell(i,j);
			}
		}
		
	}
	
	/*
	 * Initializes the board. This function is only called upon successful press and release of a single cell.
	 * This guarantees that the user's first click will not be a bomb.
	 */
	private void init_board(int r, int c){
		setBombs(r,c);
		setBoard();
	}
	
	private void setBombs(int row, int col){
		int r;
		int c;
		int ctr = 0;
		Random rn = new Random();
		while(ctr<N_BOMBS){
			r = rn.nextInt(N_BOMBS);
			c = rn.nextInt(N_BOMBS);
			if(r!=row&&c!=col&&!grid[r][c].isBomb()){
				grid[r][c].setVal(-1);
				bombs[ctr]=grid[r][c];
				ctr++;
			}
		}	
	}
	
	private void setBoard(){
		for(int r = 0;r<N_ROWS;r++){
			for(int c = 0;c<N_COLS;c++){
				if(!grid[r][c].isBomb()){
					grid[r][c].setVal(calcVal(r,c));
				}
			}
		}
	}
	
	/*
	 * Calculates cell values
	 */
	private int calcVal(int r, int c){
		int v=0;
		for(int i = r-1;i<=r+1;i++){             //using a 3x3 for loop to check adjacency, to cut down on if statements/code clutter
			for(int j = c-1;j<=c+1;j++){
				if(grid[r][c].isAdjacent(i,j)&&grid[i][j].isBomb()){
					v++;
				}
			}
		}
		return v;
	}

	/* GAME STATE CHECKS */
	
	public boolean gameOver(){
		return clicked==90||explode;
	}
	
	public boolean gameWon(){
		return clicked==90;
	}
	
	/* GETTERS */

	public int getnFlags(){
		return nFlags;
	}
	
	public boolean firstClicked(){
		return firstClick;
	}
	
	/* PAINT OVERRIDE */
	
	/*
	 * Override JPanel method, draws graphics onto panel
	 */
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                g.drawImage(grid[i][j].getImage(), j*CELL_DIM,
                    i*CELL_DIM, null);
            }
        }
    }
	
	/* MOUSE EVENT HANDLING */
	
	public void mousePressed(MouseEvent e) {
		int c = convertPos(e.getX());
		int r = convertPos(e.getY());
		lastPressedr = r;
		lastPressedc = c;
		if(e.getButton() == MouseEvent.BUTTON1)			//Left mouse click
	    {
			if(!explode&&clicked<90&&validCoord(r,c)){	//registers click only if game not ended and mouse clicks onto board
					grid[r][c].LPressed();
			}
	    }	    
	    else if(e.getButton() == MouseEvent.BUTTON3)	//Right mouse click
	    {
	    	if(!explode&&clicked<90&&validCoord(r,c)){
				Cell s = grid[r][c];
				s.RPressed();
				if(s.getState()==2){  		//square was changed from blank to M, decrement flag ctr
					nFlags--;        
				}
				else if(s.getState()==3){  	//square was changed from M to ?, increment flag ctr 
					nFlags++;
				}
	    	}
	    }
	}

	public void mouseReleased(MouseEvent e) {
		int r, c;
		if(e.getButton() == MouseEvent.BUTTON1)
	    {
			c = convertPos(e.getX());
			r = convertPos(e.getY());
			boolean over = lastPressedr==r&&lastPressedc==c;	//true if cell being released is same as the last pressed
			if(!explode&&clicked<90){
				if(over&&grid[r][c].getState()!=6){				//mouse is over the same cell and it is not already clicked
					if(firstClick){								
						init_board(r,c);						//do not initialize board until first click
						firstClick = false;
					}
					clickCell(r,c);
				}
				else{									
					grid[lastPressedr][lastPressedc].LReleasedOther();  //cell that is being released was not pressed
				}
			}
		}
	}
	
	public void doubleMousePressed(MouseEvent e){
		int c = convertPos(e.getX());
		int r = convertPos(e.getY());
		boolean over = lastPressedr==r&&lastPressedc==c;
		if(!explode&&clicked<90&&over&&grid[r][c].getState()==6){		//if second button press is over same cell as first button press, and cell is already clicked
			for(int i = r-1;i<=r+1;i++){
				for(int j = c-1;j<=c+1;j++){
					if(grid[r][c].isAdjacent(i,j)){
						grid[i][j].LPressed();							//press adjacent cells
					}
				}
			}
		}
	}
	
	public void doubleMouseReleased(MouseEvent e){
		int c = convertPos(e.getX());
		int r = convertPos(e.getY());
		boolean over = lastPressedr==r&&lastPressedc==c;
		if(!explode&&clicked<90){
			if(over&&calcFlags(r,c)==grid[r][c].getVal()){				//if mouse is over last cell pressed, correct number of adjacent flags
				for(int i = r-1;i<=r+1;i++){
					for(int j = c-1;j<=c+1;j++){
						if(grid[r][c].isAdjacent(i,j)&&grid[i][j].getState()!=6){
							clickCell(i,j);								//simulate left click on each adjacent unclicked cell
						}
					}
				}
			}
			else{														//otherwise, unpress buttons pressed by doubleMousePressed function
				r = lastPressedr;
				c = lastPressedc;
				for(int i = r-1;i<=r+1;i++){
					for(int j = c-1;j<=c+1;j++){
						if(grid[r][c].isAdjacent(i,j)){
							grid[i][j].LReleasedOther();
						}
					}
				}
			}
		}
	}
	
	/* MOUSE EVENT HELPER FUNCTIONS */
	
	/*
	 * Shows cell indicated by r and c, will recursively call if cell value is zero.
	 */
	private void showAdj(int r, int c){
		Cell s = grid[r][c];
		if(s.getState()==0){
			s.showVal();
			clicked++;
			if(clicked==90){
				markBombs();
			}
			if(s.isZero()){						
				for(int i = r-1;i<=r+1;i++){
					for(int j = c-1;j<=c+1;j++){
						if(grid[r][c].isAdjacent(i,j)){
							showAdj(i,j);
						}
					}
				}
			}
		}
	}
	
	/*
	 * Helper function used to minimize duplicate code -- used in both mouseReleased and doubleMouseReleased
	 */
	private void clickCell(int r, int c){
		Cell s = grid[r][c];
		s.LReleasedSame();
		if(s.getState()==6){						//cell has value shown now
			if(s.isBomb()){
				explode = true;
				showBombs();						//game lost, show where bombs are
			}
			else{									
				if(s.isZero()){          			//if zero, automatically show adjacent cells
					for(int i = r-1;i<=r+1;i++){
						for(int j = c-1;j<=c+1;j++){
							if(grid[r][c].isAdjacent(i,j)){
								showAdj(i,j);
							}
						}
					}
				}
				clicked++;
				if(clicked==90){
					markBombs();
				}
			}
		}
	}
	
	/*
	 * Calculates number of flags around a cell
	 */
	private int calcFlags(int r, int c){
		int v=0;
		for(int i = r-1;i<=r+1;i++){             //using a 3x3 for loop to check adjacency, to cut down on if statements/code clutter
			for(int j = c-1;j<=c+1;j++){
				if(grid[r][c].isAdjacent(i,j)&&grid[i][j].getState()==2){
					v++;
				}
			}
		}
		return v;
	}
	
	/*
	 * Changes all bombs on grid to BOMB label
	 * except for the one that was just clicked.
	 * Also marks all incorrectly flagged cells. 
	 * Called only when user loses.
	 */
	private void showBombs(){
		for(int r = 0;r<N_ROWS;r++){
			for(int c = 0;c<N_COLS;c++){
				if(grid[r][c].getState()!=6){
					grid[r][c].showBomb();
				}
			}
		}
	}

	/*
	 * Marks all bombs on grid to BOMB label
	 * except for the one that was just clicked.
	 * Called only when user loses.
	 */
	private void markBombs(){
		for(int i = 0;i<N_BOMBS;i++){
			if(bombs[i].getState()!=2){
				nFlags--;
			}
			bombs[i].flagBomb();
		}
	}
	
	/*
	 * Helper function that returns row/col coordinate from a MouseEvent coordinate.
	 */
	private int convertPos(int x){
		return x/CELL_DIM;
	}
	
	/*
	 * Helper function that checks whether mouse coordinate was valid on the grid.
	 */
	private boolean validCoord(int r, int c){
		return r<10&&r>=0&&c<10&&c>=0;
	}
}
