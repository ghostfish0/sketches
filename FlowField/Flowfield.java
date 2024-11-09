import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.function.BiFunction;
import processing.event.MouseEvent;

public class Flowfield extends PApplet {
	final private int width = 500;
	final private int height = 500;
	final private int margin = 40;
	final private int rows = 50;
	final private int cols = 50;
	final private float cellWidth = (float)this.width / this.cols;
	final private float cellHeight = (float)this.height / this.rows;

	private PVector[][] grid = new PVector[rows][cols];

	private static char[] chars = new char[] {'-', '\\', '|', '/'};

	public Flowfield() {
		super();
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				grid[r][c] = new PVector(.99f, .99f);
			}
		}
	}

	public void drawField() {
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				float x1 = c * cellWidth;
				float y1 = r * cellHeight;
				line(x1, y1, x1 + grid[r][c].x * cellWidth, y1 + grid[r][c].y * cellHeight);
			}
		}
	}

	public void textField() {
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				float x1 = c * cellWidth;
				float y1 = r * cellHeight;
				int index = (int) Math.floor((grid[r][c].heading() + TAU + QUARTER_PI) / TAU * 8) % 4;
                if (index < 0) {
                    System.out.println(grid[r][c].heading());
                    return;
                }
				char ch = chars[index];
				text(ch, x1, y1);
			}
		}
	}

    public void updateField(int r1, int c1, int r2, int c2, BiFunction<Integer, Integer, Float> f) {
        for (int r = r1; r < r2; r++) {
            for (int c = c1; c < c2; c++) {
                f.apply(r, c);
            }
        }
    }
	public void updateField(BiFunction<Integer, Integer, Float> f) {
        updateField(0, 0, this.rows, this.cols, f);
	}

	public void settings() { size(width + margin, height + margin); }

	public void setup() {
		noFill();
		background(0, 0, 255);
        updateField((r, c) -> {
                float y = (float) 2 * r / this.rows;
                float x = (float) 2 * c / this.cols;
                grid[r][c].rotate(-grid[r][c].heading());
				grid[r][c].rotate(noise(x, y) * TAU);
                return 0f;
        });
	}

	public void draw() {
		background(128);
		translate(margin / 2, margin / 2);
        updateField((r, c) -> {
                grid[r][c].rotate((float) mouseX / this.width * PI);
                return null;
        });
		drawField();
	};

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Flowfield"}, new Flowfield()); }
}
