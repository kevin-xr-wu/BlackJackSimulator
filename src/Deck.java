import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Deck {

	// standard number of decks for World Series BlackJack
	public static final int NUM_DECKS = 6;

	public Deck() {
		for (int i = 0; i < NUM_DECKS; i++) {
			populateDeck();
		}

		currCardCount = cards.size();

		deckCards.addAll(cards);
		shuffle();
	}

	public Card hit() {
		if(deckCards.empty()){
			cards.clear();
			for (int i = 0; i < NUM_DECKS; i++) {
				populateDeck();
			}
			currCardCount = cards.size();
			deckCards.clear();
			deckCards.addAll(cards);
			shuffle();
		}
		
		currCardCount--;
		return deckCards.pop();
	}

	public void shuffle() {

		Random rnd = new Random();
		for (int i = cards.size() - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			Card swapCard = cards.get(index);
			cards.set(index, cards.get(i));
			cards.set(i, swapCard);
		}

		// reallocate the stack of cards
		deckCards.clear();
		deckCards.addAll(cards);
	}

	public Stack<Card> getDeckCards() {
		return deckCards;
	}

	// testing purposes
	public void printDeckInfo() {
		System.out.println("Number of Cards: " + currCardCount);
		System.out.println("Number of Decks: " + NUM_DECKS);

		// 2 is mapped to 0, Ace is mapped to 12
		int cardCounts[] = new int[13];

		for (int i = 0; i < currCardCount; i++) {
			switch (cards.get(i).getName()) {
			case "2":
				cardCounts[0]++;
				break;
			case "3":
				cardCounts[1]++;
				break;
			case "4":
				cardCounts[2]++;
				break;
			case "5":
				cardCounts[3]++;
				break;
			case "6":
				cardCounts[4]++;
				break;
			case "7":
				cardCounts[5]++;
				break;
			case "8":
				cardCounts[6]++;
				break;
			case "9":
				cardCounts[7]++;
				break;
			case "10":
				cardCounts[8]++;
				break;
			case "Jack":
				cardCounts[9]++;
				break;
			case "Queen":
				cardCounts[10]++;
				break;
			case "King":
				cardCounts[11]++;
				break;
			case "Ace":
				cardCounts[12]++;
				break;
			default:
				break;
			}
		}


		System.out.println("Number of 2's left: " + cardCounts[0]);
		System.out.println("Number of 3's left: " + cardCounts[1]);
		System.out.println("Number of 4's left: " + cardCounts[2]);
		System.out.println("Number of 5's left: " + cardCounts[3]);
		System.out.println("Number of 6's left: " + cardCounts[4]);
		System.out.println("Number of 7's left: " + cardCounts[5]);
		System.out.println("Number of 8's left: " + cardCounts[6]);
		System.out.println("Number of 9's left: " + cardCounts[7]);
		System.out.println("Number of 10's left: " + cardCounts[8]);
		System.out.println("Number of Jack's left: " + cardCounts[9]);
		System.out.println("Number of Queen's left: " + cardCounts[10]);
		System.out.println("Number of King's left: " + cardCounts[11]);
		System.out.println("Number of Ace's left: " + cardCounts[12]);
		System.out.println();
	}

	public int getCurrCardCount() {
		return currCardCount;
	}

	public void setCurrCardCount(int currCardCount) {
		this.currCardCount = currCardCount;
	}
	
	private int currCardCount = 0;

	// card, cardValue
	private ArrayList<Card> cards = new ArrayList<Card>();
	private Stack<Card> deckCards = new Stack<>();
	
	private void populateDeck() {
		// number cards
		for (int card = 2; card < 11; card++) {
			for (int cardCount = 0; cardCount < 4; cardCount++) {
				cards.add(new Card(Integer.toString(card), card));
			}
		}

		// face cards and ace
		for (int card = 0; card < 4; card++) {
			for (int cardCount = 0; cardCount < 4; cardCount++) {
				// bad code, should have a mapping of some kind
				switch (card) {
				case 0:
					cards.add(new Card("Jack", 10));
					break;
				case 1:
					cards.add(new Card("Queen", 10));
					break;
				case 2:
					cards.add(new Card("King", 10));
					break;
				case 3:
					cards.add(new Card("Ace", 11));
					break;
				}
			}
		}
	}

}
