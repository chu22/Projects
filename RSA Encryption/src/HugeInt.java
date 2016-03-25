/*
 * 
 * HugeInt.java
 * This class allows representation of large unsigned integer numbers beyond those allowed by primitive types in Java.
 * It represents these numbers by storing digits in dynamic arrays and supports several basic arithmetic operations for the numbers.
 * 
 * Written by Lawrence Chu
 * 
 */

import java.util.Random;

public class HugeInt implements Comparable<HugeInt> {
	
	private int[] digits;	//array containing the number's digits
	private int length;     //number of digits in the number
	
	/* CONSTRUCTORS */
	
	/*
	 * Constructor: HugeInt(String)
	 *
	 * Primary constructor for HugeInt class with a numeric string input
	 * Will error check for illegal strings, and trims leading zeroes
	 * 
	 * Returns a HugeInt with the numeric value of the string
	 */
	public HugeInt(String num) throws IllegalArgumentException{
		length = num.length();
		digits = new int[length];
        int x = length - 1;
        for (int i = x; i >= 0; i--) {
        	int d = Character.getNumericValue(num.charAt(i));
        	if(d==-1){
        		throw new IllegalArgumentException("Input contains non-digit characters.");
        	}
            digits[x-i] = d;
        }
        trim();
	}
	
	
	/*
	 * Constructor HugeInt(HugeInt)
	 *
	 * Copy constructor for HugeInt class with a numeric string input
	 * 
	 * Returns a HugeInt copy of the input
	 */
	public HugeInt(HugeInt h){
		length = h.length;
		digits = new int[length];
		for (int i = 0; i < h.length; i++){
			digits[i] = h.digits[i];
		}
	}
	
	/*
	 * Constructor: HugeInt(int[])
	 *
	 * Private constructor for HugeInt class with an input digit array.
	 * Used within the class to quickly produce the results for arithmetic 
	 * operations within the class. It has no error checking because all 
	 * calls to the constructor are assumed to be correct.
	 * 
	 * Returns a HugeInt with the specified array as its digit array.
	 */
	private HugeInt(int[] arr){
		digits = arr;
		length = arr.length;
	}
	
	/*
	 * Function: randomHugeInt
	 *
	 * Static method that generates a random HugeInt
	 * 
	 * Returns a randomly generated HugeInt of length n
	 */
	public static HugeInt randomHugeInt(int n){
		int[] tmp = new int[n];
		Random r = new Random();
		for(int i = 0;i<n-1;i++){
			tmp[i] = r.nextInt(10);
		}
		tmp[n-1] = r.nextInt(9)+1;
		return new HugeInt(tmp);
	}
	
	/* GETTERS */
	
	public int getLength(){
		return length;
	}
	
	/* OPERATIONS */
	
	/*
	 * Function: shiftLeft
	 *
	 * Shifts digits of the HugeInt to the left
	 * effectively multiplying the number by 10
	 * per digit shifted.
	 * 
	 * Returns a HugeInt that has been shifted left
	 * positions digits from the original
	 */
	public HugeInt shiftLeft(int positions) {
        if(positions==0){
        	return new HugeInt(this);
        }
        int[] tmp = new int[length+positions];
        for (int i = 0; i < positions; i++){
        	tmp[i] = 0;
        }
        for (int i = 0; i < length; i++){
        	tmp[i+positions] = digits[i];
        }
        HugeInt result = new HugeInt(tmp);
        return result;
    }

	/*
	 * Function: shiftRight
	 *
	 * Shifts digits of the HugeInt to the right
	 * effectively dividing the number by 10
	 * per digit shifted.
	 * 
	 * Returns a HugeInt that has been shifted right
	 * positions digits from the original
	 */
	 public HugeInt shiftRight(int positions) {
        if(positions==0){
        	new HugeInt(this);
        }
        int[] tmp = copyDigits(positions,length-1);
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
    }
		 
	 /*
	 * Function: add
	 *
	 * Adds two HugeInts together. Addition is done via the
	 * basic long addition algorithm
	 * 
	 * Returns a HugeInt representing x+y
	 */
	 public static HugeInt add(HugeInt x, HugeInt y){
		int[] tmp;
		int min;
		if(x.compareTo(y)<0){
			tmp = new int[y.length+1];
			min = x.length;
		}
		else{
			tmp = new int[x.length+1];
			min = y.length;
		}
        int carry = 0;
        for (int i = 0; i < min; i++) {
            carry = carry + x.digits[i] + y.digits[i];
            tmp[i] = carry % 10;
            carry = carry / 10;
        }
        int max;
        if(y.length < x.length){
        	max = x.length;
            for (int i = min; i < max; i++) {
                carry = carry + x.digits[i];
                tmp[i] = carry % 10;
                carry = carry / 10;
            }
        }
        else{
        	max = y.length;
            for (int i = min; i < y.length; i++) {
                carry = carry + y.digits[i];
                tmp[i] = carry % 10;
                carry = carry / 10;
            }
        }
        if (carry > 0){
        	tmp[max] = carry;
        }
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
	}
	
	 /*
	 * Function: subtract
	 *
	 * Subtracts two HugeInts. Subtraction is done via the
	 * basic long subtraction algorithm. The function assumes
	 * the second argument will be subtracted from the first.
	 * An exception will be thrown if the result is negative.
	 * 
	 * Returns a HugeInt representing x-y
	 */
	public static HugeInt subtract(HugeInt x, HugeInt y) throws IllegalArgumentException{
        int borrow = 0;
        int[] tmp;
        if(x.compareTo(y)<=0){
        	if(x.compareTo(y)<0){
        		throw new IllegalArgumentException("The first argument must be larger than the second.");
        	}
        	return new HugeInt("0");
        }
        tmp = new int[x.length];
        for (int i = 0; i < y.length; i++) {
            borrow = borrow + 10 + x.digits[i] - y.digits[i];
            tmp[i] = borrow % 10;
            borrow = borrow < 10 ? -1 : 0;
        }
        for (int i = y.length; i < x.length; i++) {
            borrow = borrow + 10 + x.digits[i];
            tmp[i] = borrow % 10;
            borrow = borrow < 10 ? -1 : 0;
        }
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
	}
	
	
	 /*
	 * Function: multiply(HugeInt, HugeInt)
	 *
	 * Multiplies two HugeInts together using the basic long
	 * multiplication algorithm. 
	 * 
	 * Returns a HugeInt representing x*y
	 */
	public static HugeInt multiply(HugeInt x, HugeInt y){
		int[] tmp = new int[x.length+y.length];
        int carry, k;
        for (int i = 0; i < x.length; i++){
            for (int j = 0; j < y.length; j++) {
                carry = x.digits[i] * y.digits[j];
                k = i + j;
                while (carry > 0) {
                    carry += tmp[k];
                    tmp[k] = carry % 10;
                    carry /= 10;
                    k++;
                }
            }
        }
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
	}
	
	 /*
	 * Function: multiply(HugeInt, int)
	 *
	 * Multiplies a hugeInt and an integer using the basic long
	 * multiplication algorithm.
	 * 
	 * Returns a HugeInt representing x*y
	 */
	public static HugeInt multiply(HugeInt x, int y){
        int carry = 0;
        int[] tmp = new int[x.length + 20];
        int i;
        for (i = 0; i < x.length; i++) {
            int m = x.digits[i] * y + carry;
            carry = m / 10;
            tmp[i]  = m % 10;
        }
        while (carry > 0) {
            tmp[i] = carry % 10;
            carry /= 10;
            i++;
        }
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
    }
	
	 /*
	 * Function: divide
	 *
	 * Divides two HugeInts using the basic long division algorithm.
	 * The function assumes the first argument is the dividend and
	 * the second is the divisor. An exception will be thrown if the
	 * divisor is zero. 
	 * 
	 * Returns a HugeInt representing x/y
	 */
	public static HugeInt divide(HugeInt x, HugeInt y) throws IllegalArgumentException{
		if(y.compareTo(new HugeInt("0"))==0){
			throw new IllegalArgumentException("The divisor is zero.");
		}
		HugeInt r = new HugeInt(x);
        HugeInt t;
        HugeInt m;
        int shift;
        int[] tmp = new int[x.length];
        int lenA = x.length;
        int lenB = y.length;
        int dig;     
        while (y.compareTo(r) <= 0) {
        	shift = lenA - lenB;
            t = r.shiftRight(shift);
            if (t.compareTo(y) < 0) {
                shift--;
            }
            dig = 9;
            m = multiply(y,dig).shiftLeft(shift);
            while (r.compareTo(m) < 0) {
                dig--;
                m = multiply(y, dig).shiftLeft(shift);
            }
            r = subtract(r, m);
            tmp[shift] = dig;
            r.trim();
            lenA = r.length;
        }
        HugeInt result = new HugeInt(tmp);
        result.trim();
        return result;
	}
	
	 /*
	 * Function: mod
	 *
	 * Computes the modulus of a HugeInt using the input divisor via
	 * the basic long division algorithm. 
	 * 
	 * Returns a HugeInt representing the remainder of the original HugeInt
	 * divided by y.
	 */
	public HugeInt mod(HugeInt y) throws IllegalArgumentException{
		if(y.compareTo(new HugeInt("0"))==0){
			throw new IllegalArgumentException("The divisor is zero.");
		}
		HugeInt r = new HugeInt(this);
        HugeInt t;
        HugeInt m;
        int shift;
        int lenA = length;
        int lenB = y.length;
        int dig;     
        while (y.compareTo(r) <= 0) {
        	shift = lenA - lenB;
            t = r.shiftRight(shift);
            if (t.compareTo(y) < 0) {
                shift--;
            }
            dig = 9;
            m = multiply(y,dig).shiftLeft(shift);
            while (r.compareTo(m) < 0) {
                dig--;
                m = multiply(y, dig).shiftLeft(shift);
            }
            r = subtract(r, m);
            r.trim();
            lenA = r.length;
        }
        return r;
	}
	
	 /*
	 * Function: pow
	 *
	 * Raises a HugeInt to the power p using an exponentiation by
	 * squaring algorithm.
	 * 
	 * Returns a HugeInt representing x^p
	 */
	public static HugeInt pow(HugeInt x, int p){
		HugeInt one = new HugeInt("1");
    	HugeInt acc = one;
    	if(p==0){
    		return one;
    	}
    	HugeInt c = multiply(one, x);
    	while(p>1){
    		if(p%2==0){
    			c = multiply(c, c);
    		}
    		else if(p%2==1){
    			acc = multiply(acc, c);
    			c = multiply(c, c);
    		}
    		p/=2;
    	}
    	return multiply(c, acc);
	}
	
	 /*
	 * Function: modPow
	 *
	 * Calculates the modular exponent of m raised to the e
	 * modded by n, using modular arithmetic properties and
	 * exponentiation by squaring techniques.
	 * 
	 * Returns a HugeInt representing m^e mod n
	 */
	public static HugeInt modPow(HugeInt m, HugeInt e, HugeInt n) {
    	HugeInt two = new HugeInt("2");
    	HugeInt one = new HugeInt("1");
    	HugeInt zero = new HugeInt("0");
    	HugeInt acc = one;
    	if(e.compareTo(zero)==0){
    		return one;
    	}
    	HugeInt c = multiply(m, one);
    	while(e.compareTo(one)>0){
    		if(e.mod(two).compareTo(zero)==0){
    			c = multiply(c, c).mod(n);
    		}
    		else if(e.mod(two).compareTo(one)==0){
    			acc = multiply(acc, c).mod(n);
    			c = multiply(c, c).mod(n);
    		}
    		e = divide(e, two);
    	}
    	return multiply(c, acc).mod(n);
    }
	
	/*
	 * Function: gcd
	 *
	 * Produces the greatest common divisor of 2 HugeInts based on the
	 * Euclidean Algorithm.
	 * 
	 * Returns a HugeInt representing the gcd of the inputs
	 */
	public static HugeInt gcd(HugeInt x, HugeInt y){
		HugeInt zero = new HugeInt("0");
		HugeInt t;
		while(y.compareTo(zero)>0){
			t = y;
			y = x.mod(y);
			x = t;
		}
		return x;
	}
	
	 /*
	 * Function: compareTo
	 *
	 * Override of the compareTo method in the comparable class. 
	 * Allows comparisons between HugeInts
	 * 
	 * Returns 1 if the original is greater than h, 0 on equality, and -1
	 * if it is less than.
	 */
	public int compareTo(HugeInt h) {
		//First we compare sizes
        if (length < h.length) return -1;
        if (length > h.length) return 1;
        //At this point we know that both instances have equal sizes, we have to compare each digit
        for (int i = length - 1; i >= 0; i--) {
            if (digits[i] < h.digits[i]){
            	return -1;
            }
            else if (digits[i] > h.digits[i]){
            	return 1;
            }
        }
        //The instances are equal
        return 0;
	} 
	
	 /*
	 * Function: isPrime
	 *
	 * Determines whether a HugeInt is prime using the naive primality test
	 * that simply divides the number by numbers up to its square root 
	 * 
	 * Returns true on a prime number, false otherwise
	 */
	public boolean isPrime() {
    	HugeInt zero = new HugeInt("0");
    	HugeInt two = new HugeInt("2");
    	if(compareTo(new HugeInt("1"))<=0) return false;
    	else if(compareTo(two)==0) return true;
    	else if(mod(two).compareTo(zero)==0) return false;
    	HugeInt i = new HugeInt("3");
    	while (HugeInt.multiply(i,i).compareTo(this)<=0){
    		if(mod(i).compareTo(zero)==0) return false;
    		i = HugeInt.add(i,two);
    	}
        return true;
    } 
	
	/* HELPER FUNCTIONS */
	
	 /*
	 * Function: trim
	 *
	 * Trims leading zeroes from a HugeInt
	 * 
	 * Returns nothing. directly manipulates the digit array
	 * of the HugeInt.
	 */
	private void trim(){
		if (length == 0) return;
        int z = length - 1;
        while (z>0&&digits[z] == 0) {
            z--;
        }
        if(z == length-1) return;
        length = z + 1;
        digits = copyDigits(length);
	}
	
	 /*
	 * Function: copyDigits(int n)
	 *
	 * copies the first n entries of the digit array of a HugeInt
	 * into a new array.
	 * 
	 * Returns an integer array with those n entries.
	 */
	private int[] copyDigits(int n){
		int[] tmp = new int[n];
		for(int i = 0;i<n;i++){
			tmp[i] = digits[i];
		}
		return tmp;
	}
	
	/*
	 * Function: copyDigits(int n)
	 *
	 * copies entries in digit array of a HugeInt from
	 * indices in the range [lo,hi] into a new array
	 * 
	 * Returns an integer array with the listed entries.
	 */
	private int[] copyDigits(int lo, int hi){
		int[] tmp = new int[hi-lo+1];
		for(int i = lo;i<=hi;i++){
			tmp[i-lo] = digits[i];
		}
		return tmp;
	}
	
	/*
	 * Function: toString
	 *
	 * Creates a string representation of a HugeInt
	 * 
	 * Returns a string representation of the HugeInt
	 */
	public String toString() {
        String result = new String();
        for (int i = length - 1; i >= 0; i--){
        	result+=digits[i];
        }
        return result;
    }
}
