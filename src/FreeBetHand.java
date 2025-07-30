

public class FreeBetHand extends BJHand{
    private final boolean isFreeHand;

    public FreeBetHand(int num, Deck deck, int unit, boolean isFreeHand) {
        super(num, deck, unit);
        this.isFreeHand = isFreeHand;
    }

    public boolean isFreeHand() {
        return isFreeHand;
    }
}
