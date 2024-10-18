import processing.core.PApplet;

public class Karst extends PApplet {
	final private int width = 500;
	final private int height = 500;
    final private int rows = 40;
    final private int column = 40;

	private Landscape scape = new Landscape(rows, column, width, height, this);

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Karst"}, new Karst()); }

	public void settings() { size(scape.getWidth(), scape.getHeight()); scape.resetCannals(); }

	public void setup() { frameRate(5); }

	public void draw() {
		background(200);
		scape.draw();
	}
}
