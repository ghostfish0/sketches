public class LifeSimulation {
	public static void main(String[] args) throws InterruptedException {
        Landscape scape = new Landscape(10, 10);
        LandscapeDisplay display = new LandscapeDisplay(scape, 60);

		int whenToAdvance = 0;
		while (true) {
			Thread.sleep(20);
			if (whenToAdvance++ % 2 == 0) {
				scape.advance();
			}
			display.repaint();
		}
	}
}
