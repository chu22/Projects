/* 
 * TimePanel.java 
 * 
 * This class is the panel that shows time elapsed in the game
 * The maximum time is 999
 * 
*/

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class TimePanel extends JPanel{
	/* Digit images */
	private Image hund;
	private Image tens;
	private Image ones;
	
	/* Digit numbers */
	private int dh;	//hundreds
	private int dt;	//tens
	private int d1;	//ones
	
	/* Initialize to zero */
	public TimePanel(){
		hund = Digit.d[0];
		tens = Digit.d[0];
		ones = Digit.d[0];
		dh = 0;
		dt = 0;
		d1 = 0;	
	}
	
	public void incTime(){
		if(d1==9){				//1s place is 9
			if(dt==9){			//10s place is 9
				if(dh==9){		//100s place is 9
					return;		//do nothing
				}
				d1 = 0;			//else set to X00
				dt = 0;
				dh++;
				setTime(dh,dt,d1);
				return;
			}					
			d1 = 0;				//else set to XY0
			dt++;
			setTime(dh,dt,d1);
			return;
		}
		d1++;					//else set to XYZ
		setTime(dh,dt,d1);
	}
	
	private void setTime(int h, int t, int o){
		hund = Digit.d[h];
		tens = Digit.d[t];
		ones = Digit.d[o];
	}
	
	public int getTime(){
		return 100*dh+10*dt+d1;
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(hund, 0, 0, null);
		g.drawImage(tens, Digit.width, 0, null);
		g.drawImage(ones, 2*Digit.width, 0, null);
	}
}
