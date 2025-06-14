import java.util.*;

//TODO: add the differing units and strategies based on counts
//TODO: and implement a way to have them used
public class CountingBlackJack extends AutoBlackJack {

    protected Map<Integer, Integer[][]> cardCounts;
    protected int initialUnit;
    protected int runningCount;
    protected Map<Integer, Integer[][]> unitSizes;

    public CountingBlackJack(Shoe shoe, int bankroll, int unit) {
        super(shoe, bankroll, unit, null);
        initialUnit = unit;
        runningCount = 0;
    }

    //get the true count
    //TODO: verify this works
    private int getCount(){
        int currentDecks = shoe.getLeftInDeck() / 52;
        return runningCount / currentDecks;
    }

    //method that the messages call to adjust count based on card
    private void adjustCount(int cardValue) {
        if (cardValue >= 10)
            runningCount--;
        else if (cardValue <= 6)
            runningCount++;
    }

    //Overrides where the cards appear to track which cards come out
    @Override
    protected void handMsg(int i){
        adjustCount(pHs.get(i).getValue(0));
        adjustCount(pHs.get(i).getValue(1));
    }

    @Override
    protected void dUpCardMsg(){
        adjustCount(dH.getValue(0));
    }

    @Override
    protected void dHandMsg(){
        adjustCount(dH.getValue(1));
    }

    @Override
    protected void hit(Hand hand){
        super.hit(hand);
        adjustCount(hand.getValue(hand.size() - 1));
    }
}
