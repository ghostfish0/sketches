import processing.core.PApplet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cell {

	private int state;
	final public static Random rand = new Random();

	// final private static int[] stateMap = {"left", "right", "up", "down"};
	// final private static String[] stateMap = {"█", "◖", "◗", "⯊", "⯋"};
	final private static String[] stateMap = {"a", "b", "c", "d", "e"};
	private static Map<String, Integer> reverseStateMap;
	static {
		reverseStateMap = new HashMap<>();
		for (int i = 0; i < stateMap.length; i++)
			reverseStateMap.put(stateMap[i], i);
	}

	public Cell() { this.state = rand.nextInt(stateMap.length); }
	public Cell(int state) { this.state = state; }

	public int getState() { return this.state; }

	public void setState(int state) { this.state = state; }
	public void setState() { this.state = rand.nextInt(stateMap.length); }

	public void updateState(ArrayList<Cell> neighbors) {}

	public String toString() { return stateMap[this.state]; }

	public static String toString(int state) { return stateMap[state]; }

	public static int toState(String string) { return reverseStateMap.get(string); }

	public void draw(double x, double y, double w, double h) {
		//if (this.toString() == "left")
		//else if (this.toString() == "right")
		//else if (this.toString() == "up")
		//else if (this.toString() == "down")
	}
}
