public class BlackJackSimulator {

	public static final String FILENAME = "results.txt";

	public static void main(String[] args) {
		BlackJackGame game = new BlackJackGame();
		int numHands = 100; 
		// play 100 hands
		for (int i = 0; i < numHands; i++) {
			game.startRound();
		}

		game.printGameInfo();
		// game.getDeck().printDeckInfo();
	}
}
