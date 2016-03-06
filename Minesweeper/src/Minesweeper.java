/* 
 * Minesweeper.java 
 *
 * The main minesweeper class/frame
 * This class contains all event handlers and controls communications between the separate panels and menu options.
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Minesweeper extends JFrame {
	/* Class instances */
	private Grid g;				//main game grid
	private TimePanel t;		//timer display panel
	private FlagPanel f;		//number of flags display panel
	private ResetPanel r;		//reset button
	private Menu m;				//menus
	private HighScores h;		//high score holder
	
	private Timer time;
	private boolean scoreRecorded;
	private boolean firstClick;	//check if first click of the game, used in conjunction with Grid class firstClick variable to ensure 
								//the timer/grid is not set until after the first valid click. 
	
	/* High score display elements */
	JTable scoresTable;	
	JScrollPane scoresPane;
	
	/* Frame dimensions */
	private final int FRAME_WIDTH = 170;
	private final int FRAME_HEIGHT = 250;
	
	/* Timer delay */
	private final int DELAY = 1000;
	
	 /* Game Instance */
	public Minesweeper(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Minesweeper");
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
		/* Initiate class instances */
		g = new Grid();
		t = new TimePanel();
		f = new FlagPanel();
		r = new ResetPanel();
		m = new Menu();
		h = new HighScores();
		time = new Timer(DELAY, new TimerHandler());
		scoresPane= new  JScrollPane();
		scoresTable = h.getTable();
		
		firstClick = true;
		scoreRecorded = false;
		
		/* Add Event Handlers */
		g.addMouseListener(new gridHandler());
		r.addMouseListener(new ResetHandler());
		m.getItem(0).addActionListener(new resetMenuHandler());
		m.getItem(1).addActionListener(new scoresMenuHandler());
		m.getItem(2).addActionListener(new exitMenuHandler());
		m.getItem(3).addActionListener(new helpMenuHandler());
		m.getItem(4).addActionListener(new aboutMenuHandler());
		m.getItem(5).addActionListener(new resetScoresMenuHandler());
		
		/* Add class instances */
		add(f);
		add(g);
		add(t);
		add(r);
		setJMenuBar(m);
		
		/* Set dimensions for panels and grid */
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,ResetPanel.height + 5,Grid.width,Grid.height);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
		r.setBounds(FRAME_WIDTH/2-ResetPanel.width,0,ResetPanel.width,ResetPanel.height);
		scoresPane.setPreferredSize(new Dimension(300, 220));
	}
	
	/* Main */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Minesweeper window = new Minesweeper();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void reset(){
		remove(f);
		remove(g);
		remove(t);
		
		time.stop();
		
		g = new Grid();
		t = new TimePanel();
		f = new FlagPanel();
		
		firstClick = true;
		scoreRecorded = false;
		
		g.addMouseListener(new gridHandler());
		
		add(f);
		add(g);
		add(t);
		
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,ResetPanel.height + 5,Grid.width,Grid.height);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
	}
	
	 /* EVENT HANDLERS */
	
	private class gridHandler implements MouseListener{
		private static final int BOTH = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;					
		private boolean bothDown = false;								//boolean to check if both mouse buttons are down
		
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			bothDown = (e.getModifiersEx() & BOTH) == BOTH;	//get modifiers for event, which is a mask of which buttons are pressed, and compare with BOTH
			if(!bothDown){						
				g.mousePressed(e);							// Determine and manage program based on which button was clicked
				f.setCount(g.getnFlags());					//Retrieves number of flags and updates the flag panel
				g.repaint();
				f.repaint();
				if(!g.gameOver()){							//If game is not over, change :) into :O on reset button
					r.gridPressed();
					r.repaint();
				}
			}
			else{
				g.doubleMousePressed(e);
				g.repaint();
			}	
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			g.mouseReleased(e);				 	
			if(firstClick&&!g.firstClicked()){ 	// If this is the first click of the game and both press/release occurred on the same cell,
				time.start();					// start the timer
				firstClick = false;
			}
			if(bothDown&&(e.getModifiersEx() & BOTH)==0){									//both buttons were down, and now both are released			
				g.doubleMouseReleased(e);
			}
			g.repaint();
			if(g.gameOver()){
				time.stop();					// freeze timer
				f.setCount(g.getnFlags());
				f.repaint();
				if(g.gameWon()){
					r.gameWon();															//set reset button to B-)
					r.repaint();
					Score s = new Score(t.getTime());										//create new score based on time
					if(h.isTopTen(s)&&!scoreRecorded){														//if a top ten score
						String name = (String)JOptionPane.showInputDialog(					//get user name
			                    Minesweeper.this,
			                    "Congratualations! You have a new high score!\n"
			                    + "Enter your name:",
			                    "New High Score", JOptionPane.PLAIN_MESSAGE, null, null, null);
						if(name!=null){														//if user clicked OK button
							s.addName(name);												//add name to score
							h.addScore(s);													//add score to high scores
						}
						scoreRecorded = true;
					}
				}
				else{
					r.gameLost();						//set reset button to X_X
				}
			}
			else if((e.getModifiersEx() & BOTH)==0){	//neither mouse button is pressed			/
				r.mouseReleased();						//change reset button to :)
			}
			r.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
		
	private class ResetHandler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1)
		    {
				r.resetPressed();
				r.repaint();	
		    }
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1)
		    {
				int x = e.getX();
				int y = e.getY();
				if(x>=0&&x<=ResetPanel.width&&y>=0&&y<=ResetPanel.height){
					reset();
				}
				if(!g.gameOver()){
					r.mouseReleased();
					r.repaint();
				}
				else{
					if(g.gameWon()){
						r.gameWon();
					}
					else{
						r.gameLost();
					}
					r.repaint();
				}
		    }
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	private class TimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			t.incTime();
			t.repaint();
		}	
	}
	
	private class resetMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			r.mouseReleased();
			reset();
		}
	}
	
	private class scoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			scoresPane.getViewport().remove( scoresTable );		//remove old table and add new table to high scores scrollpane
			scoresTable = h.getTable();
			scoresPane.getViewport().add( scoresTable );
			JOptionPane.showMessageDialog(Minesweeper.this,
				    scoresPane,
				    "High Scores",
				    JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private class resetScoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			h.resetScoreFile();
		}
	}
	
	private class exitMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	 /* 
	  * Helper function that contains text for helpMenuHandler ActionListener 
	  */
	 public String printHelp(){
		   String str = "MINESWEEPER: HOW TO PLAY\n\n" +
		     "The Objective:\n" +
		     "    - The goal of the game is to reveal all empty squares while avoiding the mines.\n" +
		     "    - The faster you are able to do so, the better your score will be.\n\n" +
		     
		     "The Board:\n" +
		     "    - The number on the top left indicates the number valid markers available.\n" +
		     "    - The Smiley Face is a 'New Game' button.\n" +
		     "    - The number on the top right indicates the amount of time that has passed.\n" +
		     "    - There are 100 tiles and 10 mines in total.\n\n" +
		     
		     "Controls:\n" +
		     "    - Left clicking on a tile will reveal either a number(s) or a mine.\n" +
		     "         - If a mine is uncovered, you lose and the game is over.\n" +
		     "         - If a number is revealed, it indicates the number of mines there are in the surrounding 8 tiles.\n" +
		     "    - Right clicking on a blank, unrevealed tile will mark the mine with a flag. This should be done when\n" +
		     "      you suspect a mine is under the tile.\n" + 
		     "         - If you are unsure, right clicking a marked tile will turn it into a question mark.\n" +
		     "         - Note that there can be as many flags on the board as there are tiles, but the game will only finish\n" +
		     "           if there are 10 or less flags on the board (having more than 10 means you marked a tile with no mine).\n" +
		     "     - Left clicking the Smiley Face will start a new game.\n";
		   return str;
		 }

	private class helpMenuHandler implements ActionListener{

		@Override
		  public void actionPerformed(ActionEvent e) {
		   String p = printHelp();
		   JOptionPane.showMessageDialog(Minesweeper.this,
		        p,
		        "Help",
		        JOptionPane.PLAIN_MESSAGE);
		  }
	}
	
	private class aboutMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Minesweeper.this,
					 "MINESWEEPER [U. of Illinois at Chicago: CS342 - Project2]\n" +
						        "Version 1.0 (2/21/2016)\n" +
						        "Developed by :\n    Lawrence Chu [chu22@uic.edu]\n    Kevin Tang [ktang20@uic.edu]",
						        "About",
						        JOptionPane.PLAIN_MESSAGE);
		
		}
	}
}
