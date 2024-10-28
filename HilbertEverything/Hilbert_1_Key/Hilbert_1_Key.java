import java.util.Arrays;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class Hilbert_1_Key extends PApplet {
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
	final private int level = 2;
	final private int rows = getRows(this.level);
	final private int cols = getCols(this.level);
	final private int cellWidth = this.width / this.cols;
	final private int cellHeight = this.height / this.rows;

	private static int[][][] ktable = new int[20][1 << 10][1 << 10];

	private static int getRows(int lv) { return 1 << (lv + 1); }
	private static int getCols(int lv) { return getRows(lv); }

	public void settings() { size(width, height); }

	public void setup() {
		background(128);
		drawHilbert();
	}

	public void draw(){
        //background(128);
        //int lv = (mouseX / width * 10) % 10;
        //drawHilbert(0);
    };

	public void drawHilbert() {
		Cell[] hkeys = new Cell[this.rows * this.cols];
		int index = 0;
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				hkeys[index++] = new Cell(r, c, getKey(this.level, r, c));
			}
		}
        Arrays.sort(hkeys, (a, b) -> Integer.compare(a.value, b.value));
        for(Cell c: hkeys) {
            System.out.println(c);
        }
		for (int i = 0; i < hkeys.length - 1; i++) {
			int x1 = hkeys[i].row * cellHeight + cellHeight / 2;
			int y1 = hkeys[i].col * cellWidth + cellWidth / 2;
			int x2 = hkeys[i + 1].row * cellHeight + cellHeight / 2;
			int y2 = hkeys[i + 1].col * cellWidth + cellWidth / 2;
			line(x1, y1, x2, y2);
		}
	}

	public static int getKey(int level, int r, int c) {
		if (level < 0)
			return -1;
		if (r < 0 || r > getRows(level))
			return -1;
		if (c < 0 || c > getCols(level))
			return -1;
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

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Hilbert_1_Key"}, new Hilbert_1_Key()); }
}
