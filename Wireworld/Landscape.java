import java.util.ArrayList;
import processing.core.PApplet;

public class Landscape {

	// final private static int[][] neighborsOffset = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
	final private static int[][] neighborsOffset = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

	private Cell[][] landscape;
	private int rows;
	private int cols;
	private int width;
	private int height;
	private int cellWidth;
	private int cellHeight;

	private static PApplet sketch;

	public Landscape(int rows, int cols, PApplet sketch_) { this(rows, cols, 100, 100, sketch_); }
	public Landscape(int rows, int cols, int width, int height, PApplet sketch_) {
		this.landscape = new Cell[rows][cols];
		this.rows = rows;
		this.cols = cols;
		this.cellWidth = width / cols;
		this.cellHeight = height / rows;
		this.width = this.cellWidth * cols;
		this.height = this.cellHeight * rows;
		this.sketch = sketch_;
		reset();
	}

	public Landscape(int[][] grid) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(grid[row][col], this.sketch);
			}
		}
	}

	public void resetCannals() {
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				if (row - 1 < 0 || col - 1 < 0) {
					this.landscape[row][col] =
					        new Cell(Cell.rand.nextInt(4), this.sketch);
					continue;
				}
				if (this.landscape[row - 1][col - 1].toString() == "l") {
					this.landscape[row][col] =
					        new Cell(Cell.rand.nextInt(4), this.sketch);
					continue;
				}
				this.landscape[row][col] = new Cell(Cell.rand.nextInt(3), this.sketch);
			}
		}
	}

	public void resetRandom() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(Cell.rand.nextInt(4), this.sketch);
			}
		}
	}

	public void reset() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(0, this.sketch);
			}
		}
	}

	public void setCellState(double x, double y) {
		int row = (int)Math.floor(x * getRows());
		int col = (int)Math.floor(y * getCols());
		if (row < 0 || row >= getRows() || col < 0 || col > getCols())
			return;
		this.landscape[row][col].reset();
	}

	public int getRows() { return this.rows; }

	public int getCols() { return this.cols; }

	public int getWidth() { return this.width; }

	public int getHeight() { return this.height; }

	public Cell cellAt(int row, int col) { return this.landscape[row][col]; }

	public Cell cellAt(double x, double y) {
		int row = rowAt(y);
		int col = colAt(x);
		return this.landscape[row][col];
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

	public void updateCellAt(double x, double y) {
		int row = rowAt(y);
		int col = colAt(x);
		this.landscape[row][col].updateState(getNeighbors(row, col));
	}

	public String toString() {
		String str = "";
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				str += this.landscape[row][col].toString();
			}
			str += "\n";
		}
		return str;
	}

	public ArrayList<Cell> getNeighbors(int row, int col) {
		ArrayList<Cell> neighbors = new ArrayList<>();
		for (int[] offset : neighborsOffset) {
			int row_ = row + offset[0];
			int col_ = col + offset[1];
			if ((row_ < 0 || row_ >= this.rows) || (col_ < 0 || col_ >= this.cols))
				continue;
			neighbors.add(this.landscape[row_][col_]);
		}
		return neighbors;
	}

	public void advance() {
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		// init the temporary, editable landscape
		Cell[][] workLandscape = new Cell[rows][cols];

		// creates a copy of the currentLandscape;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				workLandscape[row][col] =
				        new Cell(this.landscape[row][col].getState(), sketch);
			}
		}
		// update the temporary work landscape
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				workLandscape[row][col].updateState(getNeighbors(row, col));
			}
		}
		// update the current landscape
		this.landscape = workLandscape;
	}

	public void rotate() {
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				this.landscape[row][col].rotate();
			}
		}
	}

	public static void offset(int[] arr, int k) {
		for (int i = 0; i < arr.length; i++)
			arr[i] += k;
	}
	public static void scale(int[] arr, int k) {
		for (int i = 0; i < arr.length; i++)
			arr[i] *= k;
	}

	public static void drawChars(String s, int[] x, int[] y) {}

	public void drawGrid() {
		for (int i = 0; i <= this.rows; i++) {
			sketch.line(0, i * cellHeight, this.width, i * cellHeight);
		}
		for (int i = 0; i <= this.cols; i++) {
			sketch.line(i * cellWidth, 0, i * cellWidth, this.height);
		}
	}

	public void drawScape() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				int x = j * cellWidth;
				int y = i * cellHeight;
				this.landscape[i][j].draw(x, y, cellWidth, cellHeight);
			}
		}
	}

	public void draw() {
		sketch.stroke(128);
		sketch.strokeWeight(1);
		drawGrid();
		sketch.noStroke();
		sketch.fill(40);
		drawScape();
	}
}
