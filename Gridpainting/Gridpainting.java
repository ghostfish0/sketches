import controlP5.*;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class Gridpainting extends PApplet {

	final private int width = 500;
	final private int height = 500;
	final private int rows = 20;

	final private int column = 20;
	// 0: NORMAL
	// 1: MULTI-NORMAL
	// 2: BRUSH
    final private int mode = 0;
    private ControlP5 gui;

	private int lagger = 0;
	private int laggerLimit = 5;

	private int wheelCnt = 0;

	private int brushState = 1;

	private Grid grid = new Grid(rows, column, width, height, this);

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Gridpainting"}, new Gridpainting()); }

	public void settings() { size(grid.getWidth(), grid.getHeight()); }

	public void setup() {
		gui = new ControlP5(this);
		// frameRate(5);
		background(200);
		grid.reset();
		grid.draw();
	}

	public void draw() {
		background(200);
		grid.updateCursor();
		grid.draw();
		// if ((lagger++) % laggerLimit == 0)
		//	grid.advance();
	}

	public void mouseDragged() { grid.paintCellAtCursor(); }

	public void mouseClicked() { mouseDragged(); }

	public void keyTyped() {}
	public void keyReleased() {}

	public void mouseWheel(MouseEvent event) {
		// if (wheelCnt % 10 == 0) {
		//       wheelCnt++;
		//	brushState++;
		//	brushState %= brushLimit;
		//	brush.setState(brushState);
		//}
	}
}
