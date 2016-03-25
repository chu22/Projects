/*
 * 
 * Key.java
 * This class manages the generation and storage of the public and private keys required in the 
 * RSA algorithm. 
 * 
 * Written by Lawrence Chu
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Key {
	
	private HugeInt exp;	//the e or d value for the public/private key
	private HugeInt n;		//the n value for the key
	private boolean pub;	//boolean representing whether the key is public(true) or private(false)
	//private static final String primeFile = "primeList.rsc";  //potential resource file used to store primes for prime generation/primality testing - currently not implemented
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: Key
	 *
	 * Constructor/File Reader for Key class. Loads a key from a properly formatted
	 * key file in XML format. Throws multiple exceptions for file reading errors as well 
	 * as redirecting a NullPointerException when the expected XML tags are missing.
	 * 
	 * Returns a Key with values loaded from a properly formatted XML file
	 */
	public Key(File keyName, boolean pubk) throws NullPointerException, ParserConfigurationException, SAXException, IOException{
		String expVal = new String();
        String nVal = new String();
        DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document xmlDoc = docReader.parse(keyName);
        Element rootElement = xmlDoc.getDocumentElement();
        if(pubk){
        	NodeList nodeE = rootElement.getElementsByTagName("evalue");
        	expVal = nodeE.item(0).getTextContent();
        }
        else{
        	NodeList nodeD = rootElement.getElementsByTagName("dvalue");
        	expVal = nodeD.item(0).getTextContent();
        }
        NodeList nodeN = rootElement.getElementsByTagName("nvalue");
        nVal = nodeN.item(0).getTextContent();
        exp = new HugeInt(expVal);
        n = new HugeInt(nVal);
        pub = pubk;
	}
	
	/* GETTERS */
	
	public HugeInt getN(){
		return n;
	}
	
	public HugeInt getExp(){
		return exp;
	}
	
	public boolean isPublic(){
		return pub;
	}
	
	/* KEY GENERATION */
	
	/*
	 * Function GenKeys(int, int, File, File)
	 * 
	 * generates 2 random prime numbers of the specified lengths,
	 * generates corresponding keys for those primes,
	 * and then writes them to the specified files.
	 * 
	 * Returns nothing
	 */
	public static void GenKeys(int pDecimals, int qDecimals, File pubKey, File priKey) throws FileNotFoundException, IOException{
		HugeInt one = new HugeInt("1");
		HugeInt p = generatePrime(pDecimals);
		HugeInt q = generatePrime(qDecimals);
		HugeInt n = HugeInt.multiply(p, q);
		HugeInt phi = HugeInt.multiply(HugeInt.subtract(p,one), HugeInt.subtract(q,one));
		HugeInt e = genE(n, phi);
		HugeInt d = genD(e, phi);
		writeKey(pubKey, e, n, true);
		writeKey(priKey, d, n, false);
	}

	/*
	 * Function GenKeys(int, HugeInt, File, File)
	 * 
	 * generates 1 random prime number of the specified lengths,
	 * and uses the input prime for the second number and
	 * generates corresponding keys for those primes,
	 * and then writes them to the specified files.
	 * 
	 * Returns nothing
	 */
	public static void GenKeys(int decimals, HugeInt p, File pubKey, File priKey) throws FileNotFoundException, IOException{
		HugeInt one = new HugeInt("1");
		HugeInt q = generatePrime(p.getLength());
		HugeInt n = HugeInt.multiply(p, q);
		HugeInt phi = HugeInt.multiply(HugeInt.subtract(p,one), HugeInt.subtract(q,one));
		HugeInt e = genE(n, phi);
		HugeInt d = genD(e, phi);
		writeKey(pubKey, e, n, true);
		writeKey(priKey, d, n, false);
	}
	
	/*
	 * Function GenKeys(HugeInt, HugeInt, File, File)
	 * 
	 * uses the 2 input prime numbers and
	 * generates corresponding keys for those primes,
	 * and then writes them to the specified files.
	 * 
	 * Returns nothing
	 */
	public static void GenKeys(HugeInt p, HugeInt q, File pubKey, File priKey) throws FileNotFoundException, IOException{
		HugeInt one = new HugeInt("1");
		HugeInt n = HugeInt.multiply(p, q);
		HugeInt phi = HugeInt.multiply(HugeInt.subtract(p,one), HugeInt.subtract(q,one));
		HugeInt e = genE(n, phi);
		HugeInt d = genD(e, phi);
		writeKey(pubKey, e, n, true);
		writeKey(priKey, d, n, false);
	}
	
	/* HELPER FUNCTIONS */
	
	/*
	 * Function GenE
	 * 
	 * generates the e value for the RSA algorithm by trial and error
	 * checks of randomly generated primes. The e value is a number that
	 * is relatively prime to phi, and less than n where
	 * n = p*q and phi = (p-1)(q-1) for prime numbers p,q
	 * 
	 * Returns a suitable e value from the inputs.
	 */
	private static HugeInt genE(HugeInt n, HugeInt phi){
		int deci;
		HugeInt e;
		while (true) {
	        Random rnd = new Random();
	        deci = rnd.nextInt(5)+1;
	        e = generatePrime(deci);
	        if(e.compareTo(n) < 0 && relativePrimes(e, phi)){
	        	return e;
	        }
	    }
	}
	
	/*
	 * Function Gend
	 * 
	 * generates the d value for the RSA algorithm by trial and error
	 * checks. The d value is a number that is the modular inverse of
	 * e mod phi, ie where e*d mod phi = 1 or where e*d = 1+k*phi 
	 * where phi = (p-1)(q-1) for prime numbers p,q
	 * 
	 * Returns a suitable d value from the inputs.
	 */
	private static HugeInt genD(HugeInt e, HugeInt phi) {
        int k = 1;
        HugeInt one = new HugeInt("1");
        HugeInt zero = new HugeInt("0");
        while (true) {
            HugeInt value = HugeInt.multiply(phi, k);
            value = HugeInt.add(value, one);
            value = value.mod(e);
            if (value.compareTo(zero) == 0) {
                return HugeInt.divide(HugeInt.add(HugeInt.multiply(phi, k), one), e);
            }
            k++;
       }
    }
	
	/*
	 * Function relativePrimes
	 * 
	 * determines whether the two inputs are relatively prime by determining if their GCD = 1
	 * 
	 * Returns true if relatively prime, false otherwise
	 */
	private static boolean relativePrimes(HugeInt e, HugeInt phi){
		return HugeInt.gcd(e, phi).compareTo(new HugeInt("1")) == 0;
	}
	
	/*
	 * Function generatePrime
	 * 
	 * generates a random prime number of length decimals
	 * by generating a random number and testing its primality with
	 * the implemented naive primality test. Prime generation with 
	 * the currently implemented naive algorithm starts to become unwieldy
	 * above 10 digits
	 * 
	 * Returns a prime HugeInt of the specified length.
	 */
	private static HugeInt generatePrime(int decimals){
		 while (true) {
	            HugeInt rnd = HugeInt.randomHugeInt(decimals);
	            if (rnd.isPrime()) return rnd;
	        }
	}
	
	/*
	 * Function writeKey
	 * 
	 * Writes a key in proper XML format to a specified file using the information from the inputs.
	 * Determines the tags to be used based on the pub boolean flag, and writes the
	 * exp and n inputs into the appropriate flags.
	 * 
	 * Returns nothing
	 */
	private static void writeKey(File keyName, HugeInt exp, HugeInt n, boolean pub) throws FileNotFoundException, IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(keyName));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<rsakey>");
		if(pub){
            writer.println("<evalue>" + exp + "</evalue>");
		}
		else{
			writer.println("<dvalue>" + exp + "</dvalue>");
		}
        writer.println("<nvalue>" + n + "</nvalue>");
        writer.println("</rsakey>");
        writer.close();

	}
	
}
