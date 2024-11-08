public class Cell {
	public int row;
	public int col;
	public int value;
	public Cell(int r, int c, int v) {
		this.row = r;
		this.col = c;
		this.value = v;
	}
	public String toString() { return "[" + this.row + ", " + this.col + "] = " + this.value + ";"; }
}
