public class Player {

	public static final int INITIAL_MONEY = 50000; 
	public Player() {
	}

	private int handValue = 0;
	private int money = INITIAL_MONEY;
	private int bet = 0;
	private boolean out = false; 
	
	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public int getHandValue() {
		return handValue;
	}

	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void addHandValue(Card card) {
		handValue += card.getValue();
		// ace can be treated as 1 if player is about to bust
		if(card.getName() == "Ace" && handValue >= BlackJackGame.BUST_VALUE) {
			handValue -= 10; 
		}
		
		// every time a card is dealt, modify runningCount
		if (card.getValue() == 10 || card.getValue() == 11) {
			BlackJackGame.runningCount--; 
		} else if (card.getValue() <= 6) {
			BlackJackGame.runningCount++; 
		}
		
		//System.out.println("Card name: " + card.getName() + " Running Count: " + BlackJackGame.runningCount);
	}

	public void addMoney(int winnings) {
		money += winnings;
	}
	
	public void loseMoney(int bet){
		money -= bet; 
	}

}
