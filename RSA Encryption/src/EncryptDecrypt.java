/*
 * 
 * EncryptDecrypt.java
 * This class executes the RSA algorithm to encrypt/decrypt message files.
 * 
 * Written by Lawrence Chu
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class EncryptDecrypt {
	
	/*
	 * Function Encrypt(File, File, File)
	 * 
	 * Generates an encrypted file given a blocked file input and a key file.
	 * 
	 * Returns nothing
	 */
	public static void Encrypt(File keyName, File blockName, File fname) 
			throws FileNotFoundException, IOException, IllegalArgumentException, NullPointerException, IllegalStateException, ParserConfigurationException, SAXException {
		Key k = new Key(keyName, true);
		BlockList b = new BlockList(blockName);
		PrintWriter writer = new PrintWriter(new FileWriter(fname));
		ArrayList<String> bl = b.getBlocks();
		HugeInt value;
		for(String s : bl){
			value = new HugeInt(s);
			if(k.getN().compareTo(value)<0){
				writer.close();
        		throw new IllegalStateException("Key value insufficient for current message block size");
        	}
			value = HugeInt.modPow(value, k.getExp(), k.getN());
			writer.println(value);
		}
		writer.close();
	}
	
	/*
	 * Function Encrypt(File, BlockList, File)
	 * 
	 * Generates an encrypted file given a BlockList input and a key file.
	 * This method is used in conjunction with the block(File,int) method to
	 * directly bypass need for blocked file creation, building an encrypted
	 * file directly from a text message file.
	 * 
	 * Returns nothing
	 */
	public static void Encrypt(File keyName, BlockList blockName, File fname) 
			throws FileNotFoundException, IOException, IllegalArgumentException, NullPointerException, IllegalStateException, ParserConfigurationException, SAXException {
		Key k = new Key(keyName, true);
		PrintWriter writer = new PrintWriter(new FileWriter(fname));
		ArrayList<String> bl = blockName.getBlocks();
		HugeInt value;
		for(String s : bl){
			value = new HugeInt(s);
			if(k.getN().compareTo(value)<0){
				writer.close();
        		throw new IllegalStateException("Key value insufficient for current message block size");
        	}
			value = HugeInt.modPow(value, k.getExp(), k.getN());
			writer.println(value);
		}
		writer.close();
	}
	
	/*
	 * Function Decrypt(File, File, File, int)
	 * 
	 * Generates a blocked file given an encrypted file input, a block size, and a key file.
	 * 
	 * Returns nothing
	 */
	public static void Decrypt(File keyName, File blockName, File fname, int blockSize) 
			throws FileNotFoundException, IOException, IllegalArgumentException, NullPointerException, IllegalStateException, ParserConfigurationException, SAXException {
		Key k = new Key(keyName, false);
		BlockList b = new BlockList(blockName);
		PrintWriter writer = new PrintWriter(new FileWriter(fname));
		ArrayList<String> bl = b.getBlocks();
		HugeInt value;
		for(String s : bl){
			value = new HugeInt(s);
			if(k.getN().compareTo(value)<0){
				writer.close();
        		throw new IllegalStateException("Key value insufficient for current message block size");
        	}
			value = HugeInt.modPow(value, k.getExp(), k.getN());
			String t = value.toString();
			while(t.length()<blockSize*2){
				t = "0" + t;
			}
			writer.println(t);
		}
		writer.close();
	}
	
	/*
	 * Function Decrypt(File, File, File, int)
	 * 
	 * Generates a BlockList given an encrypted file input and a key file.
	 * This method is used in conjunction with the unblock(BlockList, File)
	 * to skip over the blocked file creation step, directly converting an
	 * encrypted file to a message file. 
	 * 
	 * Returns a BlockList with string representations of the unencrypted blocked file.
	 */
	public static BlockList Decrypt(File keyName, File blockName) 
			throws FileNotFoundException, IOException, IllegalArgumentException, NullPointerException, IllegalStateException, ParserConfigurationException, SAXException {
		Key k = new Key(keyName, false);
		BlockList b = new BlockList(blockName);
		BlockList ret = new BlockList();
		ArrayList<String> bl = b.getBlocks();
		ArrayList<String> r = ret.getBlocks();
		HugeInt value;
		for(String s : bl){
			value = new HugeInt(s);
			if(k.getN().compareTo(value)<0){
        		throw new IllegalStateException("Key value insufficient for current message block size");
        	}
			value = HugeInt.modPow(value, k.getExp(), k.getN());
			s = value.toString();
			if(s.length()%2!=0){
				s = "0" + s;
			}
			r.add(s);
		}
		return ret;
	}
}
