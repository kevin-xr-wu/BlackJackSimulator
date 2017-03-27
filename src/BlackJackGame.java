import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BlackJackGame {

	// pretty sure there are race conditions with the checks 
	// when hitting
	
	public static final int BUST_VALUE = 22;
	public static final int STOP_VALUE = 17;
	public static int runningCount = 0; 
	public final int CONTROL_BET = 200; 
	public final int TEST_MIN_BET = 100; 
	
	public BlackJackGame() {
		this.deck = new Deck();
		this.controlPlayer1 = new Player();
		this.controlPlayer2 = new Player();
		this.controlPlayer3 = new Player();

		this.testPlayer1 = new Player();
		this.testPlayer2 = new Player();
		this.testPlayer3 = new Player();

		// a dealer is not exactly a player
		this.dealer = new Player();

		controlPlayers.add(controlPlayer1);
		controlPlayers.add(controlPlayer2);
		controlPlayers.add(controlPlayer3);

		testPlayers.add(testPlayer1);
		testPlayers.add(testPlayer2);
		testPlayers.add(testPlayer3);

	}

	public Deck getDeck() {
		return deck;
	}

	public void startRound() {
		deal();

		// hack to keep count of players who are still in
		int stillPlaying = controlPlayers.size() + testPlayers.size();

		// each player hits until they are either
		// over 17
		// or bust
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			while (controlPlayers.get(playerPosition).getHandValue() < STOP_VALUE) {
				controlPlayers.get(playerPosition).addHandValue(deck.hit());
				if (controlPlayers.get(playerPosition).getHandValue() >= BUST_VALUE) {
					// player is out
					controlPlayers.get(playerPosition).setOut(true);
					stillPlaying--;
				}
			}
		}

		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			while (testPlayers.get(playerPosition).getHandValue() < STOP_VALUE) {
				testPlayers.get(playerPosition).addHandValue(deck.hit());
				if (testPlayers.get(playerPosition).getHandValue() >= BUST_VALUE) {
					// player is out
					testPlayers.get(playerPosition).setOut(true);
					stillPlaying--;
				}
			}
		}
		
		// dealer goes last
		if (stillPlaying == 0) {
			// dealer automatically wins
		} else {
			while (dealer.getHandValue() < STOP_VALUE) {
				dealer.addHandValue(deck.hit());
				if (dealer.getHandValue() >= BUST_VALUE) {
					dealer.setOut(true);
					// players automatically win
					giveWinnings();
				}
			}
		}
		
		if(!dealer.isOut()){
			compareHands();
		}
		
		// reset players and dealer for the next round
		printRoundInfo();
		reset();
	}

	public void printGameInfo() {
		System.out.println("------------------------- End of the Day Results -------------------------");
		int totalMoney = 0; 
		int controlSum = 0; 
		int testSum = 0; 
		System.out.println("Control Players");
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			System.out.println("Player " + playerPosition + " has " + controlPlayers.get(playerPosition).getMoney());
			totalMoney += controlPlayers.get(playerPosition).getMoney();
			controlSum += controlPlayers.get(playerPosition).getMoney();
		}
		System.out.println();
		
		System.out.println("Test Players");
		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			System.out.println("Player " + playerPosition + " has " + testPlayers.get(playerPosition).getMoney());
			totalMoney += testPlayers.get(playerPosition).getMoney();
			testSum += testPlayers.get(playerPosition).getMoney();
		}
		System.out.println();

		System.out.println("Dealer has " + dealer.getMoney());
		totalMoney += dealer.getMoney(); 
		System.out.println("Total money is " + totalMoney);
		System.out.println();
		
		writeToResults("------------------------- End of the Day Results -------------------------");
		writeToResults("Control Players");
		writeToResults("Begin sum: " + controlPlayers.size() * Player.INITIAL_MONEY);
		writeToResults("End sum: " + controlSum);
		int lostAmount = (controlPlayers.size() * Player.INITIAL_MONEY) - controlSum;
		writeToResults("Lost " + lostAmount + " dollars");
		
		writeToResults("Test Players");
		writeToResults("Begin sum: " + testPlayers.size() * Player.INITIAL_MONEY);
		writeToResults("End sum: " + testSum);
		lostAmount = (testPlayers.size() * Player.INITIAL_MONEY) - testSum;
		writeToResults("Lost " + lostAmount + " dollars");
		
		writeToResults("Dealer");
		writeToResults("Dealer began with: " + Player.INITIAL_MONEY);
		writeToResults("Dealer End sum: " + dealer.getMoney());
	}
	
	public void printRoundInfo() {
		System.out.println("------------------------- Round Info -------------------------");
		System.out.println("Control Players");
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			System.out.println("Player " + playerPosition + " bet " + controlPlayers.get(playerPosition).getBet() + 
					", and isOut is " + controlPlayers.get(playerPosition).isOut() + " with " + 
					controlPlayers.get(playerPosition).getHandValue());
		}
		
		System.out.println("Test Players");
		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			System.out.println("Player " + playerPosition + " bet " + testPlayers.get(playerPosition).getBet() + 
					", and isOut is " + testPlayers.get(playerPosition).isOut() + " with " + 
					testPlayers.get(playerPosition).getHandValue());
		}
		
		System.out.println("Dealer hand is " + dealer.getHandValue());
		System.out.println("Running count is " + runningCount);
		System.out.println();
	}
	
	private Deck deck;
	private Player controlPlayer1;
	private Player controlPlayer2;
	private Player controlPlayer3;
	
	private Player testPlayer1;
	private Player testPlayer2;
	private Player testPlayer3;

	private Player dealer;
	private int trueCount = 0; 
	
	// for ease of running functions on all players
	private ArrayList<Player> controlPlayers = new ArrayList<>();
	private ArrayList<Player> testPlayers = new ArrayList<>();  

	private void deal() {
		// everyone starts off with 2 cards
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			for (int i = 0; i < 2; i++) {
				controlPlayers.get(playerPosition).addHandValue(deck.hit());
			}
			// players bet
			controlPlayers.get(playerPosition).setBet(CONTROL_BET);
			controlPlayers.get(playerPosition).loseMoney(CONTROL_BET);
			
			// casino gains the money
			dealer.addMoney(CONTROL_BET);
		}

		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			for (int i = 0; i < 2; i++) {
				testPlayers.get(playerPosition).addHandValue(deck.hit());
			}
			// players bet
			trueCount = calculateTrueCount(runningCount); 
			//System.out.println("True Count at bet time: " + trueCount);
			int betUnit = trueCount - 1; 
			int bet = TEST_MIN_BET; 
			if(betUnit > 0) {
				bet = TEST_MIN_BET * betUnit; 
			} else {
				bet = TEST_MIN_BET; 
			}
			testPlayers.get(playerPosition).setBet(bet);
			testPlayers.get(playerPosition).loseMoney(bet);
			
			// casino gains the money
			dealer.addMoney(bet);
		}
		
		for (int i = 0; i < 2; i++) {
			dealer.addHandValue(deck.hit());
		}
	}

	private void giveWinnings() {
		// give winnings out
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			if (!controlPlayers.get(playerPosition).isOut()) {
				// casino loses money
				dealer.loseMoney(CONTROL_BET);
				controlPlayers.get(playerPosition).addMoney(CONTROL_BET);
			}
		}
		
		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			if (!testPlayers.get(playerPosition).isOut()) {
				// casino loses money
				dealer.loseMoney(testPlayers.get(playerPosition).getBet());
				testPlayers.get(playerPosition).addMoney(testPlayers.get(playerPosition).getBet());
			}
		}
	}

	private void compareHands() {
		int toBeat = dealer.getHandValue();
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			if (controlPlayers.get(playerPosition).getHandValue() < toBeat) {
				controlPlayers.get(playerPosition).setOut(true);
			} else if (controlPlayers.get(playerPosition).getHandValue() == toBeat) {
				// push, player gets their bet back
				controlPlayers.get(playerPosition).setOut(true);
				controlPlayers.get(playerPosition).addMoney(CONTROL_BET);
				dealer.loseMoney(CONTROL_BET);
			}
		}

		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			if (testPlayers.get(playerPosition).getHandValue() < toBeat) {
				testPlayers.get(playerPosition).setOut(true);
			} else if (testPlayers.get(playerPosition).getHandValue() == toBeat) {
				// push, player gets their bet back
				testPlayers.get(playerPosition).setOut(true);
				testPlayers.get(playerPosition).addMoney(testPlayers.get(playerPosition).getBet());
				dealer.loseMoney(testPlayers.get(playerPosition).getBet());
			}
		}
		
		// if players are not out by now, that means that they are winners
		giveWinnings();
	}

	private void reset() {
		for (int playerPosition = 0; playerPosition < controlPlayers.size(); playerPosition++) {
			controlPlayers.get(playerPosition).setBet(0);
			controlPlayers.get(playerPosition).setHandValue(0);
			controlPlayers.get(playerPosition).setOut(false);
		}

		for (int playerPosition = 0; playerPosition < testPlayers.size(); playerPosition++) {
			testPlayers.get(playerPosition).setBet(0);
			testPlayers.get(playerPosition).setHandValue(0);
			testPlayers.get(playerPosition).setOut(false);
		}
		
		dealer.setHandValue(0);
		dealer.setOut(false);
	}
	
	private int calculateTrueCount(int runCount) {
		int numCardsInDeck = 52;
		double remainingDecks = Math.floor(deck.getCurrCardCount() / numCardsInDeck) + 0.5; 
		int count = (int) Math.round(runCount / remainingDecks); 
		return count; 
	}
	
	// writes to results.txt, one line at a time
	private void writeToResults(String singleLine) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(BlackJackSimulator.FILENAME);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(singleLine);
			bw.newLine();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
}
