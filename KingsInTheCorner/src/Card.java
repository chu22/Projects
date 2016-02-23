// Lawrence Chu
// CS342
// CLASS: Card.java
// Responsibility: This class  will hold each Card object in the 52 card deck. 
// Each card contains a rank and suit, and has operations that allow cards to be compared based on rank and suit.
// Rank and suit are held as integers, but there is also a buit in converter to represent them as characters


public class Card {
	//rank and suit instance variables
	private int r;
	private int s;
	//constructor
	public Card(int rank, int suit){
		r = rank;
		s = suit;
	}
	// rank and suit getters
	public int getRank(){
		return r;
	}
	
	public int getSuit(){
		return s;
	}
	//functions convert integer representations of r and s to characters 
	private char charRank(){
		switch(r){
		case 1:
			return 'A';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		case 10:
			return 'T';
		case 11:
			return 'J';
		case 12:
			return 'Q';
		case 13:
			return 'K';
		default:
			System.err.println("Invalid Card");
			return 'Z';
		}
	}
	
	private char charSuit(){
		switch(s){
		case 1:
			return 'C';
		case 2:
			return 'D';
		case 3:
			return 'H';
		case 4:
			return 'S';
		default:
			System.err.println("Invalid Card");
			return 'Z';
		}
	}
	
	//check equality based on 2 integer inputs
	public boolean equals(int rank, int suit){
		if(r==rank&&s==suit){
			return true;
		}
		return false;
	}
	//check if this card is less than input card, used for sorting
	public boolean lessThan(Card c){
		if(r<c.r){
			return true;
		}
		if(r==c.r&&s<c.s){
			return true;
		}
		return false;
	}
	//checks if this card is one less than input card, used for determining whether a card can be laid 
	private boolean oneLessThan(Card c){
		if(r==c.r-1){
			return true;
		}
		return false;
	}
	//checks for opposite color suit, for determining whether card can be laid
	private boolean oppositeSuit(Card c){
		switch(c.s){
		case 1:
		case 4:
			return (s==2||s==3);
		case 2:
		case 3:
			return (s==1||s==4);
		default:
			System.err.println("Invalid Card");
			return false;
		}
	}
	//function that combines above 2 functions to determine whether this card can be laid on top of the input card. Returns true of input card is null as well, which will represent an empty pile.
	public boolean canLayDown(Card c){
		return c==null||oneLessThan(c)&&oppositeSuit(c);
	}
	//returns if card is a king, for scoring uses as well as checking special cases of piles 5-8
	public boolean isKing(){
		return r==13;
	}
	//prints char representation of card
	public void printCard(){
		System.out.print(charRank() + "" + charSuit());
	}
}
