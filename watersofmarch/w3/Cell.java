import java.util.ArrayList;

public class Cell {

	private boolean alive;

	public Cell() { this(false); }

	public Cell(boolean status) { this.alive = status; }

	public boolean getAlive() { return this.alive; }

	
	public void setAlive(boolean status) { this.alive = status; }

	
	public void updateState(ArrayList<Cell> neighbors) {
		int sum = neighbors.stream().mapToInt(i -> i.getAlive() ? 1 : 0).sum();
		if (this.alive)
			this.alive = (sum == 2 || sum == 3);
		else
			this.alive = (sum == 3);
	}

	
	public String toString() { return (this.alive ? "1" : "0"); }
	public String toStringPretty() { return (this.alive ? "█" : "░"); }
}
