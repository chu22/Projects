/*
 * 
 * RSABuilder.java
 * This class is the container for each of the four tabs used in the encryption/decryption program.
 * It holds the main function required to start the executable.
 * 
 * Written by Lawrence Chu
 * 
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

public class RSABuilder extends JFrame{
	
//	private JMenuBar Menu = new JMenuBar();							//UNIMPLEMENTED MENU OPTIONS
//	private JMenu File = new JMenu("File");
//	private JMenu About = new JMenu("About");
//	private JMenuItem aAbout = new JMenuItem("About");
//	private JMenuItem aDeveloper = new JMenuItem("Developer Notes");
//	private JMenuItem fHelp = new JMenuItem("Help");
//	private JMenuItem fExit = new JMenuItem("Exit");
	
	private JTabbedPane options;	//container for individual tabs
	private KeyTab kt;				//keytab instance
	private BlockTab bt;			//blocktab instance
	private UnblockTab ut;			//unblocktab instance
	private EncryptDecryptTab et;	//encryptdecrypttab instance
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: RSABuilder
	 *
	 * Initializes the separate tabs and adds them to the frame.
	 * 
	 * Returns an instance of RSABuiler
	 */
	
	public RSABuilder(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("RSA Message Builder");
        setSize(500,500);
		setLocationRelativeTo(null);
		
		options = new JTabbedPane();
		kt = new KeyTab();
		bt = new BlockTab();
		ut = new UnblockTab();
		et = new EncryptDecryptTab();
		options.addTab("Generate Key", kt);
		options.addTab("Block Message", bt);
		options.addTab("Unblock Message", ut);
		options.addTab("Encrypt/Decrypt", et);
		
		add(options);
	}
	
	/*
	 * Function: Main
	 *
	 * Begins program execution with event queue to process events
	 * 
	 * Returns nothing
	 */
	public static void main(String args[]){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RSABuilder rsa = new RSABuilder();
					rsa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
