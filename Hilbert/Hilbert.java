import processing.core.PApplet;
import processing.event.MouseEvent;

public class Hilbert extends PApplet {
	final private int width = 500;
	final private int height = 500;
	final private int rows = 20;
	final private int column = 20;

	// private Hilbert hilly = new Hilbert(rows, column);

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Hilbert"}, new Hilbert()); }

	public void settings() { size(width, height); }

	public void setup() {        background(0);
        drawHilbert(3, width, height);
}

	public void drawHilbert(int level, int width, int height) {
		rect(0, 0, width, height);
		if (level == 0) {
			line(width / 4, 3 * height / 4, width / 4, height / 4);
			line(width / 4, height / 4, 3 * width / 4, height / 4);
			line(3 * width / 4, height / 4, 3 * width / 4, 3 * height / 4);
			return;
		}
		drawHilbert(level - 1, width / 2, height / 2);

		translate(width / 2, 0);
		drawHilbert(level - 1, width / 2, height / 2);

		translate(-width / 2, height / 2);
        pushMatrix();
		translate(width / 2, 0);
		rotate(HALF_PI);
		drawHilbert(level - 1, width / 2, height / 2);
        popMatrix();

		translate(width / 2, 0);
        pushMatrix();
        translate(0, height / 2);
        rotate(PI + HALF_PI);
		drawHilbert(level - 1, width / 2, height / 2);
        popMatrix();

		translate(-width / 2, -height / 2);
	}

	public void draw() {
	};
}
