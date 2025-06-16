import java.util.*;

//TODO: add the differing units and strategies based on counts
//TODO: and implement a way to have them used, create text file layout for unit inputs
public class CountingBlackJack extends AutoBlackJack {

    protected Map<Integer, Integer[][]> strategies;
    protected int[] unitSizes =
            {1,1,1,1,1,1,1,1,2,3,4,4,4};
    protected int initialUnit;
    protected int runningCount;

    //current solo filename (not map) for testing purposes
    public CountingBlackJack(Shoe shoe, int bankroll, int unit, String filename) {
        super(shoe, bankroll, unit, filename);
        initialUnit = unit;
        runningCount = 0;
    }

    @Override
    public void play(int playerHands){
        while (shoe.getLeftInDeck() > ((1 - PENETRATION) * 52 * shoe.getDecks())) {
            handNumber++;
            unit = getUnit(getTrueCount());
            playHand(playerHands);
            clearHands();
        }
        unit = initialUnit;
        runningCount = 0;
        System.out.println("        NEW SHOE ");
    }

    //get the true count
    //TODO: verify this works
    private int getTrueCount(){
        int currentDecks = shoe.getLeftInDeck() / 52;
        return runningCount / currentDecks;
    }

    //returns the optimal betting unit from the table
    private int getUnit(int count){
        if(count < -6) count = -6;
        if(count > 6) count = 6;
        return initialUnit * unitSizes[count + 6];
    }

    //method that the messages call to adjust count based on card
    private void adjustCount(int cardValue) {
        if (cardValue >= 10)
            runningCount--;
        else if (cardValue <= 6)
            runningCount++;
        System.out.println(runningCount + " rc");
        System.out.println(getTrueCount() + " tc " + shoe.getLeftInDeck() + " glid");
    }

    //Overrides where the cards appear to track which cards come out
    @Override
    protected void handMsg(int i){
        super.handMsg(i);
        adjustCount(pHs.get(i).getValue(0));
        adjustCount(pHs.get(i).getValue(1));
    }

    @Override
    protected void dUpCardMsg(){
        super.dUpCardMsg();
        adjustCount(dH.getValue(0));
    }

    @Override
    protected void dHandMsg(){
        super.dHandMsg();
        adjustCount(dH.getValue(1));
    }

    @Override
    protected void hit(Hand hand){
        super.hit(hand);
        adjustCount(hand.getValue(hand.size() - 1));
    }
}
