import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class Quadrant_0 extends PApplet {
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

	private int[][] b = new int[width][height];

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public void settings() { size(width, height); }

	public void setup() {
        background(0);
		PImage img = loadImage("./image.jpg");
		img.resize(width, height);
		img.loadPixels();
		//{
		//	int[][][] rgb = new int[width][height][3];
		//
		//	for (int x = 0; x < img.width; x++) {
		//		for (int y = 0; y < img.height; y++) {
		//			int pixelColor = img.pixels[y * img.width + x];
		//			rgb[x][y][0] = (int)red(pixelColor);
		//			rgb[x][y][0] *= rgb[x][y][0];
		//			rgb[x][y][1] = (int)green(pixelColor);
		//			rgb[x][y][1] *= rgb[x][y][1];
		//			rgb[x][y][2] = (int)blue(pixelColor);
		//			rgb[x][y][2] *= rgb[x][y][2];
		//		}
		//	}
		//	for (int x = 0; x < img.width; x++) {
		//		for (int y = 0; y < img.height; y++) {
		//			for (int c = 0; c < 3; c++) {
		//				rgb[x][y][c] = (int)Math.round(
		//				        Math.sqrt(rgb[x][y][c]));
		//			}
		//		}
		//	}
		//	for (int x = 0; x < img.width; x++) {
		//		for (int y = 0; y < img.height; y++) {
		//			set(x, y, color(rgb[x][y][0], rgb[x][y][1], rgb[x][y][2]));
		//		}
		//	}
		//}
		{
            b = new int[img.width][img.height];
			for (int x = 0; x < img.width; x++) {
				for (int y = 0; y < img.height; y++) {
					int pixelColor = img.pixels[y * img.width + x];
					b[x][y] = (int)brightness(pixelColor);
				}
			}
			SegmentTree2D smsm = new SegmentTree2D(b, this);
            background(0, 255, 0);
            smsm.queryDraw();
		}
	}

	public void draw(){};

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Quadrant_0"}, new Quadrant_0()); }
}
