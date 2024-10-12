import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Landscape {

	
	private Cell[][] landscape;
	private Queue<Cell[][]> historicalLandscapes;
	private Random rand;

	private double initialChance;

	public Landscape(int rows, int columns) { this(rows, columns, 0); }

	public Landscape(int rows, int cols, double chance) {
		this.rand = new Random();
		this.landscape = new Cell[rows][cols];
		this.historicalLandscapes = new LinkedList<>();
		for (int iterator = 0; iterator < 20; iterator++) {
			historicalLandscapes.add(landscape);
		}
		this.initialChance = chance;
		reset();
	}

	public Landscape(boolean[][] grid) {
		this(grid.length, grid[0].length, 0);
		int rows = grid.length;
		int cols = grid[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(grid[row][col]);
			}
		}
	}

	
	public Landscape(String[] grid) {
		this(grid.length, grid[0].length(), 0);
		int rows = grid.length;
		int cols = grid[0].length();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(grid[row].charAt(col) == 'x');
			}
		}
	}

	public void reset() {
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] =
				        new Cell(this.rand.nextDouble() < this.initialChance);
			}
		}
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
				str += this.landscape[row][col].toStringPretty();
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

		// remove the oldest historicalLandscape from the head of the queue
		this.historicalLandscapes.poll();
		// add the latest (current) landscape to the tail of the queue
		this.historicalLandscapes.offer(this.landscape);

		// creates a copy of the currentLandscape;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				workLandscape[row][col] = new Cell(this.landscape[row][col].getAlive());
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

	
	public void setCellAlive(double x, double y) {
		int row = (int)Math.floor(x * getRows());
		int col = (int)Math.floor(y * getCols());
		if (row < 0 || row >= getRows() || col < 0 || col > getCols())
			return;
		this.landscape[row][col].setAlive(true);
	}

	public int getSum() {
		return Arrays.stream(this.landscape).flatMap(Arrays::stream).mapToInt(i -> i.getAlive() ? 1 : 0).sum();
	}

	public void drawScape(Graphics g, int scale, Cell[][] scape, Color color) {
		g.setColor(color);
		for (int x = 0; x < getRows(); x++) {
			for (int y = 0; y < getCols(); y++) {
				if (scape[x][y].getAlive()) {
					g.fillRect(x * scale, y * scale, scale, scale);
				}
			}
		}
	}

	public void draw(Graphics g, int scale) {
		int redness = 0;
		// Copy of historicalLandscapes to avoid For loop modification error
		ArrayList<Cell[][]> historicalLandscapesCopy = new ArrayList<>(historicalLandscapes);
		for (Cell[][] scape : historicalLandscapesCopy) {
			if (scape[0][0] == null)
				continue;
			redness = (redness + 10 < 255 ? redness + 10 : 255);
			Color color = new Color(redness, 0, 0);
			drawScape(g, scale, scape, color);
		}
		drawScape(g, scale, this.landscape, Color.WHITE);
	}

	public static void main(String[] args) {}
}
