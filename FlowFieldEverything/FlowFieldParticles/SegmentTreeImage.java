import java.util.ArrayList;
import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PImage;

public class SegmentTreeImage extends SegmentTree2D {
	private static PApplet sketch;
	private int width;
	private int height;
	private long[][] brightness;
    private int rows;
    private int cols;
    private float cellWidth;
    private float cellHeight;
	SegmentTree2D tBrightness;

	public SegmentTreeImage(PImage img, PApplet sketch) {
		super(); // Call to the superclass constructor
		this.sketch = sketch;
		this.width = img.width;
		this.height = img.height;
		this.brightness = imgProcess(img, c -> (int)sketch.brightness(c));
		this.tBrightness = new SegmentTree2D(this.brightness);
	}

    public float queryBrightness(int rr, int cc, int rR, int cC) {
        long cnt = (rR - rr + 1) * (cC - cc + 1);
        return (float) this.tBrightness.query(rr, cc, rR, cC) / cnt;
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
	//private float stdev2(int x1, int y1, int x2, int y2) {
	//	// sum of (xi - u)^2  =
	//	// sum of (xi - sum(xi) / n)^2
	//	// sum of (xi^2 - 2 * xi * sum(xi) / n + sum^2(xi) / n^2)
	//	// sum(xi^2)    - 2 * sum^2(xi) / n + sum^2(xi) / n
	//	// sum(xi^2)    - sum^2(xi) / n
	//	int cnt = (x2 - x1 + 1) * (y2 - y1 + 1);
	//	if (cnt == 0)
	//		return 0;
	//
	//	long sum = tBrightness.query(x1, y1, x2, y2);
	//	long sum2 = tBrightness2.query(x1, y1, x2, y2);
	//
	//	double variance = (double)sum2 / cnt - (sum / cnt) * (sum / cnt);
	//
	//	return (float)variance;
	//}
}
