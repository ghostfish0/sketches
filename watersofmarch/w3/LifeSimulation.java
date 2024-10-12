public class LifeSimulation {
	public static void main(String[] args) throws InterruptedException {
		for (double chance = 0; chance <= 1; chance += 0.05) {
			Landscape scape = new Landscape(128, 128, chance);
			System.out.print(scape.getSum() + " ");
			for (int iteration = 0; iteration < 1000; iteration++) {
				scape.advance();
			}
			System.out.print(scape.getSum() + " \n");
		}
	}
}
