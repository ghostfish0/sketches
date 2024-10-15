import java.util.ArrayList;
import processing.core.PApplet;

public class Landscape {

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
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.cols = cols;
		this.cellWidth = this.width / cols;
		this.cellHeight = this.height / rows;
		this.sketch = sketch_;
		reset();
	}

	public Landscape(int[][] grid) {
		int rows = grid.length;
		int cols = grid[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(grid[row][col], this.sketch);
			}
		}
	}

	public void reset() {
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(Cell.rand.nextInt(5), this.sketch);
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

	public int getRows() { return this.landscape.length; }

	public int getCols() { return this.landscape[0].length; }

	public Cell getCell(int row, int col) { return this.landscape[row][col]; }

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
		int[] offsets = {-1, 0, 1}; // clockwise
		for (int x : offsets) {
			for (int y : offsets) {
				if (x == 0 && y == 0)
					continue;
				int nRow = row + x;
				int nCol = col + y;
				if ((nRow < 0 || nRow >= this.landscape.length) ||
				    (nCol < 0 || nCol >= this.landscape[0].length))
					continue;
				neighbors.add(this.landscape[nRow][nCol]);
			}
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
				// workLandscape[row][col] = new Cell(this.landscape[row][col].getState());
			}
		}
		// update the temporary work landscape
		// for (int row = 0; row < rows; row++) {
		//	for (int col = 0; col < cols; col++) {
		//		workLandscape[row][col].updateState();
		//	}
		//}
		// update the current landscape
		this.landscape = workLandscape;
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
