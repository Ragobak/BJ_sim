import java.io.*;


public class AutoBlackJack extends BlackJack{
    //number of updates as testing happens
    static final int PROGRESS_UPDATES = 10;
    //if want to print shoes
    static final boolean PRINT_SHOES = false;

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

    //creates an autoBlackJack with no strategy
    public AutoBlackJack(Shoe shoe, int bankroll, int unit) {
        super(shoe, bankroll, unit);
        handNumber = 0;
        initialBankroll = bankroll;
        strategy = null;
    }

    //creates an autoBlackJack with a given strategy
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
            handNumber++;
            playHand(playerHands);
            clearHands();
        }
    }

    //plays however many shoes
    public void play(int playerHands, int shoes) {
        for (int i = 0; i < shoes; i++) {
            play(playerHands);
            resetShoe();
            progressCheck(shoes, i);
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

    //updates on progress of shoe running
    protected void progressCheck(int shoes, int i) {
        if ((i % (shoes/(PROGRESS_UPDATES+1))) == 0 && i != 0 && i != shoes) {
            System.out.println("Progress: " + 100.0 * ((double) i/shoes));
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
        return getStrategy(row, column);
    }

    private int findSoftChoice(int i){
        int row = pHs.get(i).getTotal() + 4;
        int column = dH.getValue(0) - 2;
        if(row < 16) row = 16;
        if(row > 24) row = 24;
        return getStrategy(row, column);
    }

    private int findSplitChoice(int i){
        int row = pHs.get(i).getValue(0) + 23;
        int column = dH.getValue(0) - 2;
        if(row < 25) row = 25;
        if(row > 35) row = 35;
        return getStrategy(row, column);
    }

    //base insurance decision basic strategy is no
    @Override
    protected int getInsuranceChoice(int i){
        return 1;
    }

    //method to get the strategy chart that can be overridden
    protected int getStrategy(int row, int column){
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
        if(CAN_LATE_SURRENDER && (choice == 4 || choice == 6 || choice == 7)){
            if(pHs.get(i).size() != 2 || isSplit(i)){
                if(choice == 4) return 0;
                if(choice == 6) return 1;
                //if choice is 7
                return 3;
            } else {
                return 4;
            }
        } else if(!CAN_LATE_SURRENDER && (choice == 4 || choice == 6 || choice == 7)) {
            if(choice == 4) return 0;
            if(choice == 6) return 1;
            //if choice is 7
            return 3;
        }

        //handle cases after split
        if((pHs.get(i).size()!=2 || (isSplit(i) && !CAN_DOUBLE_AFTER_SPLIT)) && choice == 2) return 0;
        if((pHs.get(i).size()!=2 || (isSplit(i) && !CAN_DOUBLE_AFTER_SPLIT)) && choice == 5) return 1;
        else if(choice == 5) return 2;
        if(CAN_DOUBLE_AFTER_SPLIT && choice == 8) return 3;
        else if(choice == 8) return 0;

        //if nothing updated return original choice
        return choice;
    }

    //takes a Strategy file formatted correctly and makes it into readable 2D array
    protected int[][] createStrategy(String filename) {
        int[][] array = new int[35][10];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 35) {
                int commentIndex = line.indexOf("//");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
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
        if(PRINT_SHOES) super.handMsg(i);
    }

    @Override
    protected void dUpCardMsg() {
        if(PRINT_SHOES) super.dUpCardMsg();
    }

    @Override
    protected void insuranceMsg(int i){
        if(PRINT_SHOES) super.insuranceMsg(i);
    }

    @Override
    protected void insuranceFailMsg(int i){
        if(PRINT_SHOES) super.insuranceFailMsg(i);
    }

    @Override
    protected void insuranceSuccessMsg(int i){
        if(PRINT_SHOES) super.insuranceSuccessMsg(i);
    }

    @Override
    protected void pBJMsg(int i) {
        if(PRINT_SHOES) super.pBJMsg(i);
    }

    @Override
    protected void dBJMsg(int i) {
        if(PRINT_SHOES) super.dBJMsg(i);
    }

    @Override
    protected void pAndDBJMsg(int i) {
        if(PRINT_SHOES) super.pAndDBJMsg(i);
    }

    @Override
    protected void decisionMsg(int i) {
        if(PRINT_SHOES) super.decisionMsg(i);
    }

    @Override
    protected void pHitMsg(int i) {
        if(PRINT_SHOES) super.pHitMsg(i);
    }

    @Override
    protected void cantDASMsg(int i) {
        if(PRINT_SHOES) super.cantDASMsg(i);
    }

    @Override
    protected void doubleMsg(int i) {
        if(PRINT_SHOES) super.doubleMsg(i);
    }

    @Override
    protected void doubleErrorMsg() {
        if(PRINT_SHOES) super.doubleErrorMsg();
    }

    @Override
    protected void stayMsg(int i) {
        if(PRINT_SHOES) super.stayMsg(i);
    }

    @Override
    protected void splitMsg(int i) {
        if(PRINT_SHOES) super.splitMsg(i);
    }

    @Override
    protected void splitErrorMsg1() {
        if(PRINT_SHOES) super.splitErrorMsg1();
    }

    @Override
    protected void splitErrorMsg2() {
        if(PRINT_SHOES) super.splitErrorMsg2();
    }

    @Override
    protected void splitAcesMsg() {
        if(PRINT_SHOES) super.splitAcesMsg();
    }

    @Override
    protected void surrenderMsg(int i) {
        if(PRINT_SHOES) super.surrenderMsg(i);
    }

    @Override
    protected void surrenderErrorMsg1() {
        if(PRINT_SHOES) super.surrenderErrorMsg1();
    }

    @Override
    protected void surrenderErrorMsg2() {
        if(PRINT_SHOES) super.surrenderErrorMsg2();
    }

    @Override
    protected void pBustMsg(int i) {
        if(PRINT_SHOES) super.pBustMsg(i);
    }

    @Override
    protected void dHandMsg() {
        if(PRINT_SHOES) super.dHandMsg();
    }

    @Override
    protected void dHitMsg(){
        if(PRINT_SHOES) super.dHitMsg();
    }

    @Override
    protected void dBustMsg(int i) {
        if(PRINT_SHOES) super.dBustMsg(i);
    }

    @Override
    protected void pHandLossMsg(int i) {
        if(PRINT_SHOES) super.pHandLossMsg(i);
    }

    @Override
    protected void pHandWinMsg(int i) {
        if(PRINT_SHOES) super.pHandWinMsg(i);
    }

    @Override
    protected void pHandPushMsg(int i) {
        if(PRINT_SHOES) super.pHandPushMsg(i);
    }

    @Override
    protected void aceMsg(Hand hand) {
        if(PRINT_SHOES) super.aceMsg(hand);
    }

    @Override
    protected void sHandFirstCardMsg(int i) {
        if(PRINT_SHOES) super.sHandFirstCardMsg(i);
    }

    @Override
    protected void maxSplitsMsg(int i) {
        if(PRINT_SHOES) super.maxSplitsMsg(i);
    }

    @Override
    protected void twoAcesAfterSplitMsg(){
        if(PRINT_SHOES) super.twoAcesAfterSplitMsg();
    }

}
