// Lawrence Chu
// CS342
// CLASS: Game.java
// Responsibility: This class will keep track of the overall game state and handle all actions by the computer and user. 
// The class holds all information about the players and the cards, as well as the current round and points.
// All possible actions within the game are managed here
public class Game {
	//instance variables for players, cards, round, and turn
	private Player user;
	private Player comp;
	private Deck deck;
	private Foundation[] piles;
	private int round;
	private int turn;
	//constructor game starts at round 1, calls start round method which will set up each round of the game
	public Game(){
		round = 1;
		user = new Player();
		comp = new Player();
		startRound();
	}
	
	public void startRound(){
		System.out.println("Starting round " + round +"!");
		piles = new Foundation[8];
		deck = new Deck();
		for(int i = 0;i<8;i++){
			piles[i] = new Foundation();
		}
		printGameState();
		Deal();
	}
	
	//deals cards alternating between player and user, non dealer plays first. Odd turns belong to the user, even to the computer
	private void Deal(){
		if(round%2==1){
			for(int i=0;i<7;i++){
				user.Draw(deck.dealOne());
				comp.Draw(deck.dealOne());
			}
			for(int i=0;i<4;i++){
				piles[i].addCard(deck.dealOne());
			}
			turn = 1;
		}
		else{
			for(int i=0;i<7;i++){
				comp.Draw(deck.dealOne());
				user.Draw(deck.dealOne());
			}
			for(int i=0;i<4;i++){
				piles[i].addCard(deck.dealOne());
			}
			turn = 0;
			MoveAI();
		}
		printGameState();
	}
	//single draw function that both AI and player will use. Ends callers turn
	public void Draw(){
		if(deck.isEmpty()){
			System.out.println("Cannot draw. Deck is out of cards!");
			turn++;
			printGameState();
			return;
		}
		if(turn%2==1){
			Card c = deck.dealOne();
			user.Draw(c);
			System.out.print("You have drawn ");
			c.printCard();
			System.out.println();
			printGameState();
		}
		else{
			comp.Draw(deck.dealOne());
			System.out.println("The computer has drawn a card. It is now your turn.");
			printGameState();
		}
		turn++;
	}
	//handles user player moving of one pile on top of another, based on integer inputs. Also does all error checking based upon inputs
	public void Move(int f1, int f2){
		if(f1<1||f1>8||f2<1||f2>8){
			System.out.println("Invalid foundation piles. Choose piles 1-8");
			return;
		}
		if(f1==f2){
			System.out.println("Must pick distinct foundation piles to move.");
			return;
		}
		Card b = piles[f1-1].getBottom();
		Card t = piles[f2-1].getTop();
		if(b==null){
			System.out.println("Cannot move an empty foundation pile.");
			return;
		}
		if(f2>4&&t==null&&!b.isKing()){
			System.out.println("Foundation piles 5-8 must have a King as the bottom card.");
			return;
		}
		if(b.canLayDown(t)){
			piles[f2-1].addPile(piles[f1-1]);
			piles[f1-1].removeAll();
			System.out.println("Foundation pile " + f1 + " has been moved to Foundation pile " + f2);
			printGameState();
			
		}
		else{
			if(turn%2==1){
				System.out.print("Foundation piles do not match. ");
				b.printCard();
				System.out.print(" cannot be placed on top of ");
				t.printCard();
				System.out.println();
			}
		}
	}
	//handles user player laying down of a single card onto a pile. Handles all error checking based on inputs
	public void LayDown(int rank, int suit, int f){
		if(f<1||f>8){
			System.out.println("Invalid foundation pile. Choose piles 1-8");
			return;
		}
		if(rank<1||rank>13||suit<1||suit>4){
			System.out.println("Invalid card name. Press <H> for information on card inputs.");
			return;
		}
		Card t = piles[f-1].getTop();
		Card c = user.getHand().getCard(rank,suit);
		if(c==null){
			Card x = new Card(rank,suit);
			System.out.print("Card ");
			x.printCard();
			System.out.println(" does not exist in your hand");
			return;
		}
		
		if(f>4&&t==null&&!c.isKing()){
			System.out.println("The bottom card of Foundation piles 5-8 must be Kings.");
			return;
		}
		if(c.canLayDown(t)){
			user.getHand().removeCard(rank, suit);
			piles[f-1].addCard(c);
			c.printCard();
			System.out.println(" has been placed onto Foundation pile " + f);			
			printGameState();
			if(roundOver()){
				endRound();
			}
		}
		else{
			System.out.print("Cards do not match. ");
			c.printCard();
			System.out.print(" cannot be placed on top of ");
			t.printCard();
			System.out.println();
		}
	}
	//prints state of game, with foundation piles, player's hand, AI's hand size
	public void printGameState(){
		System.out.println();
		for(int i = 1;i<9;i++){ //prints piles
			System.out.print("Pile " + i + ": ");
			piles[i-1].printPile();
			System.out.println();
		}
		System.out.println("Computer player has " + comp.handSize() + " cards");
		//System.out.print("Comp hand: ");  //used to see computer's hand for debugging
		//comp.printHand();
		//System.out.println();
		System.out.print("Your hand: "); // prints user hand
		user.printHand();
		System.out.println();
	}
	//checks if round is over
	public boolean roundOver(){
		return user.handSize()==0||comp.handSize()==0;
	}
	//if round is over, this will be called. Adds points to losing player
	public void endRound(){
		user.addPoints();
		comp.addPoints();
		if(user.handSize()==0){
			System.out.println("You have won the round! The computer has gained " + comp.getHand().handScore() + " penalty points.");
		}
		else{
			System.out.println("The computer has won the round! You have gained " + user.getHand().handScore() + " penalty points.");
		}
		System.out.println("The score is now - User: " + user.getPoints() + " Computer: " + comp.getPoints());
		round++;
	}
	//checks if 25 point maximum has been exceeded
	public boolean GameOver(){
		if(user.getPoints()>=25){
			System.out.println("You have reached 25 penalty points. You Lose!");
			return true;
		}
		if(comp.getPoints()>=25){
			System.out.println("The computer has reached 25 penalty points. You Win!");
			return true;
		}
		return false;
	}
	//function that contains AI algorithm. Moves the kings and then calls nested functions to execute each step of the algorithm in the proper order
	public void MoveAI(){
		System.out.println();
		System.out.println("Computer player now moving...");
		AImoveKings();  		//step 1 of algorithm
		while(AIlayEmpty()){}  // calls step 4 of algorithm indefinitely as long as it can execute
		if(roundOver()){        //check round over
			endRound();
		}
		else{					//end turn otherwise
			Draw();
		}
	}
	//step 1 of algorithm
	public void AImoveKings(){
		int i = 5;
		Card t = comp.getLast();
		while(comp.handSize()!=0&&t.isKing()){
			while(!piles[i-1].isEmpty()){
				i++;
			}
			comp.getHand().removeCard(t.getRank(),t.getSuit());
			piles[i-1].addCard(t);
			t.printCard();
			System.out.println(" has been placed onto Foundation pile " + i);			
			//printGameState();
			t = comp.getLast();
		}
		for(int j = 0;j<4;j++){
			if(!piles[j].isEmpty()&&piles[j].getBottom().isKing()){
				while(!piles[i].isEmpty()){
					i++;
				}
				Move(j+1,i+1);
			}
		}
	}
	//step 2 of algorithm, nested at beginning of step 3 in a while loop so that it will be prioritized first any time any other steps are called
	public boolean AImovePiles(){
		for(int i=1;i<5;i++){
			if(!piles[i-1].isEmpty()){
				for(int j=1;j<9;j++){
					if(!piles[j-1].isEmpty()){
						Card b = piles[i-1].getBottom();
						Card t = piles[j-1].getTop();
						if(b.canLayDown(t)){
							piles[j-1].addPile(piles[i-1]);
							piles[i-1].removeAll();
							System.out.println("Foundation pile " + i + " has been moved to Foundation pile " + j);
							//printGameState();
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	//step 3 of algorithm, contains while loop that will execute step 2 as long as it can before moving on. Is also nested within step 4 to achieve the same result
	public boolean AIlayNotEmpty(){
		while(AImovePiles()){}
		Card t;
		for(int i=1;i<9;i++){
			if(!piles[i-1].isEmpty()){
				t = comp.getHand().AIcanLay(piles[i-1].getTop());
				if(t!=null){
					comp.getHand().removeCard(t.getRank(), t.getSuit());
					piles[i-1].addCard(t);
					t.printCard();
					System.out.println(" has been placed onto Foundation pile " + i);			
					//printGameState();
					return true;
				}
			}
		}
		return false;
	}
	//step 4 of the algorithm with nested functions
	public boolean AIlayEmpty(){
		while(AIlayNotEmpty()){}
		Card t;
		for(int i=1;i<5;i++){
			if(piles[i-1].isEmpty()){
				t = comp.getLast();
				if(t!=null){
					comp.getHand().removeCard(t.getRank(),t.getSuit());
					piles[i-1].addCard(t);
					t.printCard();
					System.out.println(" has been placed onto Foundation pile " + i);			
					//printGameState();
					return true;
				}
			}
		}
		return false;
	}
	
}
