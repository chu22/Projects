// Lawrence Chu
// CS342
// CLASS: KingsInTheCorner.java
// Responsibility: This is the main class that handles user input and initializes the game. 
// contains a single game class and a command processing loop which will take input and call the appropriate functions to the game when necessary
// Also handles all invalid command errors

import java.util.Scanner;

public class KingsInTheCorner {
	
	private Game g;
	
	public KingsInTheCorner(){
		g = new Game();
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		KingsInTheCorner k = new KingsInTheCorner();
		k.processCommandLoop(sc);
			
	}
	//main function that will handle all of user input and call the necessary functions for each command. Also checks for when game/round is over to start new game/round
	public void processCommandLoop(Scanner sc) {
		System.out.print("move> ");
		while (sc.hasNext()) {     //loops indefinitely, until quit is called
			
			String command = sc.next();
			if (command.equals("Q") || command.equals("q"))
				Quit(sc);
			
			else if (command.equals("H") || command.equals("h"))
				Help();
			
			else if (command.equals("A") ||command.equals("a"))
				About();
	
			else if (command.equals("D") || command.equals("d")){
				doDraw();
				if(g.GameOver()){
					newGame(sc);
				}
				while(g.roundOver()){
					g.startRound();
				}
			}
	
			else if (command.equals("L") || command.equals("l")){
				doLayDown(sc);
				if(g.GameOver()){
					newGame(sc);
				}
				while(g.roundOver()){
					g.startRound();
				}
			}
			
			else if (command.equals("M") || command.equals("m"))
				doMove(sc);
			
			else
				System.out.println("Command is not known: " + command);
			
			sc.nextLine();
			System.out.print("move> ");
	
		}
	}
	//short into to the program
	private void About(){
		System.out.println("Welcome to Kings in the Corner! This program was made by Lawrence Chu for CS342 at UIC.");
		System.out.println("Go to http://www.pagat.com/domino/kingscorners.html for rules on how to play this simple card game.");
		System.out.println("Press <H> to learn about your commands. You will be playing 1v1 against the computer. Enjoy!");
		
	}
	
	public void Help() {
		System.out.println("The commands for this project are:");
		System.out.println("  Q : quit the game ");
		System.out.println("  H : list user commands ");
		System.out.println("  A : about the game ");
		System.out.println("  D : draw a card");
		System.out.println("  L <Card> <Pile> : move a card onto a pile");
		System.out.println("  M <Pile1> <Pile2> : move pile 1 onto pile 2");
		System.out.println();
		System.out.println("Enter a <Card> in the following format: <Rank><Suit>");
		System.out.println("The list of <Rank>s is: A - ace, 2-9, T - ten, J - jack, Q - queen, K - king");
		System.out.println("The list of <Suit>s is: C - club, H - heart, D - diamond, S - spade");
		System.out.println("Enter a <Pile> with a number, 1-8");
	}
	//checks if user really wants to quit before exiting
	public void Quit(Scanner sc){
		System.out.println("Are you sure you want to quit? Y/N");
		sc.nextLine();
		String q = sc.next();
		while(!q.equals("Y")&&!q.equals("N")){
			System.out.println("Command unknown. Enter Y or N");
			sc.nextLine();
			q = sc.next();
		}
		if(q.equals("Y")){
			System.exit(1);
		}
		else{
			return;
		}
	}
	//draws
	public void doDraw(){
		g.Draw();
		g.MoveAI();
	}
	//user lay down
	public void doLayDown(Scanner sc){
		String c;
		int p;
		if (sc.hasNext())
			c = sc.next();
		else {
			System.out.println("Card value expected");
			return;
		}
		if(c.length()!=2){
			System.out.println("2 characters required for card value.");
			return;
		}
		int r = intRank(c.charAt(0));
		int s = intSuit(c.charAt(1));
		if(sc.hasNextInt()){
			p = sc.nextInt();
		}
		else{
			System.out.println("Integer value expected");
			return;
		}
		g.LayDown(r, s, p);
	}
	//user moving foundations
	public void doMove(Scanner sc){
		int p1;
		int p2;
		if(sc.hasNextInt()){
			p1 = sc.nextInt();
		}
		else{
			System.out.println("Integer value expected");
			return;
		}
		if(sc.hasNextInt()){
			p2 = sc.nextInt();
		}
		else{
			System.out.println("Integer value expected");
			return;
		}
		g.Move(p1,p2);
	}
	//asks whether user wants to play again after a game has finished
	private void newGame(Scanner sc){
		System.out.println("Would you like to play another game? Y/N");
		sc.nextLine();
		String q = sc.next();
		while(!q.equals("Y")&&!q.equals("N")){
			System.out.println("Command unknown. Enter Y or N");
			sc.nextLine();
			q = sc.next();
		}
		if(q.equals("N")){
			System.exit(1);
		}
		else{
			g = new Game();
		}
	}
	//function to convert user card input to integer representation
	private int intRank(char r){
		switch(r){
		case 'A':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'T':
			return 10;
		case 'J':
			return 11;
		case 'Q':
			return 12;
		case 'K':
			return 13;
		default:
			return -1;
		}
	}
	
	private int intSuit(char s){
		switch(s){
		case 'C':
			return 1;
		case 'D':
			return 2;
		case 'H':
			return 3;
		case 'S':
			return 4;
		default:
			return -1;
		}
	}	
}
