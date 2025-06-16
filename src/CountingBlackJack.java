import java.util.*;

/**
 * Alter various methods in order widen
 * or tighten count consideration
 * Current: -6 to 6
 */

//TODO: add way to have unit sizings from file "U"
public class CountingBlackJack extends AutoBlackJack {

    protected Map<Integer, int[][]> strategies;
    protected Map<Integer, Integer> unitSizes;
    protected int initialUnit;
    protected int runningCount;
    protected String fileFolder;

    //name of file within src as parameter, individual filenames C(count)
    public CountingBlackJack(Shoe shoe, int bankroll, int unit, String fileFolder) {
        super(shoe, bankroll, unit);
        this.fileFolder = fileFolder;
        initializeStrategies();
        initialUnit = unit;
        initializeUnits();
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
    private int getTrueCount(){
        float unRoundedDecks = (float) (shoe.getLeftInDeck() / 52.0);
        int decksLeft = Math.round(unRoundedDecks);
        return runningCount / decksLeft;
    }

    //returns the optimal betting unit from the table
    private int getUnit(int count){
        if(count < -6) count = -6;
        if(count > 6) count = 6;
        return initialUnit * unitSizes.get(count);
    }

    @Override
    protected int getStrategy(int row, int column){
        int count = getTrueCount();
        if(count < -6) count = -6;
        if(count > 6) count = 6;
        return strategies.get(count)[row][column];
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

    //creates the map for strategies
    private void initializeStrategies(){
        strategies = new HashMap<>();
        //takes considerations for counts from -6 to 6
        for(int i = -6; i <= 6; i++){
            strategies.put(i, createStrategy("src/" + fileFolder + "/C" + i));
        }
    }

    private void initializeUnits(){
        unitSizes = new HashMap<>();
        for(int i = -6; i <= 6; i++){
            unitSizes.put(i, i);
        }
    }
}
