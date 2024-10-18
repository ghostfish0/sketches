import processing.core.PApplet;

public class Wireworld extends PApplet {
	final private int width = 500;
	final private int height = 500;
	final private int rows = 20;
	final private int column = 20;

	private boolean running = true;
	private int lagger = 0;
	private int laggerLimit = 5;

	private int brushState = 1;
	private Cell brush = new Cell(brushState, this);
	final private int brushLimit = Cell.stateMap.length;

	private Landscape scape = new Landscape(rows, column, width, height, this);

	public static void main(String[] args) { PApplet.runSketch(new String[] {"Wireworld"}, new Wireworld()); }

	public void settings() { size(scape.getWidth(), scape.getHeight()); }

	public void setup() {
		// frameRate(5);
		background(200);
        scape.reset();
		scape.draw();
	}

	public void draw() {
		background(200);
		scape.draw();
		brush.draw(scape.xAt((double) (mouseX - 10) / width), scape.yAt((double) (mouseY - 10) / height), 25, 25);
		if (running && (lagger++) % laggerLimit == 0)
			scape.advance();
	}

	public void mouseDragged() { scape.cellAt((double) (mouseX - 10) / width, (double) (mouseY - 10) / height).setState(brushState); }

	public void mouseClicked() { mouseDragged(); }

	public void keyTyped() {
		if (key == ' ') {
            running = !running;
		}
	}

    public void mouseWheel() {
			brushState++;
			brushState %= brushLimit;
			brush.setState(brushState);
    }
}
