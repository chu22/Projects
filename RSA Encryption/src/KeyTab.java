/*
 * 
 * KeyTab.java
 * This class contains all the GUI components for user input required for generating new keys. It also contains all
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

public class KeyTab extends JPanel{
	
	//Labels used in the panel
	private JLabel heading  = new JLabel("Key Generation");
	private JLabel pLabel = new JLabel("Enter first prime number: ");
	private JLabel qLabel = new JLabel("Enter second prime number: ");
	private JLabel pubLabel = new JLabel("Select public key filepath: ");
	private JLabel priLabel = new JLabel("Select private key filepath: ");
	private JLabel pDeciLabel = new JLabel("Number of digits: ");
	private JLabel qDeciLabel = new JLabel("Number of digits: ");
	
	//Input text boxes used in the panel
	private JTextField pText = new JTextField( 30 );	//user input for first prime
	private JTextField qText  = new JTextField( 30 );	//user input for second prime
	private JTextField priText = new JTextField( 30 );	//user input for private key filename to write to
	private JTextField pubText = new JTextField( 30 );	//user input for public key filename to write to
	private JTextField pDeciText = new JTextField( 5 );	//user input for number of digits in first generated random number
	private JTextField qDeciText = new JTextField( 5 );	//user input for number of digits in second generated random number

	//Check boxes that allows the user to opt for random prime generation 
	private JCheckBox pCheckBox = new JCheckBox("Generate a random prime", false);
	private JCheckBox qCheckBox = new JCheckBox("Generate a random prime", false);
	
	//Buttons used in the panel
	private JButton pubFileChoose = new JButton("Choose File");	//used to allow user to choose a file to write the public key to
	private JButton priFileChoose = new JButton("Choose File");	//used to allow user to choose a file to write the private key to
	private JButton keyGen = new JButton("Generate Keys");		//main button that will generate the keys using the inputs
	
	//Panels used to organize sections within the panel
	private JPanel hedPanel = new JPanel();
	private JPanel pPanel = new JPanel();
	private JPanel prPanel = new JPanel();
	private JPanel qPanel = new JPanel();
	private JPanel qrPanel = new JPanel();
	private JPanel pubPanel = new JPanel();   
	private JPanel priPanel = new JPanel();
	private JPanel butPanel = new JPanel();
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: keyTab
	 *
	 * Adds event listeners to components and components to panels
	 * 
	 * Returns an instance of KeyTab
	 */
	public KeyTab(){
		
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		hedPanel.add(heading);
		
		pPanel.add(pLabel);
		pPanel.add(pText);
		
		
		prPanel.add(pCheckBox);
		prPanel.add(pDeciLabel);
		prPanel.add(pDeciText);
		pDeciText.setEnabled(false);
		pCheckBox.addActionListener(new pRandListener());
		
		qPanel.add(qLabel);
		qPanel.add(qText);
		
		
		qrPanel.add(qCheckBox);
		qrPanel.add(qDeciLabel);
		qrPanel.add(qDeciText);
		qDeciText.setEnabled(false);
		qCheckBox.addActionListener(new qRandListener());
		
		pubPanel.add(pubLabel);
		pubPanel.add(pubText);
		pubPanel.add(pubFileChoose);
		pubFileChoose.addActionListener(new pubFileChooser());
		
		priPanel.add(priLabel);
		priPanel.add(priText);
		priPanel.add(priFileChoose);
		priFileChoose.addActionListener(new priFileChooser());
		
		butPanel.add(keyGen);
		keyGen.addActionListener(new GenerateKey());
		
		add(hedPanel);
		add(pPanel);
		add(prPanel);
		add(qPanel);
		add(qrPanel);
		add(pubPanel);
		add(priPanel);
		add(butPanel);
		
	}
	
	/* EVENT LISTENERS */
	
	/*
	 * Class: GenerateKey
	 *
	 * Event Listener for main key generation button. Reads input fields based on
	 * selection of check boxes and class the proper Key generation function.
	 * It will produce a success pop up message on completion or
	 * it will catch an exception and produce an appropriate error message.
	 * 
	 */
	private class GenerateKey implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int pDec;
			int qDec;
			if(pCheckBox.isSelected()&&qCheckBox.isSelected()){
				try{
					pDec = Integer.parseInt(pDeciText.getText());
					qDec = Integer.parseInt(qDeciText.getText());
					File pubFile = new File(pubText.getText());
					File priFile = new File(priText.getText());
					pubText.setText(pubFile.getAbsolutePath());
					priText.setText(priFile.getAbsolutePath());
					Key.GenKeys(pDec, qDec, pubFile, priFile);
					JOptionPane.showMessageDialog(KeyTab.this, "The keys have been successfully generated!");
				}catch(NumberFormatException exc){
					JOptionPane.showMessageDialog(KeyTab.this, "Invalid number of digits");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Filepath not found");
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Error writing keys to file.");
					e1.printStackTrace();
				}
			}
			else if(pCheckBox.isSelected()){
				try{
					HugeInt q = new HugeInt(qText.getText());
					if(!q.isPrime()){
						JOptionPane.showMessageDialog(KeyTab.this, "The entered number is not prime.");
						return;
					}
					pDec = Integer.parseInt(pDeciText.getText());
					File pubFile = new File(pubText.getText());
					File priFile = new File(priText.getText());
					pubText.setText(pubFile.getAbsolutePath());
					priText.setText(priFile.getAbsolutePath());
					Key.GenKeys(pDec, q, pubFile, priFile);
					JOptionPane.showMessageDialog(KeyTab.this, "The keys have been successfully generated!");
				}catch(NumberFormatException exc){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered number of digits for random prime generation is invalid.");
				}catch(IllegalArgumentException exc){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered prime number is invalid.");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Filepath not found");
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Error writing keys to file.");
					e1.printStackTrace();
				}
			}
			else if(qCheckBox.isSelected()){
				try{
					HugeInt p = new HugeInt(pText.getText());
					if(!p.isPrime()){
						JOptionPane.showMessageDialog(KeyTab.this, "The entered number is not prime.");
						return;
					}
					qDec = Integer.parseInt(qDeciText.getText());
					File pubFile = new File(pubText.getText());
					File priFile = new File(priText.getText());
					pubText.setText(pubFile.getAbsolutePath());
					priText.setText(priFile.getAbsolutePath());
					Key.GenKeys(qDec, p, pubFile, priFile);
					JOptionPane.showMessageDialog(KeyTab.this, "The keys have been successfully generated!");
				}catch(NumberFormatException exc){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered number of digits for random prime generation is invalid.");
				}catch(IllegalArgumentException exc){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered prime number is invalid.");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Filepath not found");
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Error writing keys to file.");
					e1.printStackTrace();
				}
			}
			else{
				try{
					HugeInt p = new HugeInt(pText.getText());
					HugeInt q = new HugeInt(qText.getText());
					if(!p.isPrime()){
						JOptionPane.showMessageDialog(KeyTab.this, "The first entered number is not prime.");
						return;
					}
					if(!q.isPrime()){
						JOptionPane.showMessageDialog(KeyTab.this, "The second entered number is not prime.");
						return;
					}
					File pubFile = new File(pubText.getText());
					File priFile = new File(priText.getText());
					pubText.setText(pubFile.getAbsolutePath());
					priText.setText(priFile.getAbsolutePath());
					Key.GenKeys(p, q, pubFile, priFile);
					JOptionPane.showMessageDialog(KeyTab.this, "The keys have been successfully generated!");
				}catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered number of digits for random prime generation is invalid.");
				}catch(IllegalArgumentException e1){
					JOptionPane.showMessageDialog(KeyTab.this, "The entered prime number is invalid.");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Filepath not found");
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(KeyTab.this, "Error writing keys to file.");
					e1.printStackTrace();
				}
			}
			
		}
		
	}
	
	/*
	 * Class: pubFileChooser
	 *
	 * Event Listener for public key file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the pubText text box
	 * 
	 */
	
	private class pubFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                pubText.setText(f.getAbsolutePath());
            }
		}
		
	}
	
	/*
	 * Class: priFileChooser
	 *
	 * Event Listener for private key file chooser button. Creates a
	 * JFileChooser which will launch and allow the user to select
	 * a file from their system. It will then get the file's path
	 * and print it into the priText text box
	 * 
	 */
	private class priFileChooser implements ActionListener{
		JFileChooser fileChooser = new JFileChooser();
        
		@Override
		public void actionPerformed(ActionEvent e) {
			int ret = fileChooser.showDialog(null, "Choose File");
			if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                priText.setText(f.getAbsolutePath());
            }
		}
		
	}
	
	/*
	 * Class: pRandListener
	 *
	 * Event Listener for the p check box, disabling the unnecessary
	 * text fields based on whether the user wants random generation or
	 * prime input
	 * 
	 */
	private class pRandListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(pCheckBox.isSelected()){
				pDeciText.setEnabled(true);
				pText.setEnabled(false);
			}
			else{
				pDeciText.setEnabled(false);
				pText.setEnabled(true);
			}
			
		}
		
	}
	
	/*
	 * Class: qRandListener
	 *
	 * Event Listener for the q check box, disabling the unnecessary
	 * text fields based on whether the user wants random generation or
	 * prime input
	 * 
	 */
	private class qRandListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(qCheckBox.isSelected()){
				qDeciText.setEnabled(true);
				qText.setEnabled(false);
			}
			else{
				qDeciText.setEnabled(false);
				qText.setEnabled(true);
			}
			
		}
		
	}
	
	
}
