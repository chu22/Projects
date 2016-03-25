/*
 * 
 * EncryptDecryptTab.java
 * This class contains all the GUI components for user input required for Encryption/Deryption. It also contains all
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
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class EncryptDecryptTab extends JPanel{

	//Labels used in the panel
	private JLabel heading  = new JLabel("Encrypt/Decrypt");
	private JLabel keyLabel = new JLabel("Enter key filepath: ");	
	private JLabel inpLabel = new JLabel("Enter input filepath: ");	
	private JLabel outLabel = new JLabel("Enter output filepath: ");
	private JLabel sizeLabel = new JLabel("Enter block size: ");
	
	//Input text boxes used in the panel
	private JTextField keyText = new JTextField( 30 );		//used to get key filename for the RSA algorithm
	private JTextField inpText = new JTextField( 30 );		//used to get filename for program to read from
	private JTextField outText  = new JTextField( 30 );		//used to get filename for program to write to
	private JTextField sizeText = new JTextField( 5 );		//used to get block size
	
	//Check boxes that allows the user to opt out of blocked file creation
	private JCheckBox skipCheckBox = new JCheckBox("Skip blocking", false);
	
	//Buttons used in the panel
	private JButton keyFileChoose = new JButton("Choose File");		//used to allow user to choose a key file to read from
	private JButton inpFileChoose = new JButton("Choose File");		//used to allow user to choose a file to read from
	private JButton outFileChoose = new JButton("Choose File");		//user input for filename for program to write to
	private JButton Encrypt = new JButton("Encrypt");				//main button that will generate the encrypted file using the inputs
	private JButton Decrypt = new JButton("Decrypt");				//main button that will generate the blocked/message file using the inputs
	  
	//Panels used to organize sections within the panel
	private JPanel hedPanel = new JPanel();
	private JPanel keyPanel = new JPanel();
	private JPanel inpPanel = new JPanel(); 
	private JPanel outPanel = new JPanel(); 
	private JPanel sizePanel = new JPanel();  
	private JPanel butPanel = new JPanel();
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: EncryptDecryptTab
	 *
	 * Adds event listeners to components and components to panels
	 * 
	 * Returns an instance of EncryptDecryptTab
	 */
	public EncryptDecryptTab(){
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		hedPanel.add(heading);
		
		keyPanel.add(keyLabel);
		keyPanel.add(keyText);
		keyPanel.add(keyFileChoose);
		keyFileChoose.addActionListener(new keyFileChooser());
		
		inpPanel.add(inpLabel);
		inpPanel.add(inpText);
		inpPanel.add(inpFileChoose);
		inpFileChoose.addActionListener(new inpFileChooser());
		
		outPanel.add(outLabel);
		outPanel.add(outText);
		outPanel.add(outFileChoose);
		outFileChoose.addActionListener(new outFileChooser());
		
		sizePanel.add(sizeLabel);
		sizePanel.add(sizeText);

		butPanel.add(skipCheckBox);
		butPanel.add(Encrypt);
		butPanel.add(Decrypt);
		Encrypt.addActionListener(new RunEncrypt());
		Decrypt.addActionListener(new RunDecrypt());
		
		add(hedPanel);
		add(keyPanel);
		add(inpPanel);
		add(outPanel);
		add(sizePanel);
		add(butPanel);
	}
	
	/*
	 * Class: RunEncrypt
	 *
	 * Event Listener for main encryption function button. Reads input fields and the check box, which
	 * determines whether the input file should be treated as a message or a blocked file, and which
	 * methods to call. It will produce a success pop up message on completion or
	 * it will catch an exception and produce an appropriate error message.
	 * 
	 */
	private class RunEncrypt implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			File keyFile = new File(keyText.getText());
			File inpFile = new File(inpText.getText());
			File outFile = new File(outText.getText());
			keyText.setText(keyFile.getAbsolutePath());
			inpText.setText(inpFile.getAbsolutePath());
			outText.setText(outFile.getAbsolutePath());
			try {
				if(!skipCheckBox.isSelected()){
					EncryptDecrypt.Encrypt(keyFile, inpFile, outFile);
				}
				else{
					int blockSize = Integer.parseInt(sizeText.getText());
					EncryptDecrypt.Encrypt(keyFile, BlockList.block(inpFile, blockSize), outFile);
				}
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Message encrypted");
			} catch (NumberFormatException e1){
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid block size input");
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "File path not found");
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid input file contents");
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid key file contents. Ensure you are using a public key for encryption or a private key for decryption.");
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Key values are insufficient for the current message block size");
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error reading/writing files");
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error parsing input file");
				e1.printStackTrace();
			} catch (SAXException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error reading key file");
				e1.printStackTrace();
			}
			
		}
		
	}
	
	/*
	 * Class: RunDecrypt
	 *
	 * Event Listener for main decryption function button. Reads input fields and the check box, which
	 * determines whether the input file should be treated as an encrypted file or a blocked file, and which
	 * methods to call. It will produce a success pop up message on completion or
	 * it will catch an exception and produce an appropriate error message.
	 * 
	 */
	private class RunDecrypt implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			File keyFile = new File(keyText.getText());
			File inpFile = new File(inpText.getText());
			File outFile = new File(outText.getText());
			keyText.setText(keyFile.getAbsolutePath());
			inpText.setText(inpFile.getAbsolutePath());
			outText.setText(outFile.getAbsolutePath());
			try {
				if(!skipCheckBox.isSelected()){
					int blockSize = Integer.parseInt(sizeText.getText());
					EncryptDecrypt.Decrypt(keyFile, inpFile, outFile, blockSize);
				}
				else{
					BlockList.unblock(EncryptDecrypt.Decrypt(keyFile, inpFile), outFile);
				}
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Message decrypted");
			} catch (NumberFormatException e1){
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid block size input");
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "File path not found");
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid input file contents");
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Invalid key file contents. Ensure you are using a public key for encryption or a private key for decryption.");
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Key values are insufficient for the current message block size");
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error reading/writing files");
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error parsing input file");
				e1.printStackTrace();
			} catch (SAXException e1) {
				JOptionPane.showMessageDialog(EncryptDecryptTab.this, "Error reading key file");
				e1.printStackTrace();
			}
			
		}
		
	}
	
	/*
	 * Class: keyFileChooser
	 *
	 * Event Listener for key file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the keyText text box.
	 * 
	 */
	private class keyFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                keyText.setText(f.getAbsolutePath());
            }
		}
	
	}
	
	/*
	 * Class: inpFileChooser
	 *
	 * Event Listener for input file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the inpText text box.
	 * 
	 */
	private class inpFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                inpText.setText(f.getAbsolutePath());
            }
		}
		
	}
	
	/*
	 * Class: outFileChooser
	 *
	 * Event Listener for input file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the outText text box.
	 * 
	 */
	private class outFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                outText.setText(f.getAbsolutePath());
            }
		}
		
	}
}
