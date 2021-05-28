import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Senet {
	private HashMap<Integer, HashMap<Integer, Player>> boards;
	private HashMap<Integer, String> boardNames;
	private ArrayList<Player> players;
	private Scanner sc = new Scanner(System.in);
	private Dice dice = new Dice();

	public Senet() {
		// Create boards
		boardNames = new HashMap<Integer, String>();
		boards = new HashMap<Integer, HashMap<Integer, Player>>();
		// Add players with standard names
		players = new ArrayList<Player>();
		players.add(new Player("Black"));
		players.add(new Player("White"));
		// Board 1
		boardNames.put(1, "Test board 1");
		boards.put(1, new HashMap<Integer, Player>());
		boards.get(1).put(1, players.get(0));
		boards.get(1).put(16, players.get(0));
		boards.get(1).put(23, players.get(0));
		for (int i = 2; i <= 14; i += 2) {
			boards.get(1).put(i, players.get(1));
		}
		boards.get(1).put(8, players.get(0));
		boards.get(1).put(17, players.get(1));
		boards.get(1).put(18, players.get(1));
		boards.get(1).put(20, players.get(1));
		boards.get(1).put(21, players.get(1));
		for (int i = 24; i <= 26; i++) {
			boards.get(1).put(i, players.get(1));
		}
		// Board 2
		boardNames.put(2, "Test board 2");
		boards.put(2, new HashMap<Integer, Player>());
		boards.get(2).put(29, players.get(0));
		boards.get(2).put(22, players.get(1));
		boards.get(2).put(23, players.get(1));
		boards.get(2).put(24, players.get(1));
		// Board 3
		boardNames.put(3, "Test board 3");
		boards.put(3, new HashMap<Integer, Player>());
		boards.get(3).put(13, players.get(0));
		boards.get(3).put(25, players.get(0));
		boards.get(3).put(26, players.get(0));
		boards.get(3).put(28, players.get(0));
		boards.get(3).put(29, players.get(0));
		boards.get(3).put(6, players.get(1));
		boards.get(3).put(18, players.get(1));
		boards.get(3).put(22, players.get(1));
		// Board 4
		boardNames.put(4, "Can't move");
		boards.put(4, new HashMap<Integer, Player>());
		boards.get(4).put(1, players.get(0));
		boards.get(4).put(2, players.get(1));
		boards.get(4).put(3, players.get(1));
		boards.get(4).put(4, players.get(1));
		// Board 5
		boardNames.put(5, "Forced backwards");
		boards.put(5, new HashMap<Integer, Player>());
		boards.get(5).put(13, players.get(0));
		boards.get(5).put(14, players.get(1));
		boards.get(5).put(15, players.get(1));
		boards.get(5).put(16, players.get(1));
		// Board 6
		boardNames.put(6, "Instant win");
		boards.put(6, new HashMap<Integer, Player>());
		boards.get(6).put(14, players.get(1));
		boards.get(6).put(15, players.get(1));
		boards.get(6).put(16, players.get(1));
		// Board 7
		boardNames.put(7, "Safe at encampment");
		boards.put(7, new HashMap<Integer, Player>());
		boards.get(7).put(23, players.get(0));
		boards.get(7).put(24, players.get(0));
		boards.get(7).put(25, players.get(0));
		boards.get(7).put(1, players.get(1));
		boards.get(7).put(2, players.get(1));
		boards.get(7).put(26, players.get(1));
		boards.get(7).put(29, players.get(1));
	}

	public void play() {
		System.out.println("Welcome to Senet!");
		while (true) {
			HashMap<Integer, Player> game = chooseGame();
			Board board = new Board(players.get(0), players.get(1), game);
			players.get(0).resetNumberOfAttacks();
			players.get(1).resetNumberOfAttacks();
			if (game != boards.get(0)) {
				players.get(0).setColorsignBlack();
				players.get(1).setColorsignWhite();
				board.addTurn();
				board.print();
			} else {
				choosePlayerNames();
				throwForStarter();
				setNormalBoard();
				board.setBoard(boards.get(0));
				board.addTurn();
				board.print();
				board.moveFromTo(players.get(1), 9, dice.throwSticks(players.get(1)), true);
			}
			while (true) {
				// Turn counter
				board.addTurn();
				// Turns
				if (!board.nextTurn(players.get(0), players.get(1)) || !board.nextTurn(players.get(1), players.get(0))) {
					break;
				}
			}
			if (!playAgain()) {
				return;
			}
		}
	}

	public HashMap<Integer, Player> chooseGame() {
		System.out.println("Would you like to start a normal game? <y/n>");
		while (true) {
			if (sc.hasNextLine()) {
				String stringIntput = sc.nextLine();
				if (stringIntput.equals("y") || stringIntput.equals("Y") || stringIntput.equals("yes")) {
					return null;
				} else if (stringIntput.equals("n") || stringIntput.equals("N") || stringIntput.equals("no")) {
					System.out.println("Choose a board setup to play the game");
					System.out.println("(0) Normal game");
					for (Map.Entry<Integer, String> x : boardNames.entrySet()) {
						System.out.println("(" + x.getKey() + ") " + x.getValue());
					}
					System.out.print("Type the number of the board you want to play >> ");
					while (true) {
						if (sc.hasNextInt()) {
							int intInput = sc.nextInt();
							sc.nextLine();
							if (boards.containsKey(intInput) && intInput > 0) {
								return boards.get(intInput);
							} else if (intInput == 0) {
								return null;
							}
						} else {
							if (sc.hasNextLine()) {
								sc.nextLine();
							}
							System.out.println("Please enter a valid number");
						}
					}
				}
				System.out.println("Please type <y/n>");
			} else {
				if (sc.hasNextLine()) {
					sc.nextLine();
				}
			}
		}
	}

	public void choosePlayerNames() {
		for (int i = 0; i < 2; i++) {
			System.out.print("Enter the name of player " + (i + 1) + ": ");
			while (true) {
				if (sc.hasNextLine()) {
					players.get(i).changeName(sc.nextLine());
					break;
				}
			}
		}
	}

	public void throwForStarter() {
		int playerThrow = 0;
		while (true) {
			// Player take turns throwing.
			for (int i = 0; i < 2; i++) {
				playerThrow = dice.throwSticksBegin(players.get(i));
				// When one player throws 1.
				if (playerThrow == 1) {
					if (i == 1) {
						Collections.reverse(players);
					}
					// Set players Color sign
					players.get(0).setColorsignBlack();
					players.get(1).setColorsignWhite();
					System.out
							.println(players.get(0).getName() + " starts the game as " + players.get(0).getColorsign());
					System.out.println(
							players.get(1).getName() + " is second and plays as " + players.get(1).getColorsign());
					return;
				}
			}
		}
	}

	public void setNormalBoard() {
		boardNames.put(0, "Normal game");
		boards.put(0, new HashMap<Integer, Player>());
		for (int i = 1; i < 10; i++) {
			boards.get(0).put(i, players.get(1));
			i++;
			boards.get(0).put(i, players.get(0));
		}
		boards.get(0).remove(10);
		boards.get(0).put(11, players.get(0));
	}

	public boolean playAgain() {
		System.out.println("\rWould you like to play again? <y/n>");
		while (true) {
			if (sc.hasNextLine()) {
				String intput = sc.nextLine();
				if (intput.equals("y") || intput.equals("Y") || intput.equals("yes")) {
					System.out.println("Awesome!\r");
					return true;
				} else if (intput.equals("n") || intput.equals("N") || intput.equals("no")) {
					System.out.println("Thanks for playing, see you next time! \rGoodbye...");
					return false;
				}
				System.out.println("Please type <y/n>");
			} else {
				if (sc.hasNextLine()) {
					sc.nextLine();
				}
			}
		}
	}
}
