// Lawrence Chu
// CS342
// CLASS: Foundation.java
// Responsibility: This class will hold each of the 8 piles in the game. It inherits the functions and instance variables from the CardPile class. 
// It additionally contains functions specific to foundation piles, to allow for adding/removing all cards from a pile and checking the bottom and top cards of the pile
//
public class Foundation extends CardPile {
	//constructor
	public Foundation() {
		super(13);

	}
	///removes all cards from pile by creating a new instance of cards
	public void removeAll(){
		cards = new Card[cap];
		nCards = 0;
	}
	//adds a pile on top of current cards
	public void addPile(Foundation c){
		for(int i=0;i<c.nCards;i++){
			addCard(c.cards[i]);
		}
	}
	//gets top card of pile, used for checking whether a card/pile can be placed onto it. Returns null if pile is empty
	public Card getTop(){
		if(isEmpty()){
			return null;
		}
		return cards[nCards-1];
	}
	//gets bottom card of pile, used for checking whether it can be placed on a pile. Returns null if pile is empty
	public Card getBottom(){
		if(isEmpty())
		{
			return null;
		}
		return cards[0];
	}
}
