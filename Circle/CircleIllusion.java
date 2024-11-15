import controlP5.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;
// import processing.opengl.PGraphics2D;
// import com.krab.lazy.*;

public class CircleIllusion extends PApplet {
	final private int width = 500;
	final private int height = 500;
	final private int margin = 40;
	final private float radius = 200;
	final private float cellRadius = 10;
	final private float T = 100f;
	final private float omega = TAU / T;
	final private float k = omega * omega;

	private int n = 100;

	private ControlP5 gui;
	private String xformula = "r * c * c";
	private String yformula = "r * s * c";

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
		protected void move(float t) {
			this.x += vx * t;
			this.y += vy * t;
		}
		protected void accelerate(float ax, float ay, float t) {
			this.vx += ax * t;
			this.vy += ay * t;
		}
		protected void move() { this.move(1); }
		protected void accelerate(PVector a) { accelerate(a, 1); }
		protected void accelerate(PVector a, float t) { accelerate(a.x, a.y, t); }
		protected void accelerate(float ax, float ay) { accelerate(ax, ay, 1); }
		public String toString() { return x + ", " + y; }
	}

	private LinkedList<Particle> particles = new LinkedList<>();

	private static char[] chars = new char[] {'-', '\\', '|', '/'};

	public CircleIllusion() { super(); }

	public void settings() {
		// size(width + margin, height + margin, P2D);
		size(width + margin, height + margin);
	}

	public void setup() {
		noFill();
		background(0, 0, 255);
		gui = new ControlP5(this);
		gui.addTextfield("xformula").setPosition(40, 40).setSize(200, 20).setAutoClear(false).setText(xformula);
		gui.addTextfield("yformula").setPosition(40, 70).setSize(200, 20).setAutoClear(false).setText(yformula);
		gui.addTextfield("n").setPosition(40, 100).setSize(200, 20).setAutoClear(false).setText(n + "");

		addParticles();
	}

	public void addParticles() {
		LinkedList<Particle> p_ = new LinkedList<>();
		try {
			for (int i = 0; i < n; i++) {
				float angle = PI / n * i;
				float sin_ = sin(angle);
				float cos_ = cos(angle);
				Map<String, Double> variables =
				        Map.of("r", (double)radius, "s", (double)sin_, "c", (double)cos_,
				               "a", (double)angle);
				float x = (float)Expression.eval(xformula, variables);
				float y = (float)Expression.eval(yformula, variables);
				float vx = -radius * omega * sin_ * cos_;
				float vy = -radius * omega * sin_ * sin_;
				p_.add(new Particle(x, y, vx, vy));
			}
		} catch (Exception e) {
			return;
		}
		this.particles = p_;
	}

	public void draw() {
		background(128);
		pushMatrix();
		translate(margin / 2, margin / 2);
		translate(this.width / 2, this.height / 2);
		noFill();
		circle(0, 0, 2 * radius);
		updateParticles();
		fill(0, 255, 0);
		drawParticles();
		popMatrix();
	};

	public void drawParticles() {
		processParticles(p -> {
			square(p.x, p.y, cellRadius);
			return true;
		});
	}
	public void updateParticles() {
		processParticles(p -> {
			p.accelerate(-this.k * p.x, -this.k * p.y);
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

	public void xformula(String s) {
		this.xformula = s;
		addParticles();
	}
	public void yformula(String s) {
		this.yformula = s;
		addParticles();
	}
	public void n(String s) {
		try {
			this.n = Integer.parseInt(s);
		} catch (Exception e) {
            return;
		}
		addParticles();
	}

	public static void main(String[] args) { PApplet.runSketch(new String[] {"CircleIllusion"}, new CircleIllusion()); }
}
