import java.util.Random;
import java.util.Scanner;

public class Dice {
	private Scanner sc = new Scanner(System.in);
	
	public int throwSticks(Player player) {
		//I changed throwStick to throwSticks because now you get the entire throw result.
		Random rnd = new Random();
		// Press enter
		pressEnter(player);
		// Count how many times sticks threw white (true)
		int whiteThrowCounter = 0;
		// Throws one stick 4 times
		for (int i = 0; i < 4; i++) {
			if (rnd.nextBoolean()) {
				whiteThrowCounter++;
			}
		}
		// If there are 0 white then its all black (6)
		if (whiteThrowCounter == 0) {
			whiteThrowCounter = 6;
		}
		System.out.println(player.getName() + " (" + player.getColorsign() + "), you have thrown " + whiteThrowCounter);
		return whiteThrowCounter;
	}
	
	public int throwSticksBegin(Player player) {
		//I changed throwStick to throwSticks because now you get the entire throw result.
		Random rnd = new Random();
		int whiteThrowCounter = 0;
		for (int i = 0; i < 4; i++) {
			if (rnd.nextBoolean()) {
				whiteThrowCounter++;
			}
		}
		if (whiteThrowCounter == 0) {
			whiteThrowCounter = 6;
		}
		System.out.println(player.getName() + " has thrown " + whiteThrowCounter);
		return whiteThrowCounter;
	}
	
	public void pressEnter(Player player) {
		System.out.print(player.getName() + " (" + player.getColorsign() + "), press <ENTER> to throw the dice...");
		while (true) {
			if (sc.hasNextLine()) {
				sc.nextLine();
				return;
			}
		}
	}
}
