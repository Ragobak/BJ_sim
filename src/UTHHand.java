


public class UTHHand {

    private final Card[] cards;
    //strength will be 2 if a pair, 1 if suited, 0 if unsuited
    private final int strength;


    public UTHHand(Deck deck) {
        cards = new SuitedCard[2];
        cards[0] = deck.pickCard();
        cards[1] = deck.pickCard();
        strength = determineStrength();
    }

    private int determineStrength() {
        if (cards[0].getValue() == cards[1].getValue()) return 2;
        if (cards[0].getSuit() == cards[1].getSuit()) return 1;
        return 0;
    }

    public Card get(int card) {
        return cards[card];
    }

    public int getStrength() {
        return strength;
    }

    public int getValue(int index) {
        return cards[index].getValue();
    }

    public char getSuit(int index) {
        return cards[index].getSuit();
    }

    public String toString() {
        return cards[0] + ", " + cards[1];
    }

}
