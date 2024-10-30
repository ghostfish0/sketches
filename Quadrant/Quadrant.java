import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class Quadrant extends PApplet {
	private static class Cell {
		protected int row;
		protected int col;
		protected int value;
		public Cell(int r, int c, int v) {
			this.row = r;
			this.col = c;
			this.value = v;
		}
		public String toString() { return "[" + this.row + ", " + this.col + "] = " + this.value + ";"; }
	}
	final private int width = 500;
	final private int height = 500;

	private SegmentTreeImage smsm;
	private PImage img;
    private int counter = 0;

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public void settings() { size(width, height); }

	public void setup() {
		background(0);
		rectMode(CORNER);
		img = loadImage("./potrait.jpg");
		img.resize(width, height);
		smsm = new SegmentTreeImage(img, this);
        stroke(0, 0, 255);
		//noStroke();
	}

	public void draw() {
		background(0);
		int x = mouseX > width ? width : mouseX < 0 ? 0 : mouseX;
		int y = mouseY > height ? height : mouseY < 0 ? 0 : mouseY;
		smsm.queryDraw();
        if (counter++ % 100 == 0) {
            saveFrame();
        }
	};

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Quadrant"}, new Quadrant()); }
}
