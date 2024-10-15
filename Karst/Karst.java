import processing.core.PApplet;

public class Karst extends PApplet {
	private int width = 500;
	private int height = 500;

	// final private static String[] stateMap = {"█", "◖", "◗", "⯊", "⯋"};
	// final private static String[] stateMap = {"a", "b", "c", "d", "e"};
	private Landscape scape = new Landscape(20, 20, width, height, this);

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Karst"}, new Karst()); }

	public void settings() { size(width, height); }

	public void setup() { frameRate(5); }

	public void draw() {
		background(200);
		scape.draw();
		scape.reset();
	}
}
