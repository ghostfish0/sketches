import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PConstants;

public class Cell {
	private static PApplet sketch;
	final public static Random rand = new Random();

	private int state;

	final private static String[] stateMap = {"u", "l", "d", "r", "f"};
    //final private static String[] stateMap ={0,   1,   2,   3,   4};
    //final private static int[] lClogMap = {"r", "d", };
    final private static int[] lClogMap = {};
	private static Map<String, Integer> rStateMap;
	static {
		rStateMap = new HashMap<>();
		for (int i = 0; i < stateMap.length; i++)
			rStateMap.put(stateMap[i], i);
	}

	public Cell(int state, PApplet sketch_) {
		this.state = state;
		sketch = sketch_;
	}

    public void reset() { this.state = rand.nextInt(stateMap.length); }

	public int getState() { return this.state; }

    public void setState(int state) { this.state = state; }

	public void updateState(ArrayList<Cell> neighbors) {}

    public void rotate() {
        this.state++;
        this.state %= stateMap.length;
    }

	public String toString() { return stateMap[this.state]; }

	public static String toString(int state) { return stateMap[state]; }

	public static int toState(String string) { return rStateMap.get(string); }

	public void draw(float x, float y, float w, float h) {
		switch (this.toString()) {
		case "f":
			sketch.rect(x, y, w, h);
			break;
		case "l":
			sketch.arc(x + w, y + h / 2, w, h, PConstants.HALF_PI, PConstants.PI + PConstants.HALF_PI);
			break;
		case "r":
			sketch.arc(x, y + h / 2, w, h, PConstants.PI + PConstants.HALF_PI, PConstants.TAU + PConstants.HALF_PI);
			break;
		case "u":
			sketch.arc(x + w / 2, y + h, w, h, PConstants.PI, PConstants.TAU);
			break;
		case "d":
			sketch.arc(x + w / 2, y, w, h, 0, PConstants.PI);
			break;
		default:
			break;
		}
	}
}
