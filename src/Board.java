import java.util.HashMap;
import java.util.Scanner;

public class Board {
	private HashMap<Integer, Player> squares;
	private Scanner sc = new Scanner(System.in);
	private Dice dice = new Dice();
	private int turnCounter;

	public Board(Player startPlayer, Player secondPlayer, HashMap<Integer, Player> board) {
		squares = board;
		turnCounter = 0;
	}

	public void setBoard(HashMap<Integer, Player> board) {
		if (squares == null) {
			squares = board;
		} else {
			System.out.println("Error: Board is already set.");
		}
	}

	// Player that's moving, where piece is moving from, place to move
	// Move the pieces and Messages enable/disabled
	public boolean moveFromTo(Player player, int from, int to, boolean move) {
		// Checking if that piece is of the player
		if (squares.get(from) != player) {
			if (move) {
				System.out.println("There are no allies that can move at " + from);
			}
			return false;
		}
		// Checks if destination is on the board
		if ((from + to) <= 0 || (from + to) > 30) {
			if (move) {
				System.out.println("You can't move to a place outside of the board.");
			}
			return false;
		}
		// Checking if it's necessary to check for blockade
		if (to > 3 || to < -3) {
			// Counter for length of blockade
			int blockade = 0;
			// Check all places until destination
			if (to > 0) {
				for (int i = 0; i < to; i++) {
					if (squares.containsKey(from + i) && squares.get(from + i) != player) {
						blockade++;
					} else if (blockade < 3) {
						// Resets counter
						blockade = 0;
					}
				}
			} else {
				for (int i = 0; i < -to; i++) {
					if (squares.containsKey(from - i) && squares.get(from - i) != player) {
						blockade++;
					} else if (blockade < 3) {
						// Resets counter
						blockade = 0;
					}
				}
			}
			// If 3 or more then it's a blockade
			if (blockade >= 3) {
				if (move) {
					System.out.println("There is an enemy blockade in your way!");
				}
				return false;
			}
		}
		// Checking if the piece lands on 30
		if (from + to == 30) {
			// Checking if all pieces are on the last row
			for (int i = 1; i <= 20; i++) {
				if (squares.get(i) == player) {
					if (move) {
						System.out.println("Not all your allies are on the final row!");
					}
					return false;
				}
			}
			if (move) {
				System.out.println("One of your allies won a battle and left the board. Hurray!");
				squares.remove(from);
				print();
			}
			return true;
		}
		// Checking if player lands on 27 to send them back to start
		if (from + to == 27) {
			if (move) {
				System.out.println("One of your allies fel into an enmey trap!");
				System.out.println("They fell back to a safe position. " + from + " >> " + backToStart(player));
				squares.remove(from);
				print();
			}
			return true;
		}
		// Checking if square is empty and/or player's
		if (squares.containsKey(from + to)) {
			// Checking if piece is hostile
			if (squares.get(from + to) == player) {
				if (move) {
					System.out.println("One of your allies are in the province at " + (from + to));
				}
				return false;
			}
			// Checking if enemy joined forces
			if (squares.get(from + to + 1) == squares.get(from + to)
					|| squares.get(from + to - 1) == squares.get(from + to)) {
				if (move) {
					System.out.println("The enemy has joined forces! You can't attack them at " + (from + to));
				}
				return false;
			}
			// Checking if there is an enemy on 26/28/29
			if (from + to == 26 || from + to == 28 || from + to == 29) {
				if (move) {
					System.out.println("The enemy is safe at " + (from + to) + " because they are in a encampment");
				}
				return false;
			}
			// To switch pieces
			if (move) {
				System.out.println("You attacked an enemy and moved! " + from + " >> " + (from + to));
				System.out.println("The enemy flet and moved. " + (from + to) + " >> " + from);
				squares.put(from, squares.get(from + to));
				squares.put(from + to, player);
				player.addAttack();
				print();
			}
			return true;
		}
		// Move's piece if empty
		if (move) {
			System.out.println("You moved to a new location. " + from + " >> " + (from + to));
			squares.remove(from);
			squares.put(from + to, player);
			print();
		}
		return true;
	}

	public boolean nextTurn(Player player, Player opponent) {
		while (true) {
			int to = dice.throwSticks(player);
			// Checks all forwards moves
			if (!checkTurn(player, to)) {
				// Checks all backwards moves
				if (!checkTurn(player, -to)) {
					// Check for a winner
					if (checkForWinner(player, opponent)) {
						return false;
					}
					System.out.println("Your allies can't move at all, so your turn will pass");
					return true;
				} else {
					System.out.println("Your allies can't move forward, so this turn a ally will move back");
					to = -to;
				}
			}
			// Set up player turn
			while (true) {
				int input = inputFrom(player);
				if (moveFromTo(player, input, to, true)) {
					break;
				}
			}
			// Check for a winner
			if (checkForWinner(player, opponent)) {
				return false;
			}
			// Checks if player needs to throw again
			if (to == 2 || to == 3 || to == -2 || to == -3) {
				System.out.print("You threw ");
				if (to < 0) {
					System.out.print((-to));
				} else {
					System.out.print(to);
				}
				System.out.println(" so you may throw again.");
				return true;
			}
		}
	}

	public int inputFrom(Player player) {
		while (true) {
			System.out
					.print(player.getName() + " (" + player.getColorsign() + "), which ally do you want to move? >> ");
			if (sc.hasNextInt()) {
				int input = sc.nextInt();
				if (input < 30 && input > 0) {
					return input;
				} else {
					System.out.println("Enter a number that one of your allies is standing on");
				}
			} else {
				if (sc.hasNextLine()) {
					sc.nextLine();
				}
				System.out.println("Please try again");
			}
		}
	}

	// Checks if the turn is possible.
	public boolean checkTurn(Player player, int to) {
		for (int i = 1; i < 30; i++) {
			if (moveFromTo(player, i, to, false)) {
				return true;
			}
		}
		return false;
	}

	public int backToStart(Player player) {
		// Checks first empty spot
		int i = 1;
		while (true) {
			if (!squares.containsKey(i)) {
				squares.put(i, player);
				return i;
			}
			i++;
		}
	}

	// Checks if player is the winner if so do end message therefore secondPlayer
	public boolean checkForWinner(Player player, Player opponent) {
		if (!squares.containsValue(player)) {
			System.out.println("\rCongratulations " + player.getName() + " you and your allies won the war!\r");
			int piecesLeft = 0;
			for (int i = 1; i <= 30; i++) {
				if (squares.get(i) == opponent) {
					piecesLeft++;
				}
			}
			System.out.println(opponent.getName() + " is second with " + piecesLeft + " allies left");
			System.out.print("\rThe war lasted " + (turnCounter - 1) + " turn");
			if (turnCounter > 2) {
				System.out.print("s");
			}
			System.out.print(
					"\r\r" + player.getName() + " charged at enemy forces " + player.getNumberOfAttacks() + " time");
			if (player.getNumberOfAttacks() == 1) {
				System.out.print("s");
			}
			System.out.print("\r" + opponent.getName() + " charged at enemy forces "
					+ opponent.getNumberOfAttacks() + " time");
			if (player.getNumberOfAttacks() == 1) {
				System.out.print("s");
			}
			System.out.println();
			return true;
		}
		return false;
	}

	public void print() {
		System.out.print("===================(");
		if (turnCounter < 10) {
			System.out.print("0");
		}
		System.out.print(turnCounter);
		System.out.println(")===================");
		for (int i = 0; i < 30; i += 10) {
			if (i >= 10 && i <= 20) {
				System.out.println("|                                        |");
			}
			System.out.print("|");
			for (int y = 1; y <= 10; y++) {
				int x = 0;
				if (i == 10) {
					x = 21 - y;
				} else {
					x = i + y;
				}
				System.out.print(" ");
				if (x < 10) {
					System.out.print("0");
				}
				System.out.print(x + " ");
			}
			System.out.println("|");
			System.out.print("|");
			for (int y = 1; y <= 10; y++) {
				int x = 0;
				if (i == 10) {
					x = 21 - y;
				} else {
					x = i + y;
				}
				if (squares.containsKey(x)) {
					System.out.print(" " + squares.get(x).getColorsign() + "  ");
				} else {
					System.out.print(" .  ");
				}
			}
			System.out.println("|");
		}
		System.out.println("==========================================");
	}

	// +1 to turns
	public void addTurn() {
		turnCounter++;
	}
}
