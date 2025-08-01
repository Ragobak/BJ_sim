import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UTHHand extends Hand {

    //capped hands of 2 cards will use this array, UTHHand flop will use superclass arrayList
    private final Card[] playerCards;
    //strength will be 2 if a pair, 1 if suited, 0 if unsuited
    //this is the initial bet (blind only)
    private final int initialBet;
    private int finalBet;
    private boolean isFinished;
    //final hand strength (flush, full house, etc)
    private int finalStrength;
    //kicker on said hand, varies (high card w pair, how high the straight, ace high for flush, etc)
    private int finalKicker;


    //create an array variation capped at 2 cards
    public UTHHand(Deck deck, int unit) {
        playerCards = new SuitedCard[2];
        playerCards[0] = deck.pickCard();
        playerCards[1] = deck.pickCard();
        initialBet = unit;
        finalBet = 0;
    }

    //takes in one array style hand and one arraylist and combines them into ordered arraylist
    public UTHHand(UTHHand a, UTHHand aL) {
        playerCards = null;
        cards = new ArrayList<>();
        initialBet = a.initialBet;
        combineHands(a, aL);
    }

    private void combineHands(UTHHand a, UTHHand aL) {
        if(a.playerCards == null) {
            System.out.println("Wrong type of hand in first param");
            return;
        }
        if(aL.playerCards != null) {
            System.out.println("Wrong type of hand in second param");
            return;
        }
        cards.addAll(Arrays.asList(a.playerCards));
        cards.addAll(aL.cards);
        Collections.sort(cards);
    }

    //create an arrayList variation of a UTHHand
    public UTHHand(Deck deck, int unit, int numCards) {
        super(numCards, deck);
        playerCards = null;
        initialBet = unit;
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
        if (playerCards != null) return playerCards[index].getValue();
        return cards.get(index).getValue();
    }

    @Override
    public Card get(int card) {
        if (playerCards != null) return playerCards[card];
        return cards.get(card);
    }

    @Override
    public char getSuit(int index) {
        if (playerCards != null) return playerCards[index].getSuit();
        return cards.get(index).getSuit();
    }

    public String toString() {
        if (playerCards != null) {
            return playerCards[0] + ", " + playerCards[1];
        }
        return super.toString();
    }

}
