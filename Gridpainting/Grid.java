import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;

public class Grid {
	protected static PApplet sketch;
	protected static Random rand;

	protected int[][] landscape;
	private int mode; // 0-> normal  1->mouse   2->visual   3->visual block
	private int rows;
	private int cols;
	private int width;
	private int height;
	protected int cellWidth;
	protected int cellHeight;
	private Cursor cursor;

	public Grid(int rows, int cols, PApplet sketch_) { this(rows, cols, 100, 100, sketch_); }
	public Grid(int rows, int cols, int width, int height, PApplet sketch_) {
		this.landscape = new int[rows][cols];
		this.rows = rows;
		this.cols = cols;
		this.mode = 0;
		this.cellWidth = width / cols;
		this.cellHeight = height / rows;
		// tight fit
		this.width = this.cellWidth * cols;
		this.height = this.cellHeight * rows;
		this.sketch = sketch_;
		this.cursor = new Cursor(0, 0);
		this.rand = new Random();
		reset();
	}

	public Grid(int[][] grid) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = 0;
			}
		}
	}

	public void fillRandom(int row1, int col1, int row2, int col2) {
		if (row1 < 0 || row1 >= rows || row2 < 0 || row2 >= rows || col1 < 0 || col1 >= cols || col2 < 0 ||
		    col2 >= cols)
			return;
		for (int row = row1; row < row2; row++) {
			for (int col = col1; col < col2; col++) {
				this.landscape[row][col] = rand.nextInt(256);
			}
		}
	}

	public void fillReset(int row1, int col1, int row2, int col2) {
		if (row1 < 0 || row1 > rows || row2 < 0 || row2 > rows || col1 < 0 || col1 > cols || col2 < 0 ||
		    col2 >= cols)
			return;
		for (int row = row1; row < row2; row++) {
			for (int col = col1; col < col2; col++) {
				this.landscape[row][col] = 0;
			}
		}
	}

	public void reset() { this.fillReset(0, 0, this.rows, this.cols); }

	public void setScreenToCell(double x, double y) {
		int row = (int)Math.floor(x * getRows());
		int col = (int)Math.floor(y * getCols());
		if (row < 0 || row >= getRows() || col < 0 || col > getCols())
			return;
		this.landscape[row][col] = 0;
	}

	public int getRows() { return this.rows; }

	public int getCols() { return this.cols; }

	public int getWidth() { return this.width; }

	public int getHeight() { return this.height; }

	public int getCelWidth() { return this.cellWidth; }

	public int getMode() { return this.mode; }

	public int getCellHeight() { return this.cellHeight; }

	public int getCellAtGrid(int row, int col) { return this.landscape[row][col]; }

	public int getCellAtScreen(double x, double y) {
		int row = rowAt(y);
		int col = colAt(x);
		return this.landscape[row][col];
	}

	public void setCellAtGrid(int row, int col, int state) { this.landscape[row][col] = state; }

	public void setCellAtScreen(double x, double y, int state) {
		int row = rowAt(y);
		int col = colAt(x);
		this.landscape[row][col] = state;
	}

	public int colAt(double k) {
		int col = (int)Math.round(k * this.cols);
		col %= this.cols;
		col = (col >= this.cols ? this.cols - 1 : col);
		col = (col < 0 ? 0 : col);
		return col;
	}
	public int rowAt(double k) {
		int row = (int)Math.round(k * this.rows);
		row = (row >= this.rows ? this.rows - 1 : row);
		row = (row < 0 ? 0 : row);
		return row;
	}

	public int xAt(double k) { return colAt(k) * this.cellWidth; }
	public int yAt(double k) { return rowAt(k) * this.cellHeight; }

	public String toString() {
		String str = "";
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				str += this.landscape[row][col] + " ";
			}
			str += "\n";
		}
		return str;
	}

	public static void offset(int[] arr, int k) {
		for (int i = 0; i < arr.length; i++)
			arr[i] += k;
	}
	public static void scale(int[] arr, int k) {
		for (int i = 0; i < arr.length; i++)
			arr[i] *= k;
	}

	public void drawCursor() {}

	public void drawGrid() {
		for (int i = 0; i <= this.rows; i++) {
			sketch.line(0, i * cellHeight, this.width, i * cellHeight);
		}
		for (int i = 0; i <= this.cols; i++) {
			sketch.line(i * cellWidth, 0, i * cellWidth, this.height);
		}
	}

	public void drawCell(int x, int y) { sketch.rect(x, y, cellWidth, cellHeight); }

	public void drawScape() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if (this.landscape[i][j] != 0)
					this.drawCell(j * cellWidth, i * cellHeight);
			}
		}
	}

	protected class Cursor {
		final Grid grid = Grid.this;
		final PApplet sketch = grid.sketch;
		private int row;
		private int col;
		protected int state;
		public Cursor(int row, int col) { this(row, col, 255); }
		public Cursor(int row, int col, int state) {
			this.row = row;
			this.col = col;
			this.state = state;
		}
		protected int getRow() { return row; }
		protected int getCol() { return col; }
		protected int getX() { return col * grid.cellWidth; }
		protected int getY() { return row * grid.cellHeight; }
        protected int getState() { return state; }
		protected void sticktoMouse() {
			this.row = grid.rowAt((double)(sketch.mouseY - grid.cellHeight / 2) / grid.height);
			this.col = grid.colAt((double)(sketch.mouseX - grid.cellWidth / 2) / grid.width);
		}
        protected void setState() { grid.landscape[this.row][this.col] = this.state; }
		protected void draw() {
			sketch.push();
			sketch.stroke(this.state);
			sketch.noFill();
			sketch.strokeWeight(1);
			grid.drawCell(this.getX(), this.getY());
			sketch.pop();
		}
	}

	public void updateCursor() { this.cursor.sticktoMouse(); }

	public void paintCellAtCursor() {
        this.cursor.setState();
    }

	public void draw() {
		sketch.stroke(128);
		sketch.strokeWeight(1);
		drawGrid();
		sketch.noStroke();
		sketch.fill(40);
		drawScape();
		this.cursor.draw();
	}
}
