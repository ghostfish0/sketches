import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Landscape {

	/**
	 * The underlying grid of Cells for Conway's Game
	 */
	private Cell[][] landscape;
	private Queue<Cell[][]> historicalLandscapes;
	private Random rand;

	/**
	 * The original probability each individual Cell is alive
	 */
	private double initialChance;

	/**
	 * Constructs a Landscape of the specified number of rows and columns.
	 * All Cells are initially dead.
	 *
	 * @param rows    the number of rows in the Landscape
	 * @param columns the number of columns in the Landscape
	 */
	public Landscape(int rows, int columns) { this(rows, columns, 0); }

	/**
	 * Constructs a Landscape of the specified number of rows and columns.
	 * Each Cell is initially alive with probability specified by chance.
	 *
	 * @param rows    the number of rows in the Landscape
	 * @param columns the number of columns in the Landscape
	 * @param chance  the probability each individual Cell is initially alive
	 */
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

	/**
	 * Constructs a Landscape using a predefined pattern as boolean grid
	 *
	 * @param grid    the pre-defined grid
	 */
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

	/**
	 * Constructs a Landscape using a predefined pattern as an array of String
	 *
	 * @param grid    the pre-defined grid
	 */
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

	/**
	 * Recreates the Landscape according to the specifications given
	 * in its initial construction.
	 */
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

	/**
	 * Returns the number of rows in the Landscape.
	 *
	 * @return the number of rows in the Landscape
	 */
	public int getRows() { return this.landscape.length; }

	/**
	 * Returns the number of columns in the Landscape.
	 *
	 * @return the number of columns in the Landscape
	 */
	public int getCols() { return this.landscape[0].length; }

	/**
	 * Returns the Cell specified the given row and column.
	 *
	 * @param row the row of the desired Cell
	 * @param col the column of the desired Cell
	 * @return the Cell specified the given row and column
	 */
	public Cell getCell(int row, int col) { return this.landscape[row][col]; }

	/**
	 * Returns a String representation of the Landscape.
	 */
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

	/**
	 * Returns an ArrayList of the neighboring Cells to the specified location.
	 *
	 * @param row the row of the specified Cell
	 * @param col the column of the specified Cell
	 * @return an ArrayList of the neighboring Cells to the specified location
	 */
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

	/**
	 * Advances the current Landscape by one step.
	 */
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

	/**
	 * Set the Cell equivalent to point (x,y) to Alive
	 *
	 * @param x The x (horizontal) position relative to the window's width, range from 0.0 to 1.0
	 * @param y The y (vertical) position relative to the window's height, range from 0.0 to 1.0
	 */
	public void setCellAlive(double x, double y) {
		int row = (int)Math.floor(x * getRows());
		int col = (int)Math.floor(y * getCols());
		if (row < 0 || row >= getRows() || col < 0 || col > getCols())
			return;
		this.landscape[row][col].setAlive(true);
	}

	/**
	 * Count how many alive cells are there on the board
	 *
	 * @return The number of alive cells on the board
	 */
	public int getSum() {
		return Arrays.stream(this.landscape).flatMap(Arrays::stream).mapToInt(i -> i.getAlive() ? 1 : 0).sum();
	}

	/**
	 * Helper function to draw a given Cell[][] grid
	 *
	 * @param g the Java Graphics to draw to
	 * @param scale size of each cell
	 * @param scape the Cell[][] grid to be drawn
	 * @param color color of the scape
	 */
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

	/**
	 * Draws the Cell to the given Graphics object at the specified scale.
	 * An alive Cell is drawn with a black color; a dead Cell is drawn gray.
	 *
	 * @param g     the Graphics object on which to draw
	 * @param scale the scale of the representation of this Cell
	 */
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
