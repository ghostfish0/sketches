import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import processing.core.PApplet;

public class Cell {
	private static PApplet sketch;
	final public static Random rand = new Random();

	private int state;

	final private static String[] stateMap = {"f", "l", "r", "u", "d"};
	private static Map<String, Integer> reverseStateMap;
	static {
		reverseStateMap = new HashMap<>();
		for (int i = 0; i < stateMap.length; i++)
			reverseStateMap.put(stateMap[i], i);
	}

	public Cell(int state, PApplet sketch_) {
		this.state = state;
		sketch = sketch_;
	}

    public void reset() { this.state = rand.nextInt(stateMap.length); }

	public int getState() { return this.state; }

    public void setState(int state) { this.state = state; }

	public void updateState(ArrayList<Cell> neighbors) {}

	public String toString() { return stateMap[this.state]; }

	public static String toString(int state) { return stateMap[state]; }

	public static int toState(String string) { return reverseStateMap.get(string); }

	public void draw(float x, float y, float w, float h) {
		switch (this.toString()) {
		case "f":
			sketch.rect(x, y, w, h);
			break;
		case "l":
			sketch.arc(x + w, y + h / 2, w, h, sketch.HALF_PI, sketch.PI + sketch.HALF_PI);
			break;
		case "r":
			sketch.arc(x, y + h / 2, w, h, sketch.PI + sketch.HALF_PI, sketch.TAU + sketch.HALF_PI);
			break;
		case "u":
			sketch.arc(x + w / 2, y + h, w, h, sketch.PI, sketch.TAU);
			break;
		case "d":
			sketch.arc(x + w / 2, y, w, h, 0, sketch.PI);
			break;
		default:
			break;
		}
	}
}
