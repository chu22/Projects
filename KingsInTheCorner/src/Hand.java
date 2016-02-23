// Lawrence Chu
// CS342
// CLASS: Hand.java
// Responsibility: This class will hold each of the players' hands. It inherits the functions and instance variables of the CardPile class
// It additionally keeps track of how many kings the hand currently has, to make scoring easier
// It has additional functions pertaining to sorting and removing specific cards, which are unique to the hands in the game

public class Hand extends CardPile {

	private int nKings;
	
	public Hand() {
		super(26);
		nKings=0;
	}
	//AI method used to determine whether the computer has a valid move to lay down a card
	public Card AIcanLay(Card c){
		for(int i=0;i<nCards;i++){
			if(cards[i].canLayDown(c)){
				return cards[i];
			}
		}
		return null;
	}
	//helper function to locate cards within the hand
	private int getCardInd(int rank, int suit){
		for(int i=0;i<nCards;i++){
			if(cards[i].equals(rank,suit)){
				return i;
			}
		}
		return -1;
	}
	//addCard function overloaded to implement the nKings instance variable
	public void addCard(Card c){
		super.addCard(c);
		if(c.isKing()){
			nKings++;
		}
	}
	//gets a card in the hand
	public Card getCard(int rank, int suit){
		int h = getCardInd(rank,suit);
		if(h==-1){
			return null;
		}
		return cards[h];
	}
	//removes the card in the hand
	public void removeCard(int rank, int suit){
		int h = getCardInd(rank,suit);
		if(cards[h].isKing()){
			nKings--;
		}
		nCards--;
		for(int i=h;i<nCards;i++){
			cards[i]=cards[i+1];
		}
		
	}
	//sorts the cards, algorithm assumes that all cards except for the last one are already pre-sorted
	public void sort(){
		if(nCards<2){
			return;
		}
		Card tmp = cards[nCards-1];
		int i = 0;
		while(cards[i].lessThan(tmp)){
			i++;
		}
		for(int j=nCards-1;j>i;j--){
			cards[j] = cards[j-1];
		}
		cards[i]=tmp;
	}
	//returns the largest card in the hand, which should also be the one at the largest index due to sorting
	public Card getLargest(){
		if(nCards==0){
			return null;
		}
		return cards[nCards-1];
	}
	//returns penalty point value of current hand
	public int handScore(){
		return nKings*9+nCards;
	}
}
