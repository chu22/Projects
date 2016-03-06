/* Cell.java 
 * 
 * This class represents the clickable cells inside the grid
 * It stores all data related to each cell, including images, values and row/col
 * 
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Image;
import javax.swing.ImageIcon;

public class Cell {
	private int r; 					//grid row
	private int c; 					//grid column
	private int val; 				//number of adjacent mines, or -1 if it is a mine
	private int state; 				//image state, used to communicate label with Grid class			
	private Image label;			//current image to be displayed on cell
	
	private static String val_button_names[] = {"button_pressed_v.gif", "button_1.gif", "button_2.gif", "button_3.gif", "button_4.gif", 
			"button_5.gif", "button_6.gif", "button_7.gif", "button_8.gif"};	
	private static final Image NORM =  new ImageIcon("button_normal.gif").getImage();						//state 0
	private static final Image NORM_P = new ImageIcon("button_pressed.gif").getImage();						//state 1
	private static final Image FLAG = new ImageIcon("button_flag.gif").getImage();							//state 2
	private static final Image QUESTION = new ImageIcon("button_question.gif").getImage();					//state 3
	private static final Image QUESTION_P = new ImageIcon("button_question_pressed.gif").getImage();		//state 4

	private Image BOMB;																						//state 5
	private Image VAL;																						//state 6
	
	public static final int height = 16;		//cell height
	public static final int width = 16;			//cell width
	
	/* CONSTRUCTOR/INITIALIZATION METHODS */
	
	public Cell(int row, int col){
		r = row;
		c = col;
		val = -2;  //initializing to -2 just to mark that the square value hasn't been calculated yet 
		label = NORM;
		state = 0;
	}
	
	/*
	 * Initializes value and sets the last 2 images based on whether the cell is a bomb or not.
	 */
	public void setVal(int v){
		val = v;
		if(val>-1&&val<9){
			ImageIcon i6 = new ImageIcon("button_bomb_x.gif");           //cell incorrectly flagged image
			BOMB = i6.getImage();
			ImageIcon i7 = new ImageIcon(val_button_names[val]);         //value
			VAL = i7.getImage();
		}
		else if(val==-1){
			ImageIcon i6 = new ImageIcon("button_bomb_pressed.gif");	//cell was a bomb image
			BOMB = i6.getImage();
			ImageIcon i7 = new ImageIcon("button_bomb_blown.gif");		//bomb clicked image
			VAL = i7.getImage();
		}
	}
	
	
	/* CELL STATE CHECKS */
	
	/*
	 * Checks whether a row/col is adjacent to the cell.
	 */
	public boolean isAdjacent(int row, int col){
		if(row<0||row<r-1||row>r+1||row>9){
			return false;
		}
		if(col<0||col<c-1||col>c+1||col>9){
			return false;
		}
		if(row==r&&col==c){
			return false;
		}
		return true;
	}
	
	public boolean isBomb(){
		return val==-1;
	}
	
	public boolean isZero(){
		return val==0;
	}
	
	/* GETTERS */
	
	public int getState(){
		return state;
	}
	
	public Image getImage(){
		return label;
	}
	
	public int getVal(){
		return val;
	}
	/* LABEL CHANGES BASED ON MOUSE CLICKS */
	
	/*
	 * Change label/state based on left mouse press.
	 * If cell is not in normal or question mark state, ignore
	 */
	public void LPressed(){
		if(label==NORM){
			label = NORM_P;
			state = 1;
		}
		else if(label==QUESTION){
			label = QUESTION_P;
			state = 4;
		}
	}
	
	/*
	 * Change label/state based on left mouse release,
	 * assuming that the mouse also pressed onto this cell.
	 * If cell is not in a pressed state, ignore.
	 */
	public void LReleasedSame(){
		if(label==QUESTION_P){
			label = QUESTION;
			state = 3;
		}
		else if(label==NORM_P){
				label = VAL;		//show value
				state = 6;
		}
	}
	
	/*
	 * Change label/state based on left mouse release,
	 * assuming that the mouse did not press this cell.
	 * If cell is not in a pressed state, ignore.
	 */
	public void LReleasedOther(){
		if(label==QUESTION_P){
			label = QUESTION;
			state = 3;
		}
		else if(label==NORM_P){
			label = NORM;			//change back to normal
			state = 0;
		}	
	}
	
	/*
	 * Change label/state based on right mouse press.
	 */
	public void RPressed(){
		if(label == NORM){
			label = FLAG;
			state = 2;
		}
		else if(label == FLAG){
			label = QUESTION;
			state = 3;
		}
		else if(label == QUESTION){
			label = NORM;
			state = 0;
		}
	}
	
	
	/* SPECIAL CASES FOR LABEL CHANGES */
	
	/*
	 * Change label to FLAG. 
	 * This method will only be called by the markBombs method in the 
	 * Grid class which is only called when the user wins the game.
	 */
	public void flagBomb(){
		label = FLAG;
	}
	
	/*
	 * Change label to BOMB, 
	 * which will show an error label if the mine was incorrectly flagged
	 * as a bomb or a bomb label if it was an unmarked bomb. This method 
	 * will only be called by the showBombs method in the Grid class which 
	 * is only called when the user loses.
	 */
	public void showBomb(){
		if(val==-1&&label!=FLAG){
			label = BOMB;
			state = 5;
		}
		else if(val!=-1&&label==FLAG){
			label = BOMB;
			state = 5;
		}
	}
	
	/*
	 * change label to VAL
	 * this method will only be called by the showAdj method in the Grid class
	 * which is only called when showing bombs adjacent to a zero value.
	 */
	public void showVal(){
		label = VAL;
		state = 6;
	}
}
