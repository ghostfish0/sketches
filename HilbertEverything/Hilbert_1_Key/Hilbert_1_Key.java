import processing.core.PApplet;
import processing.event.MouseEvent;

public class Hilbert_1_Key extends PApplet {
	final private static int[][][] htable = new int[][][] {
	        {{7, 0}, {0, 1}, {6, 3}, {0, 2}}, //
	        {{1, 2}, {7, 3}, {1, 1}, {6, 0}}, //
	        {{2, 1}, {2, 2}, {4, 0}, {5, 3}}, //
	        {{4, 3}, {5, 0}, {3, 2}, {3, 1}}, //
	        {{3, 3}, {4, 2}, {2, 0}, {4, 1}}, //
	        {{5, 1}, {3, 0}, {5, 2}, {2, 3}}, //
	        {{6, 2}, {6, 1}, {0, 3}, {1, 0}}, //
	        {{0, 0}, {1, 3}, {7, 1}, {7, 2}}  //
	};
	final private int width = 500;
	final private int height = 500;
	final private int rows = 20;
	final private int column = 20;

	public void settings() { size(width, height); }

	public void setup() {
		background(0);
		// drawHilbert(3, width, height);
	}

	public void draw(){};

	public static int getKey(int level, int r, int c) {
		//
		return 0;
	}

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Hilbert_1_Key"}, new Hilbert_1_Key()); }
}
