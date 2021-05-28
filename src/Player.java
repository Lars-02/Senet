
public class Player {
	private String name;
	private String colorsign;
	private int numberOfAttacks;

	public Player(String setName) {
		name = setName;
	}

	public void changeName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setColorsignBlack() {
		colorsign = "X";
	}

	public void setColorsignWhite() {
		colorsign = "O";
	}

	public String getColorsign() {
		if (colorsign.equals("X") || colorsign.equals("O")) {
			return colorsign;
		} else {
			System.out.println("Error: Colorsign is invalid.");
			return "#";
		}
	}

	public void resetNumberOfAttacks() {
		numberOfAttacks = 0;
	}

	public void addAttack() {
		numberOfAttacks++;
	}

	public int getNumberOfAttacks() {
		return numberOfAttacks;
	}
}
