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

	/*
	0. empty (black) →  empty,
	1. electron head (blue) →  tail,
	2. electron tail (red) →  conductor,
	3. conductor (yellow). →  electron head if exactly one or two of the neighbouring cells are electron heads, otherwise
	remains conductor
    */
	final public static String[] stateMap = {"empty", "head", "tail", "conductor"};
	final public static int[][] colorMap = {{0, 0, 0}, {0, 0, 255}, {255, 0, 0}, {255, 255, 0}};

	public Cell(int state, PApplet sketch_) {
		this.state = state;
		sketch = sketch_;
	}

	public void resetRandom() { this.state = rand.nextInt(stateMap.length); }

	public void reset() { this.state = 0; }

	public int getState() { return this.state; }

	public void setState(int state) { this.state = state; }

	public void updateState(ArrayList<Cell> neighbors) {
		switch (this.toString()) {
		case "empty":
			break;
		case "head":
			this.state = 2; // to tail
			break;
		case "tail":
			this.state = 3; // to conductor
			break;
		case "conductor":
			int cnt = 0;
			for (Cell c : neighbors) {
				if (c.toString() == "head")
					cnt++;
			}
			if (cnt == 1 || cnt == 2)
				this.state = 1; // to head
			break;
		default:
			break;
		}
	}

	public void rotate() {
		this.state++;
		this.state %= stateMap.length;
	}

	public String toString() { return stateMap[this.state]; }

	public static String toString(int state) { return stateMap[state]; }

	private void fill(int[] rgb) { sketch.fill(rgb[0], rgb[1], rgb[2]); }

	public void draw(float x, float y, float w, float h) {
		// switch (this.toString()) {
		// case "empty":
		//	sketch.fill(0);
		//	break;
		// case "head":
		//           sketch.fill(0, 0, 255);
		//	break;
		// case "tail":
		//           sketch.fill(255, 0, 0);
		//	break;
		// case "conductor":
		//	sketch.fill(255, 255, 0);
		//	break;
		//       default:
		//           break;
		// }
		fill(colorMap[this.state]);
		sketch.rect(x, y, w, h);
	}
    
    public void drawBorder(int x, int y, int w, int h) {
        sketch.stroke(255);        
        sketch.strokeWeight(5);
        sketch.noFill();
		sketch.rect(x, y, w, h);
    }
}
