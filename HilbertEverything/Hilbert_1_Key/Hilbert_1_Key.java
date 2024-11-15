import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;
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
    final private int margin = 10;
	private SegmentTreeImage smsm;
	private PImage img;

	private int level = 2;

	private static int[][][] ktable = new int[20][1 << 10][1 << 10];
	private static Cell[][] ctable = new Cell[20][1];

	public static int getRows(int lv) { return 1 << (lv + 1); }
	public static int getCols(int lv) { return getRows(lv); }

	public void settings() { size(width + margin, height + margin); }

	public void setup() {
        noFill();
		background(128);
		getKeys();
		img = loadImage("./DP205532.jpg");
		img.resize(width, height);
		smsm = new SegmentTreeImage(img, this);
		smsm.drawHilbert(7);
	}

	public void draw() {
		background(128);
        translate(margin / 2, margin / 2);
		this.level = (int)Math.floor(8.0 * mouseX / width) % 8 + 1;
		smsm.drawHilbert(level);
	};

	public void drawHilbertCurve(int level) {
		int rows = getRows(level);
		int cols = getCols(level);
		float cellWidth = (float)this.width / cols;
		float cellHeight = (float)this.height / rows;

		for (int i = 0; i < ctable[level].length - 1; i++) {
			float x1 = ctable[level][i].row * cellHeight + cellHeight / 2;
			float y1 = ctable[level][i].col * cellWidth + cellWidth / 2;
			float x2 = ctable[level][i + 1].row * cellHeight + cellHeight / 2;
			float y2 = ctable[level][i + 1].col * cellWidth + cellWidth / 2;
			line(x1, y1, x2, y2);
		}
	}

	public static int getKey(int r, int c, int level) {
		int rows = getRows(level);
		int cols = getCols(level);

		if (level < 0) {
			return -1;
		}
		if (r < 0 || r >= rows) {
			return -1;
		}
		if (c < 0 || c >= cols) {
			return -1;
		}
		if (ktable[level][r][c] > 0)
			return ktable[level][r][c];

		int key = 0;
		int mask = 1 << (level);
		int si = 0;
		for (int i = 0; i < level + 1; i++) {
			key <<= 2;
			int ir = (r & mask) > 0 ? 1 : 0;
			int ic = (c & mask) > 0 ? 1 : 0;
			int ci = (ir << 1) | ic;
			key |= htable[si][ci][1];
			si = htable[si][ci][0];

			mask >>= 1;
		}

		ktable[level][r][c] = key;
		return key;
	}

	public void getKeys() {
		for (int level = 0; level < 9; level++) {
			int rows = getRows(level);
			int cols = getCols(level);
			ctable[level] = new Cell[rows * cols];
			int index = 0;
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					ctable[level][index++] =
					        new Cell(r, c, getKey(r, c, level));
				}
			}
			Arrays.sort(ctable[level], (a, b) -> Integer.compare(a.value, b.value));
		}
	}

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Hilbert_1_Key"}, new Hilbert_1_Key()); }
}
