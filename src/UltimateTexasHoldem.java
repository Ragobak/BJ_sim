import java.util.ArrayList;
import java.util.Scanner;

public class UltimateTexasHoldem {

    protected final SuitedDeck deck;
    protected double bankroll;
    protected int unit;
    protected ArrayList<SuitedCard> runout;
    protected final ArrayList<UTHHand> pHs;

    public UltimateTexasHoldem(SuitedDeck deck, int bankroll, int unit) {
        this.deck = deck;
        this.bankroll = bankroll;
        this.unit = unit;
        this.pHs = new ArrayList<>();
        this.runout = new ArrayList<>();
    }


}
