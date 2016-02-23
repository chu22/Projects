// Lawrence Chu
// CS342
// CLASS: Player.java
// Responsibility: This class will hold useful variables and funcions for both the user and AI players.
// Each player has a hand and a number of penalty points
// The class gives the player the ability to draw and access the player's hand.
public class Player {
	
	private int points;
	private Hand hand;
	//functions should be self explanatory
	public Player(){
		points = 0;
		hand = new Hand();
	}
	
	public void Draw(Card c){
		if(c==null){
			return;
		}
		hand.addCard(c);
		hand.sort();
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void addPoints(){
		points+=hand.handScore();
	}
	
	public int handSize(){
		return hand.size();
	}
	
	public void printHand(){
		hand.printPile();
	}
	
	public Card getLast(){
		return hand.getLargest();
	}
}
