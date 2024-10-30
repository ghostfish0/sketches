import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PImage;

public class SegmentTreeImage extends SegmentTree2D {
	private static PApplet sketch;
	private int width;
	private int height;
	private long[][] r;
	private long[][] g;
	private long[][] b;
	private long[][] brightness;
	private long[][] brightness2;
	SegmentTree2D tR;
	SegmentTree2D tG;
	SegmentTree2D tB;
	SegmentTree2D tBrightness;
	SegmentTree2D tBrightness2;

	public SegmentTreeImage(PImage img, PApplet sketch) {
		super(); // Call to the superclass constructor
		this.sketch = sketch;
		this.width = img.width;
		this.height = img.height;
		this.r = imgProcess(img, c -> squared((int)sketch.red(c)));
		this.g = imgProcess(img, c -> squared((int)sketch.green(c)));
		this.b = imgProcess(img, c -> squared((int)sketch.blue(c)));
		this.brightness = imgProcess(img, c -> (int)sketch.brightness(c));
		this.brightness2 = imgProcess(img, c -> (int) squared(sketch.brightness(c)));
		this.tR = new SegmentTree2D(this.r);
		this.tG = new SegmentTree2D(this.g);
		this.tB = new SegmentTree2D(this.b);
		this.tBrightness = new SegmentTree2D(this.brightness);
		this.tBrightness2 = new SegmentTree2D(this.brightness2);
	}

	// public void queryDraw() { queryDraw(1, 0, 0, this.width - 1, this.height - 1, 0); }
	public void queryDraw() { queryDraw(1, 0, 0, this.height - 1, this.width - 1, 0, 0, this.height - 1, this.width - 1, 0); }
	public void queryDraw(int r, int c) {
		queryDraw(1, 0, 0, this.height - 1, this.width - 1, 0, 0, r, c, 0);
		queryDraw(1, 0, 0, this.height - 1, this.width - 1, 0, c + 1, r, this.width - 1, 0);
		queryDraw(1, 0, 0, this.height - 1, this.width - 1, r + 1, 0, this.height - 1, c, 0);
		queryDraw(1, 0, 0, this.height - 1, this.width - 1, r + 1, c + 1, this.height - 1, this.width - 1, 0);
	}
	protected void queryDraw(int v, int r1, int c1, int r2, int c2, int rr, int cc, int rR, int cC, int lv) {
		if (cc > cC || rr > rR)
			return;
		// int results = tBrightness.query(v, r1, c1, r2, c2, rr, cc, rR, cC);
		float results = stdev(v, r1, c1, r2, c2, rr, cc, rR, cC);
		// System.out.println(results);
		int cnt = (rR - rr + 1) * (cC - cc + 1);
		// if (results < cnt * (1 << (lv + 1))) {
		if (results < 80.0 * sketch.mouseX / sketch.width + 20) {
			float r_ = (float)Math.sqrt(1f * tR.query(v, r1, c1, r2, c2, rr, cc, rR, cC) / cnt);
			float g_ = (float)Math.sqrt(1f * tG.query(v, r1, c1, r2, c2, rr, cc, rR, cC) / cnt);
			float b_ = (float)Math.sqrt(1f * tB.query(v, r1, c1, r2, c2, rr, cc, rR, cC) / cnt);
			sketch.fill(r_, g_, b_);
			sketch.rect(rr - 1, cc - 1, rR + 1, cC + 1);
		} else {
			int rm = (r1 + r2) / 2;
			int cm = (c1 + c2) / 2;
			queryDraw(v * 4, r1, c1, rm, cm, rr, cc, min(rR, rm), min(cC, cm), lv + 1);
			queryDraw(v * 4 + 1, r1, cm + 1, rm, c2, rr, max(cc, cm + 1), min(rR, rm), cC, lv + 1);
			queryDraw(v * 4 + 2, rm + 1, c1, r2, cm, max(rr, rm + 1), cc, rR, min(cC, cm), lv + 1);
			queryDraw(v * 4 + 3, rm + 1, cm + 1, r2, c2, max(rr, rm + 1), max(cc, cm + 1), rR, cC, lv + 1);
		}
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
	private float stdev(int v, int r1, int c1, int r2, int c2, int rr, int cc, int rR, int cC) {
		// sum of (xi - u)^2  =
		// sum of (xi - sum(xi) / n)^2
		// sum of (xi^2 - 2 * xi * sum(xi) / n + sum^2(xi) / n^2)
		// sum(xi^2)    - 2 * sum^2(xi) / n + sum^2(xi) / n
		// sum(xi^2)    - sum^2(xi) / n
		int cnt = (rR - rr + 1) * (cC - cc + 1);
		if (cnt == 0)
			return 0;

		long sum = tBrightness.query(v, r1, c1, r2, c2, rr, cc, rR, cC);
		long sum2 = tBrightness2.query(v, r1, c1, r2, c2, rr, cc, rR, cC);

		double variance = (double) sum2 / cnt - (sum / cnt) * (sum / cnt);

		return (float)Math.sqrt(variance);
	}

	public void test() {
		int r = Math.abs(sketch.mouseX - width / 2);
		int r1 = width / 2 - r;
		int r2 = width / 2 + r;
		int c1 = width / 2 - r;
		int c2 = width / 2 + r;
		sketch.push();
		sketch.noFill();
		sketch.stroke(0, 0, 255);
		sketch.strokeWeight(5);
		sketch.rect(r1, c1, 2 * r, 2 * r);
		sketch.stroke(255, 0, 0);
		sketch.text(stdev(1, 0, 0, this.height - 1, this.width - 1, r1, c1, r2, c2), width / 2, height / 2);
		// System.out.println();
		sketch.pop();
	}
}
