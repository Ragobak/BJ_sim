


public class UTHHand extends Hand {

    //capped hands of 2 cards will use this array, UTHHand flop will use superclass arrayList
    private final Card[] PlayerCards;
    //strength will be 2 if a pair, 1 if suited, 0 if unsuited
    private final int strength;
    //this is the initial bet (blind only)
    private final int initialBet;
    private int finalBet;
    private boolean isFinished;

    //create an array variation capped at 2 cards
    public UTHHand(Deck deck, int unit) {
        PlayerCards = new SuitedCard[2];
        PlayerCards[0] = deck.pickCard();
        PlayerCards[1] = deck.pickCard();
        strength = determineStrength();
        initialBet = unit;
        finalBet = 0;
    }

    //create an arrayList variation of a UTHHand
    public UTHHand(Deck deck, int unit, int numCards) {
        super(numCards, deck);
        strength = -1;
        PlayerCards = null;
        initialBet = unit;
    }

    private int determineStrength() {
        if (PlayerCards[0].getValue() == PlayerCards[1].getValue()) return 2;
        if (PlayerCards[0].getSuit() == PlayerCards[1].getSuit()) return 1;
        return 0;
    }


    public int getStrength() {
        return strength;
    }

    public void setFinalBet(int finalBet) {
        this.finalBet = finalBet;
        this.isFinished = true;
    }

    public int getFinalBet() {
        return finalBet;
    }

    public boolean isFinished() {
        return isFinished;
    }


    //following overridden methods determine which kind of hand it is, then act accordingly
    @Override
    public int getValue(int index) {
        if (PlayerCards != null) return PlayerCards[index].getValue();
        return cards.get(index).getValue();
    }

    @Override
    public Card get(int card) {
        if (PlayerCards != null) return PlayerCards[card];
        return cards.get(card);
    }

    @Override
    public char getSuit(int index) {
        if (PlayerCards != null) return PlayerCards[index].getSuit();
        return cards.get(index).getSuit();
    }

    public String toString() {
        return PlayerCards[0] + ", " + PlayerCards[1];
    }

}
