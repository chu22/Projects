/*
 * 
 * UnblockTab.java
 * This class contains all the GUI components for user input required for unblocking. It also contains all
 * event listeners required for the components.
 * 
 * Written by Lawrence Chu
 * 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UnblockTab extends JPanel{
	
	//Labels used in the panel
	private JLabel heading  = new JLabel("Unblocking");
	private JLabel msgLabel = new JLabel("Enter output message filepath: ");
	private JLabel unblkLabel = new JLabel("Enter input block filepath: ");
	private JLabel sizeLabel = new JLabel("Enter block size: ");
	
	//Input text boxes used in the panel
	private JTextField msgText = new JTextField( 30 );		//user input for filename for program to write to
	private JTextField unblkText  = new JTextField( 30 );	//user input for filename for program to read from
	private JTextField sizeText = new JTextField( 5 );		//user input for block size
	
	//Buttons used in the panel
	private JButton msgFileChoose = new JButton("Choose File");		//used to allow user to choose a file to write to
	private JButton unblkFileChoose = new JButton("Choose File");	//used to allow user to choose a file to read from
	private JButton CreateMessage = new JButton("Unblock Message");	//main button that will generate the message file using the inputs
	  
	//Panels used to organize sections within the panel
	private JPanel hedPanel = new JPanel();
	private JPanel msgPanel = new JPanel();
	private JPanel unblkPanel = new JPanel();
	private JPanel sizePanel = new JPanel();   
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: UnblockTab
	 *
	 * Adds event listeners to components and components to panels
	 * 
	 * Returns an instance of UnblockTab
	 */
	public UnblockTab(){
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		hedPanel.add(heading);
		
		msgPanel.add(msgLabel);
		msgPanel.add(msgText);
		msgPanel.add(msgFileChoose);
		msgFileChoose.addActionListener(new msgFileChooser());
		
		unblkPanel.add(unblkLabel);
		unblkPanel.add(unblkText);
		unblkPanel.add(unblkFileChoose);
		unblkFileChoose.addActionListener(new unblkFileChooser());
		
		sizePanel.add(sizeLabel);
		sizePanel.add(sizeText);
		sizePanel.add(CreateMessage);
		CreateMessage.addActionListener(new UnblockFile());
		
		add(hedPanel);
		add(unblkPanel);
		add(msgPanel);
		add(sizePanel);
		
	}
	
	
	/* EVENT LISTENERS */
	
	/*
	 * Class: UnblockFile
	 *
	 * Event Listener for main unblocking function button. Reads input fields and calls
	 * the unblock method. It will produce a success pop up message on completion or
	 * it will catch an exception and produce an appropriate error message.
	 * 
	 */
	private class UnblockFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				int blockSize = Integer.parseInt(sizeText.getText());
				File msgFile = new File(msgText.getText());
				File unblkFile = new File(unblkText.getText());
				msgText.setText(msgFile.getAbsolutePath());
				unblkText.setText(unblkFile.getAbsolutePath());
				BlockList.unblock(unblkFile, msgFile, blockSize);
				JOptionPane.showMessageDialog(UnblockTab.this, "Message successfully unblocked");
			}catch(NumberFormatException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(UnblockTab.this, "Invalid block size input");
			}catch(IllegalArgumentException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(UnblockTab.this, "A line in the blocked file does not match the blocksize");
			}catch(FileNotFoundException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(UnblockTab.this, "File path not found");
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(UnblockTab.this, "Error in reading/writing files");
			}
		}
		
	}
	
	/*
	 * Class: msgFileChooser
	 *
	 * Event Listener for message file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the output msgText text box
	 * 
	 */
	private class msgFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                msgText.setText(f.getAbsolutePath());
            }
		}
		
	}
	
	/*
	 * Class: unblkFileChooser
	 *
	 * Event Listener for blocked file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the unblkText text box.
	 * 
	 */
	private class unblkFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                unblkText.setText(f.getAbsolutePath());
            }
		}
		
	}
}
