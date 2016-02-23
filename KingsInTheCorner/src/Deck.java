// Lawrence Chu
// CS342
// CLASS: Deck.java
// Responsibility: This class will hold the deck for the game. 
// It is set to initialize a standard 52 card deck and automatically shuffle the cards when called.
// Contains functions to shuffle and deal cards from top of the deck
import java.util.Random;

public class Deck extends CardPile {
	//constructor initializes all 52 cards and shuffles them 
	public Deck() {
		super(52);
		int c = 0;
		for(int i=1;i<14;i++){
			for(int j=1;j<5;j++){
				cards[c] = new Card(i,j);
				c++;
			}
		}
		nCards = 52;
		shuffle();
 	}
	
	private void shuffle(){
		Random rn = new Random();
		int r;
		for(int i=0;i<nCards;i++){
			r = rn.nextInt(nCards-i)+i;
			Card tmp = cards[i];
			cards[i]=cards[r];
			cards[r]=tmp;
			
		}
	}
	//deals 1 card off the top, returns null if all cards have been dealt.
	public Card dealOne(){
		if(nCards==0){
			return null;
		}
		nCards--;
		return cards[nCards];
	}
	
}
