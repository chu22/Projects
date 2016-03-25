/*
 * 
 * BlockList.java
 * This class enables creation of message blocking/unblocking files, used to reduce the size of
 * numbers operated upon for the RSA algorithm for long messages. Blocks are numeric representations
 * of a certain number of characters in a message, which may or may not have been manipulated with the
 * RSA encryption algorithm. The blocking class assumes messages are written in Troy format, which contains
 * exactly 100 characters. Conversions from ASCII to Troy are built in to the function.
 * 
 * Written by Lawrence Chu
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BlockList {
	
	private ArrayList<String> blocks;  //list of strings, one string representing one block/line of a file
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: BlockList
	 *
	 * Default constructor with an empty arraylist
	 * 
	 * Returns an empty BlockList
	 */
	public BlockList(){
		blocks = new ArrayList<String>();
	}
	
	/*
	 * Constructor: BlockList(File)
	 *
	 * Primary constructor for BlockList, builds a BlockList from a file, assuming
	 * one line is regarded as one block. The constuctor redirects I/O exceptions
	 * from the BufferedReader class as needed.
	 * 
	 * Returns an BlockList loaded from a file
	 */
	public BlockList(File blockName) throws FileNotFoundException, IOException{
		blocks = new ArrayList<String>();
		String line;
    	BufferedReader reader = new BufferedReader(new FileReader(blockName));
        while ((line = reader.readLine()) != null) {
           blocks.add(line);
        }
        reader.close();

	}
	
	/*
	 * Function block(File, File, int)
	 * 
	 * Generates a block file from an input message file and a given block size
	 * Converts the message to Troy character encoding first and pads the last 
	 * line with leading zeroes, which correspond to the null character, to 
	 * maintain the block size.
	 * 
	 * Returns nothing
	 */
	public static void block(File msgName, File blockName, int blockSize) throws FileNotFoundException, IOException{
		FileInputStream inputStream = new FileInputStream(msgName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		PrintWriter writer = new PrintWriter(new FileWriter(blockName));
        int c = 0;
        int ctr = 0;
        String s = new String();
        while((c = reader.read()) != -1) {
        	if (ctr==blockSize){
        		writer.println(s);
        		s = "";
        		ctr = 0;
        	}
        	s = ASCIItoTroy((char)c) + s;
        	ctr++;
        }
        while(s.length()<blockSize*2){
        	s = "00" + s;
        }
        writer.println(s);
        reader.close();
        writer.close();
	}
	
	/* GETTERS */
	
	public ArrayList<String> getBlocks(){
		return blocks;
	}
	
	/* BLOCKING/UNBLOCKING */
	
	/*
	 * Function block(File, int)
	 * 
	 * Generates a BlockList from an input message file and a given block size
	 * Converts the message to Troy character encoding first and pads the last 
	 * line with leading zeroes, which correspond to the null character, to 
	 * maintain the block size. Used by the program to create a encrypted blocked file
	 * file from a message file, skipping over the blocking file creation step
	 * 
	 * Returns a BlockList containing the blocks of the input message
	 */
	public static BlockList block(File msgName, int blockSize) throws FileNotFoundException, IOException{
		FileInputStream inputStream = new FileInputStream(msgName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		BlockList b = new BlockList();
        int c = 0;
        int ctr = 0;
        String s = new String();
        while((c = reader.read()) != -1) {
        	if (ctr==blockSize){
        		b.blocks.add(s);
        		s = "";
        		ctr = 0;
        	}
        	s = ASCIItoTroy((char)c) + s;
        	ctr++;
        }
        b.blocks.add(s);
        reader.close();
        return b;
	}
	
	/*
	 * Function unblock(File, File, int)
	 * 
	 * Generates a text file from an input blocked file and a given block size
	 * Converts the block characters to ASCII character encoding first  and then
	 * writes the message to a specified file
	 * 
	 * Returns nothing
	 */
	public static void unblock(File blockName, File msgName, int blockSize) throws IllegalArgumentException, FileNotFoundException, IOException{
		FileInputStream inputStream = new FileInputStream(blockName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(new FileWriter(msgName));
        int c = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            int n = line.length();
            if(n!=blockSize*2){
            	reader.close();
     	        writer.close();
            	throw new IllegalArgumentException("One or more blocks are of incorrect size");
            }
            for (int i = n/2-1; i >= 0; i--) {
                c = (int)(line.charAt(i*2+1) - '0') + (int)(line.charAt(i*2) - '0')*10; 
                c = TroytoASCII(c);
                if(c!=0){
                	writer.write(c);
                }
            }
        }
        reader.close();
        writer.close();
    }
	
	/*
	 * Function unblock(BlockList, File)
	 * 
	 * Generates a text file from an input BlockList.
	 * Converts the block characters to ASCII character encoding first  and then
	 * writes the message to a specified file
	 * 
	 * Returns nothing
	 */
	public static void unblock(BlockList blockName, File msgName) throws FileNotFoundException, IOException{
        PrintWriter writer = new PrintWriter(new FileWriter(msgName));
        int c = 0;
        ArrayList<String> b = blockName.getBlocks();        
        for(String line : b){
        	int n = line.length();
            for (int i = n/2-1; i >= 0; i--) {
                c = (int)(line.charAt(i*2+1) - '0') + (int)(line.charAt(i*2) - '0')*10; 
                c = TroytoASCII(c);
                if(c!=0){
                	writer.write(c);
                }
            }
        }
        writer.close();
    }
	
	/* CHARACTER CONVERSION */
	
	/*
	 * Function ASCIItoTroy
	 * 
	 * converts an ASCII character to a numeric 2 digit string
	 * that represents the Troy character
	 * 
	 * Returns a 2 character String that is a numeric representation
	 * of the corresponding Troy character encoding
	 */
	public static String ASCIItoTroy(char c){
		 if (c >= 32 && c <= 126){
			 int x = c-27;
			 if(x<10){
				 return ("0" + x);
			 }
			 return String.valueOf(x);
		 }
        if (c == 0) return "00";
        if (c == 11) return "01";
        if (c == 9) return "02";
        if (c == 10) return "03";
        if (c == 13) return "04";
        else return "00";
	}
	
	/*
	 * Function TroytoASCII
	 * 
	 * converts an Troy character to the integer representation of an ASCII character
	 * 
	 * Returns an integer that is the corresponding ASCII character value of the Troy
	 * character.
	 */
	public static int TroytoASCII(int c){
		switch (c) {
        case 0: return 0;
        case 1: return 11;
        case 2: return 9;
        case 3: return 10;
        case 4: return 13;
        default:return (c + 27);
    }
	}
}
