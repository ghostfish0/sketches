import java.util.ArrayList;
import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PImage;

public class SegmentTreeImage extends SegmentTree2D {
	private static PApplet sketch;
	private int width;
	private int height;
	private int level;
	private long[][] brightness;
	private long[][] brightness2;
    private int rows;
    private int cols;
    private float cellWidth;
    private float cellHeight;
	SegmentTree2D tBrightness;
	SegmentTree2D tBrightness2;

	public SegmentTreeImage(PImage img, PApplet sketch) {
		super(); // Call to the superclass constructor
		this.sketch = sketch;
		this.width = img.width;
		this.height = img.height;
		this.brightness = imgProcess(img, c -> (int)sketch.brightness(c));
		this.brightness2 = imgProcess(img, c -> (int)squared(sketch.brightness(c)));
		this.tBrightness = new SegmentTree2D(this.brightness);
		this.tBrightness2 = new SegmentTree2D(this.brightness2);
        this.setLevel(7);
	}

    public void setLevel(int level) {
        this.level = level;
		this.rows = Hilbert_1_Key.getRows(this.level);
		this.cols = Hilbert_1_Key.getCols(this.level);
		this.cellWidth = 1f * this.width / cols;
		this.cellHeight = 1f * this.height / rows;
    }

	public void drawHilbert(int lv) {
        setLevel(lv);
		ArrayList<Cell> cells = new ArrayList<>();

		drawHilbert(1, 0, 0, rows - 1, cols - 1, 0, cells);

		cells.sort((c1, c2) -> Integer.compare(c1.value, c2.value));

		for (int i = 0; i < cells.size() - 1; i++) {
			Cell head = cells.get(i);
			Cell tail = cells.get(i + 1);
			if (head.value == -1)
				continue;
			if (head.value == tail.value)
				continue;
			float y1 = head.row * cellHeight + cellHeight / 2;
			float x1 = head.col * cellWidth + cellWidth / 2;
			float y2 = tail.row * cellHeight + cellHeight / 2;
			float x2 = tail.col * cellWidth + cellWidth / 2;
			sketch.line(x1, y1, x2, y2);
		}
	}

	private void drawHilbert(int v, int r1, int c1, int r2, int c2, int lv, ArrayList<Cell> output) {
		if (lv > this.level || r1 > r2 && c1 > c2)
			return;
		if (lv == this.level || r2 == r1 || c2 == c1) {
			for (int r = r1; r <= r2; r++) {
				for (int c = c1; c <= c2; c++) {
					output.add(new Cell(r, c, getKey(r, c, this.level)));
				}
			}
			return;
		}
		int y1 = (int) Math.floor(r1 * cellHeight + cellHeight / 2);
		int x1 = (int) Math.floor(c1 * cellWidth + cellWidth / 2);
		int y2 = (int) Math.floor(r2 * cellHeight + cellHeight / 2);
		int x2 = (int) Math.floor(c2 * cellWidth + cellWidth / 2);
		float results = stdev2(x1, y1, x2 - 1, y2 - 1);
		if (results < 500) {
			output.add(new Cell(r1, c1, getKey(r1, c1, this.level)));
			output.add(new Cell(r1, c2, getKey(r1, c2, this.level)));
			output.add(new Cell(r2, c1, getKey(r2, c1, this.level)));
			output.add(new Cell(r2, c2, getKey(r2, c2, this.level)));
			return;
		}
		int rm = (r1 + r2) / 2;
		int cm = (c1 + c2) / 2;
		drawHilbert(4 * v, r1, c1, rm, cm, lv + 1, output);
		drawHilbert(4 * v + 1, r1, cm + 1, rm, c2, lv + 1, output);
		drawHilbert(4 * v + 2, rm + 1, c1, r2, cm, lv + 1, output);
		drawHilbert(4 * v + 3, rm + 1, cm + 1, r2, c2, lv + 1, output);
	}

	private static long[][] imgProcess(PImage img, Function<Integer, Integer> func) {
		long[][] result = new long[img.width][img.height];
		for (int x = 0; x < img.width; x++) {
			for (int y = 0; y < img.height; y++) {
				int pixelColor = img.pixels[y * img.width + x];
				result[x][y] = func.apply(pixelColor);
			}
		}
		return result;
	}
	private float stdev2(int x1, int y1, int x2, int y2) {
		// sum of (xi - u)^2  =
		// sum of (xi - sum(xi) / n)^2
		// sum of (xi^2 - 2 * xi * sum(xi) / n + sum^2(xi) / n^2)
		// sum(xi^2)    - 2 * sum^2(xi) / n + sum^2(xi) / n
		// sum(xi^2)    - sum^2(xi) / n
		int cnt = (x2 - x1 + 1) * (y2 - y1 + 1);
		if (cnt == 0)
			return 0;

		long sum = tBrightness.query(x1, y1, x2, y2);
		long sum2 = tBrightness2.query(x1, y1, x2, y2);

		double variance = (double)sum2 / cnt - (sum / cnt) * (sum / cnt);

		return (float)variance;
	}
	private static int getKey(int r, int c, int level) { return Hilbert_1_Key.getKey(r, c, level); }
}
