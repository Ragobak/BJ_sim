import java.util.*;
import java.io.*;


//simple current changes, just stay 16 +count, insurance, and unit sizing



public class CountingBlackJack extends AutoBlackJack {
    //range of counts to be considered
    protected int countRange;
    protected List<int[][]> strategies;
    protected int[] unitSizes;
    protected int initialUnit;
    protected int runningCount;
    protected String fileFolder;
    protected int insuranceTake;


    //name of file within src as parameter, individual filenames C(count)
    public CountingBlackJack(Shoe shoe, int bankroll, int unit,
                             int countRange, int insuranceTake, String fileFolder) {
        super(shoe, bankroll, unit);
        this.countRange = countRange;
        this.fileFolder = fileFolder;
        this.insuranceTake = insuranceTake;
        runningCount = 0;
        initialUnit = unit;
        initializeStrategies();
        initializeUnits();
    }

    //plays shoes implementing changing unit and strategy per hand, and resetting count after each shoe
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
    }

    //get the true count
    private int getTrueCount(){
        float unRoundedDecks = (float) (shoe.getLeftInDeck() / 52.0);
        int decksLeft = Math.round(unRoundedDecks);
        return runningCount / decksLeft;
    }

    //returns the optimal betting unit from the table
    private int getUnit(int count){
        if(count < -countRange) count = -countRange;
        if(count > countRange) count = countRange;
        return initialUnit * unitSizes[count + countRange];
    }

    //gets strategy from list based on count
    @Override
    protected int getStrategy(int row, int column){
        int count = getTrueCount();
        if(count < -countRange) count = -countRange;
        if(count > countRange) count = countRange;
        return strategies.get(count + countRange)[row][column];
    }

    //takes insurance after a specified count
    @Override
    protected int getInsuranceChoice(int i){
        if(getTrueCount() < insuranceTake) {
            return 1;
        }
        return 0;
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

    
    //creates the list for strategies
    private void initializeStrategies(){
        strategies = new ArrayList<>();
        //takes considerations for counts for range sizing
        for(int i = -countRange; i <= countRange; i++){
            strategies.add(createStrategy("src/" + fileFolder + "/C" + i));
        }
    }

    //creates the array for unit sizing
    private void initializeUnits(){
        unitSizes = createUnitSizing();
    }

    //takes a Unit file formatted correctly and makes it into readable 1D array
    private int[] createUnitSizing() {
        int[] array = new int[countRange * 2 + 1];
        try (BufferedReader br = new BufferedReader(new FileReader("src/" + fileFolder + "/U"))) {
            String line = br.readLine(); 
            if (line == null) {
                throw new RuntimeException("File is empty");
            }
            int commentIndex = line.indexOf("//");
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            String[] tokens = line.trim().split(",");
            if (tokens.length != countRange * 2 + 1) {
                throw new RuntimeException("Expected " + countRange * 2 + 1 + " values, but got " + tokens.length);
            }
            for (int i = 0; i < countRange * 2 + 1; i++) {
                array[i] = Integer.parseInt(tokens[i].trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
        return array;
    }
    
}
