/* 
 * Menu.java 
 *
 * This class is a repository for the menu items for the game
 * 
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar{

	private JMenu Help;
	private JMenu Game;
	private JMenuItem[] items; //{reset,scores,exit,help,about}
	
	public Menu(){
		Game = new JMenu("Game");
		Game.setMnemonic(KeyEvent.VK_G);
		Help = new JMenu("Help");
		Help.setMnemonic(KeyEvent.VK_L);
		add(Game);
		add(Help);
		items = new JMenuItem[6];
		items[0] = new JMenuItem("Reset", KeyEvent.VK_R);
		Game.add(items[0]);
		items[1] = new JMenuItem("Top Ten", KeyEvent.VK_T);
		Game.add(items[1]);
		items[5] = new JMenuItem("Reset Top Ten", KeyEvent.VK_E);
		Game.add(items[5]);
		items[2] = new JMenuItem("Exit", KeyEvent.VK_X);
		Game.add(items[2]);
		items[3] = new JMenuItem("Help", KeyEvent.VK_H);
		Help.add(items[3]);
		items[4] = new JMenuItem("About", KeyEvent.VK_A);
		Help.add(items[4]);
	}
	
	public JMenuItem getItem(int i){
		return items[i];
	}	
}
