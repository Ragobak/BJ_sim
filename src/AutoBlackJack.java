import java.io.*;

//something needs fixing, MAKE SURE REG GAME WORKS

public class AutoBlackJack extends BlackJack{

    //keep track of total hands/money played, and starting bankroll
    protected int handNumber;
    protected long initialBankroll;
    protected long totalMoneyPlayed;

    /**
     * Strategy card key
     * 0 hit, 1 stay, 2 double (if cant, hit), 3 split, 4 surrender (if cant, hit),
     * 5 double (if cant, stay), 6 surrender (if cant, stay), 7 surrender (if cant, split)
     * 8 split (if can double after split, else hit)
    */

    //fix strategy based on game
    private final int[][] strategy;

    public AutoBlackJack(Shoe shoe, int bankroll, int unit) {
        super(shoe, bankroll, unit);
        handNumber = 0;
        initialBankroll = bankroll;
        totalMoneyPlayed = 0;
        //can change this around and set strategy based on other game mode changes
        if(HIT_ON_SOFT_17){
            strategy = createStrategy("src/HitOnSoft17.txt");
        } else {
            strategy = createStrategy("src/StayOnSoft17.txt");
        }
    }

    public AutoBlackJack(Shoe shoe, int bankroll, int unit, String filename) {
        super(shoe, bankroll, unit);
        handNumber = 0;
        initialBankroll = bankroll;
        strategy = createStrategy(filename);
    }

    //plays 1 shoe
    @Override
    public void play(int playerHands) {
        while (shoe.getLeftInDeck() > ((1 - PENETRATION) * 52 * shoe.getDecks())) {
            playHand(playerHands);
            handNumber++;
            clearHands();
        }
    }

    //plays however many shoes
    public void play(int playerHands, int shoes) {
        for (int i = 0; i < shoes; i++) {
            play(playerHands);
            resetShoe();
            if (totalMoneyPlayed < 0) {
                System.err.println("Warning: totalMoneyPlayed overflowed!");
            }
        }
        //print total hands after x shoes and money, and percent profit
        System.out.println("\nTotal hands played: " + handNumber +
                " Bankroll: " + bankroll);
        System.out.println("Total money played: " + totalMoneyPlayed);
        System.out.println("% profit per unit: " +
                (((bankroll - initialBankroll) / (totalMoneyPlayed)) * 100) + "\n");
    }

    //plays however many shoes however many times
    public void play(int playerHands, int shoes, int times) {
        for (int i = 0; i < times; i++) {
            play(playerHands, shoes);
            handNumber = 0;
            totalMoneyPlayed = 0;
            bankroll = initialBankroll;
        }
    }

    //Overridden method to choose action based on strategy card
    @Override
    protected int getChoice(int i){
        int choice;
        if(pHs.get(i).size()==2 && pHs.get(i).getValue(0) == (pHs.get(i).getValue(1))
                && handleTimesSplit(i) && pHs.get(i).size() == 2){
            choice =  findSplitChoice(i);
        } else if(pHs.get(i).hasAce()) {
            //if in rare case cant split aces, treat it as soft 12
            if(pHs.get(i).getValue(0) == 11 && pHs.get(i).getValue(1)==11){
                aceHandle(pHs.get(i));
            }
            choice = findSoftChoice(i);
        } else {
            choice = findHardChoice(i);
        }

        choice = recheckChoice(i, choice);
        return choice;
    }

    private int findHardChoice(int i){
        int row = pHs.get(i).getTotal() - 4;
        int column = dH.getValue(0) - 2;
        if(row < 0) row = 0;
        if(row > 15) row = 15;
        return strategy[row][column];
    }

    private int findSoftChoice(int i){
        int row = pHs.get(i).getTotal() + 4;
        int column = dH.getValue(0) - 2;
        if(row < 16) row = 16;
        if(row > 24) row = 24;
        return strategy[row][column];
    }

    private int findSplitChoice(int i){
        int row = pHs.get(i).getValue(0) + 23;
        int column = dH.getValue(0) - 2;
        if(row < 25) row = 25;
        if(row > 35) row = 35;
        return strategy[row][column];
    }

    //next three overridden methods in order to have total money played for percent calculations
    @Override
    protected void setUpHand(int numHands) {
        super.setUpHand(numHands);
        totalMoneyPlayed += (long) numHands * unit;
    }
    @Override
    protected void doubleDown(int index) {
        super.doubleDown(index);
        totalMoneyPlayed++;
    }

    @Override
    protected void split(int index) {
        super.split(index);
        if(handleTimesSplit(index)) totalMoneyPlayed += unit;
    }

    //rechecks to make sure decision is allowed
    private int recheckChoice(int i, int choice) {
        //7 and 8 only show up in split cases, so more checks are done in getChoice
        //handle cases where wants to surrender but cant
        if((pHs.get(i).size() != 2 || !isSplit(i) || !CAN_LATE_SURRENDER) && choice == 4) return 0;
        if((pHs.get(i).size() != 2 || !isSplit(i) || !CAN_LATE_SURRENDER) && choice == 6) return 1;
        else if(choice == 6) return 4;
        if(!CAN_LATE_SURRENDER && choice == 7) return 3;
        else if(choice == 7) return 4;

        //handle cases after split
        //TODO desicion tree slightly wrong here
        if((pHs.get(i).size()!=2 || (isSplit(i) && CAN_DOUBLE_AFTER_SPLIT)) && choice == 2) return 0;
        if((pHs.get(i).size()!=2 || (isSplit(i) && CAN_DOUBLE_AFTER_SPLIT)) && choice == 5) return 1;
        //TODO until here
        else if(choice == 5) return 2;
        if(CAN_DOUBLE_AFTER_SPLIT && choice == 8) return 3;
        else if(choice == 8) return 0;

        //if nothing updated return original choice
        return choice;
    }

    //takes a file formatted correctly and makes it into readable 2D array
    protected int[][] createStrategy(String filename) {
        int[][] array = new int[35][10];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 35) {
                // Remove comments starting with "//"
                int commentIndex = line.indexOf("//");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
                // Trim and split on commas
                String[] tokens = line.trim().split(",");
                if (tokens.length != 10) {
                    throw new RuntimeException("Invalid number of values in row " + row);
                }
                for (int col = 0; col < 10; col++) {
                    array[row][col] = Integer.parseInt(tokens[col].trim());
                }
                row++;
            }
            if (row != 35) {
                throw new RuntimeException("Expected 31 rows, but got " + row);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
        return array;
    }




//    so no messages are printed during auto run
    @Override
    protected void handMsg(int i) {
    }

    @Override
    protected void dUpCardMsg() {
    }

    @Override
    protected void pBJMsg(int i) {
    }

    @Override
    protected void dBJMsg(int i) {
    }

    @Override
    protected void pAndDBJMsg(int i) {
    }

    @Override
    protected void decisionMsg(int i) {
    }

    @Override
    protected void pHitMsg(int i) {
    }

    @Override
    protected void cantDASMsg(int i) {
    }

    @Override
    protected void doubleMsg(int i) {
    }

    @Override
    protected void doubleErrorMsg(int i) {
    }

    @Override
    protected void stayMsg(int i) {
    }

    @Override
    protected void splitMsg(int i) {
    }

    @Override
    protected void splitErrorMsg1(int i) {
    }

    @Override
    protected void splitErrorMsg2(int i) {
    }

    @Override
    protected void splitAcesMsg(int i) {
    }

    @Override
    protected void surrenderMsg(int i) {
    }

    @Override
    protected void surrenderErrorMsg() {
    }

    @Override
    protected void pBustMsg(int i) {
    }

    @Override
    protected void dHandMsg() {
    }

    @Override
    protected void dHitMsg(){
    }

    @Override
    protected void dBustMsg(int i) {
    }

    @Override
    protected void pHandLossMsg(int i) {
    }

    @Override
    protected void pHandWinMsg(int i) {
    }

    @Override
    protected void pHandPushMsg(int i) {
    }

    @Override
    protected void aceMsg(Hand hand) {
    }

    @Override
    protected void sHandFirstCardMsg(int i) {
    }

    @Override
    protected void maxSplitsMsg(int i) {
    }

    @Override
    protected void twoAcesAfterSplitMsg(){
    }

}
