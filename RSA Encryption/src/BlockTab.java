/*
 * 
 * BlockTab.java
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
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BlockTab extends JPanel{
	
	//Labels used in the panel
	private JLabel heading  = new JLabel("Blocking");
	private JLabel msgLabel = new JLabel("Enter input message filepath: ");
	private JLabel blkLabel = new JLabel("Enter output block filepath: ");
	private JLabel sizeLabel = new JLabel("Enter block size: ");
	
	//Input text boxes used in the panel
	private JTextField msgText = new JTextField( 30 );	//user input for filename for program to read from
	private JTextField blkText  = new JTextField( 30 );	//user input for filename for program to write to
	private JTextField sizeText = new JTextField( 5 );	//user input for block size
	
	//Buttons used in the panel
	private JButton msgFileChoose = new JButton("Choose File");	//used to allow user to choose a file to read from
	private JButton blkFileChoose = new JButton("Choose File");	//used to allow user to choose a file to write to
	private JButton CreateBlock = new JButton("Block Message");	//main button that will generate the blocked file using the inputs
	
	//Panels used to organize sections within the panel
	private JPanel hedPanel = new JPanel();
	private JPanel msgPanel = new JPanel();
	private JPanel blkPanel = new JPanel();
	private JPanel sizePanel = new JPanel();   
	private JPanel butPanel = new JPanel();
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: blockTab
	 *
	 * Adds event listeners to components and components to panels
	 * 
	 * Returns an instance of BlockTab
	 */
	public BlockTab(){
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		hedPanel.add(heading);
		
		msgPanel.add(msgLabel);
		msgPanel.add(msgText);
		msgPanel.add(msgFileChoose);
		msgFileChoose.addActionListener(new msgFileChooser());
		
		blkPanel.add(blkLabel);
		blkPanel.add(blkText);
		blkPanel.add(blkFileChoose);
		blkFileChoose.addActionListener(new blkFileChooser());
		
		sizePanel.add(sizeLabel);
		sizePanel.add(sizeText);
		sizePanel.add(CreateBlock);
		CreateBlock.addActionListener(new BlockFile());
		
		add(hedPanel);
		add(msgPanel);
		add(blkPanel);
		add(sizePanel);
		add(butPanel);
	}
	
	/* EVENT LISTENERS */
	
	/*
	 * Class: BlockFile
	 *
	 * Event Listener for main blocking function button. Reads input fields and calls
	 * the block method. It will produce a success pop up message on completion or
	 * it will catch an exception and produce an appropriate error message.
	 * 
	 */
	private class BlockFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				int blockSize = Integer.parseInt(sizeText.getText());
				File msgFile = new File(msgText.getText());
				File blkFile = new File(blkText.getText());
				msgText.setText(msgFile.getAbsolutePath());
				blkText.setText(blkFile.getAbsolutePath());
				BlockList.block(msgFile, blkFile, blockSize);
				JOptionPane.showMessageDialog(BlockTab.this, "Message successfully blocked");
			}catch(NumberFormatException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(BlockTab.this, "Invalid block size input");
			}catch(FileNotFoundException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(BlockTab.this, "File path not found");
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(BlockTab.this, "Error in reading/writing files");
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
	 * Class: blkFileChooser
	 *
	 * Event Listener for blocked file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the blkText text box.
	 * 
	 */
	private class blkFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                blkText.setText(f.getAbsolutePath());
            }
		}
		
	}
}
