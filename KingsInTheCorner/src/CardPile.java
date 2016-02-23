// Lawrence Chu
// CS342
// CLASS: CardPile.java
// Responsibility: This class be a general object that holds variables and common function of each collection of cards in the game, namely the hand, foundation pile, and draw deck. 
// Each CardPile has an array of cards, as well as an integer keeping track of the number of cards in the pile.
// Functions will keep track of adding cards and checking hand size.

public class CardPile {
	protected int nCards;
	protected int cap;
	protected Card[] cards;
	//constructor that can create piles of different starting capacity.
	public CardPile(int n){
		nCards = 0;
		cap = n;
		cards = new Card[n];
	}
	//resize function for potential dynamic allocation, shouldn't be needed
	private void resize(){
		cap*=2;
		Card copy[] = new Card[cap];
		for (int i = 0; i < nCards; i++) {
			copy[i] = cards[i];
		}
		cards = copy;
	}
	//add card to pile
	public void addCard(Card c){
		if(c==null){
			return;
		}
		if (nCards == cap) {
			resize();
		}
		nCards++;
		cards[nCards-1] = c;
	}
	public boolean isEmpty(){
		return nCards==0;
	}
	
	public int size(){
		return nCards;
	}
	//prints card pile using printCard method for Card.
	public void printPile(){
		for(int i = 0;i<nCards;i++){
			cards[i].printCard();
			System.out.print(" ");
		}
	}
}
