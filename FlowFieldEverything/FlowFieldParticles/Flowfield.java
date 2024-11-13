import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Flowfield extends PApplet {
	final private int width = 500;
	final private int height = 500;
	final private int margin = 40;
	final private int rows = 50;
	final private int cols = 50;
	final private float cellWidth = (float)this.width / this.cols;
	final private float cellHeight = (float)this.height / this.rows;

	public class Particle {
		public float x;
		public float y;
		public float vx;
		public float vy;
		public Particle(float x, float y, float vx, float vy) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
		}
		public Particle(PVector p, PVector v) { this(p.x, p.y, v.x, v.y); }
		protected void move() { this.move(1); }
		protected void move(float t) {
			this.x += vx * t;
			this.y += vy * t;
		}
		protected void accelerate(PVector a, float t) { accelerate(a.x, a.y, t); }
		protected void accelerate(float ax, float ay) { accelerate(ax, ay, 1); }
		protected void accelerate(float ax, float ay, float t) {
			this.vx += ax * t;
			this.vy += ay * t;
		}
		protected void accelerate(PVector a) { accelerate(a, 1); }
	}

	private SegmentTreeImage smsm;
	private PImage img;
	private long[][] brightness;
	private PVector g = new PVector(0.5f, 0);
	private PVector[][] field = new PVector[rows][cols];
	private LinkedList<Particle> particles = new LinkedList<>();

	private static char[] chars = new char[] {'-', '\\', '|', '/'};

	public Flowfield() { super(); }

	public void settings() { size(width + margin, height + margin); }

	public void setup() {
		noFill();
		img = loadImage("./DP205532.jpg");
		img.resize(width, height);
		smsm = new SegmentTreeImage(img, this);
		updateField();
		background(0, 0, 255);
	}

	public void draw() {
		background(128);
		translate(margin / 2, margin / 2);
		image(img, 0, 0);

		updateField();
		drawField();

		fill(0, 255, 0);
		updateParticles();
		drawParticles();
		if (frameCount % 30 == 0) {
			for (int i = 0; i < 50; i++) {
				this.particles.add(new Particle(0, i * cellHeight, 0, 0));
			}
		}
	};

	public void mouseDragged() { mousePressed(); }
	public void mousePressed() { this.particles.add(new Particle(mouseX - margin / 2, mouseY - margin / 2, 0, 0)); }

	public void drawParticles() {
		processParticles(p -> {
			square(p.x, p.y, 5);
			line(p.x + 2, p.y + 2, p.x + p.vx + 2, p.y + p.vy + 2);
			return true;
		});
	}
	public void updateParticles() {
		processParticles(p -> {
			int r = (int)Math.floor(this.rows * p.y / this.width - 0.5f);
			int c = (int)Math.floor(this.rows * p.x / this.height - 0.5f);
			if ((r >= -5 && r < this.rows + 5) && (c >= -5 && c < this.cols + 5)) {
				if (!(r < 0 || r >= this.rows || c < 0 || c >= this.cols)) {
					p.accelerate(this.field[r][c]);
				}
                else {
                    p.accelerate(1, 0);
                }
			}
			p.move();
			return true;
		});
	}

	public void processParticles(Function<Particle, Boolean> f) {
		ListIterator iterator = this.particles.listIterator();
		while (iterator.hasNext()) {
			Particle p = (Particle)iterator.next();
			if (!f.apply(p)) {
				iterator.remove();
			}
		}
	}

	public void drawField() {
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				float x1 = c * cellWidth;
				float y1 = r * cellHeight;
				if (field[r][c].x < 0)
					stroke(255, 0, 0);
				else
					stroke(0, 0, 255);
				line(x1, y1, x1 + field[r][c].x * cellWidth,
				     y1 + field[r][c].y * cellHeight);
			}
		}
	}

	public void updateField() {
		updateField((r, c) -> {
			int x = (int)Math.floor(c * this.cellWidth);
			int y = (int)Math.floor(r * this.cellHeight);
			int x_ = (int)Math.floor((c + 1) * this.cellWidth);
			int y_ = (int)Math.floor((r + 1) * this.cellHeight);
			float m = smsm.queryBrightness(x, y, x_, y_);
			push();
			fill(m);
			noStroke();
			rect(x, y, cellWidth, cellHeight);
			pop();
			m /= 100;
			m -= .5f;
			m = m < 0 ? (10f * mouseX / this.width) * m : .5f * m;
			field[r][c] = new PVector(m, 0);
			return null;
		});
	}

	public void updateField(int r1, int c1, int r2, int c2, BiFunction<Integer, Integer, Float> f) {
		for (int r = r1; r < r2; r++) {
			for (int c = c1; c < c2; c++) {
				f.apply(r, c);
			}
		}
	}
	public void updateField(BiFunction<Integer, Integer, Float> f) { updateField(0, 0, this.rows, this.cols, f); }

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

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Flowfield"}, new Flowfield()); }
}
