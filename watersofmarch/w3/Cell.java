import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.geom.Arc2D

public class Cell {

	private int state;
	//final private static int[] stateMap = {"left", "right", "up", "down"};
    final private static int[] stateMap = {"◖", "◗", "⯊", "⯋"};
	private static Map<String, Integer> reverseStateMap;

	public Cell() { this(false); }
	public Cell(int state) {
		this.alive = state;
		for (int i = 0; i < stateMap.length; i++)
			reverseStateMap.put(stateMap[i], i);
	}

	public int getState() { return this.state; }

	public void setState(int state) { this.state = state; }

	public void updateState(ArrayList<Cell> neighbors) {}

	public String toString() { return stateMap[this.state]; }

	public static String toString(int state) { return stateMap[state]; }

	public static int toState(String string) { return reverseStateMap.get(string); }

	public void draw(Graphics g) {
        //if (this.toString() == "left")
        //    g.draw(Arc2D.Double(x, y, w, h, 90, 180, Arc2D.OPEN));
        //else if (this.toString() == "right")
        //    g.draw(Arc2D.Double(x, y, w, h, 90, -180, Arc2D.OPEN));
        //else if (this.toString() == "up")
        //    g.draw(Arc2D.Double(x, y, w, h, 0, -180, Arc2D.OPEN));
        //else if (this.toString() == "down") 
        //    g.draw(Arc2D.Double(x, y, w, h, 0, -180, Arc2D.OPEN));
	}
}
