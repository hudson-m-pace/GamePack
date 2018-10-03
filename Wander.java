public class Wander {
	public static void gameLoop() {
		long lastTime = System.nanoTime();
		final double ticks = 10D;
		double ns = 1000000000 / ticks;
		double delta = 0;
		System.out.println("starting");

		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				System.out.println("hey");
				delta--;
			}
		}
	}
}