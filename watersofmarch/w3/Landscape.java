import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Landscape {

	
	private Cell[][] landscape;
	final static private Random rand = new Random();

	public Landscape(int rows, int cols) {
		this.landscape = new Cell[rows][cols];
		reset();
	}

	public Landscape(boolean[][] grid) {
		int rows = grid.length;
		int cols = grid[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(grid[row][col]);
			}
		}
	}

	public void reset() {
		int rows = this.landscape.length;
		int cols = this.landscape[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.landscape[row][col] = new Cell(this.rand.nextInt(4));
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
				workLandscape[row][col] = new Cell(this.landscape[row][col].getState());
			}
		}
		// update the temporary work landscape
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				workLandscape[row][col].updateState();
			}
		}
		// update the current landscape
		this.landscape = workLandscape;
	}

	
	public void drawScape(Graphics g, int scale, Color color) {
		g.setColor(color);
        g.drawString("" + this, 0, 0);
	}

	public void draw(Graphics g, int scale) {
		drawScape(g, scale, Color.WHITE);
	}
}
