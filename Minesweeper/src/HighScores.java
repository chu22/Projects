/* 
 * HighScores.java 
 *
 * This class maintains the list of high scores, using Score objects
 * It will create, read, and write high scores to a file
 * It also creates the JTable used to output high scores
 * 
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HighScores {
	
	private ArrayList<Score> scores;		//top scores list
	private JTable scoreTable;				//JTable element to display scores
	
	private final int MAX = 10;				//number of scores to hold
	private final String[] COL_NAMES = 		//JTable column names
		{"  ","Name", "Score"};
	
	private static final String SCORE_FILE = "scores.dat";	//name of file high scores are saved to
	 
	ObjectOutputStream out = null;			//used to write Scores to file
	ObjectInputStream in = null;			//used to read Scores from file
	
	/* CONSTRUCTOR/INITIALIZATION FUNCTIONS */
	
	public HighScores(){
		scores = new ArrayList<Score>(MAX);
		scoreTable = new JTable();
		scoreTable.setEnabled(false);
		scoreTable.getTableHeader().setReorderingAllowed(false);
		loadScoreFile();
		updateTable();
	}
	
	/* FUNCTIONS TO CHANGE HIGH SCORES LIST */
	
	public boolean isTopTen(Score s){
		return scores.size()<MAX||s.lessThan(scores.get(scores.size()-1));
	}
	
	public void addScore(Score s){	
		loadScoreFile();
		if(scores.size()<MAX){
			scores.add(s);
		}
		else{
			scores.remove(scores.size()-1);
			scores.add(s);
		}
		Collections.sort(scores);
		updateScoreFile();
	}
	
	/* FILE MANIPULATION FUNCTIONS */
	
	private void loadScoreFile() {
        try {
            in = new ObjectInputStream(new FileInputStream(SCORE_FILE));
            scores = (ArrayList<Score>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("CNF Error: " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            }
        }
	}
	
	private void updateScoreFile() {
        try {
            out = new ObjectOutputStream(new FileOutputStream(SCORE_FILE));
            out.writeObject(scores);
            updateTable();
        } catch (FileNotFoundException e) {
            System.out.println("FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
	}
	
	public void resetScoreFile(){
		scores = new ArrayList<Score>(MAX);
		updateScoreFile();
	}
	
	/* JTABLE FUNCTIONS/GETTER */
	
	private void updateTable(){
		String[][] scoreGrid = new String[10][3];
		int ctr = 0;
		for(Score s : scores){
			scoreGrid[ctr][0] = ctr+1 + ".";
			scoreGrid[ctr][1] = s.getName();
			scoreGrid[ctr][2] = s.getScore() + "";
			ctr++;
		}
		while(ctr<10){
			scoreGrid[ctr][0] = ctr+1 + ".";
			scoreGrid[ctr][1] = "";
			scoreGrid[ctr][2] = "";
			ctr++;
		}
		scoreTable.setModel(new DefaultTableModel(scoreGrid, COL_NAMES));
		scoreTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		scoreTable.getColumnModel().getColumn(1).setPreferredWidth(235);
		scoreTable.getColumnModel().getColumn(2).setPreferredWidth(40);
	}
	
	public JTable getTable(){
		return scoreTable;
	}	
}
