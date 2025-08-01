import java.util.ArrayList;
import java.util.Scanner;

public class UltimateTexasHoldem {

    protected final SuitedDeck deck;
    protected double bankroll;
    protected int unit;
    protected UTHHand runOut;
    protected final ArrayList<UTHHand> pHs;
    protected UTHHand dH;

    private static final Scanner input = new Scanner(System.in);

    public UltimateTexasHoldem(SuitedDeck deck, int bankroll, int unit) {
        this.deck = deck;
        this.bankroll = bankroll;
        this.unit = unit;
        this.pHs = new ArrayList<>();
    }

    public void play(int playerHands, int times) {
        for (int i = 0; i < times; i++) {
            playHand(playerHands);
        }
    }

    private void playHand(int playerHands) {
        setUpHands(playerHands);
        preFlopAction(playerHands);
        postFlopAction(playerHands);
        riverAction(playerHands);
        postHandLogic(playerHands);
    }

    private void setUpHands(int playerHands) {
        //deals hands and prints them
        for (int i = 0; i < playerHands; i++) {
            pHs.add(new UTHHand(deck, unit));
            handMsg(i);
        }
        dH = new UTHHand(deck, unit);
        runOut = new UTHHand(deck, unit, 5);
    }

    private void preFlopAction(int playerHands) {
        preFlopDecisionMsg();
        for (int i = 0; i < playerHands; i++) {
            choiceMsg(i);
            int choice = getChoice();
            if (choice == 0) {
                checkMsg(i);
            } else if (choice == 3) {
                pHs.get(i).setFinalBet(3 * (unit));
                threeXMsg(i);
            } else if (choice == 4) {
                pHs.get(i).setFinalBet(4 * (unit));
                fourXMsg(i);
            } else {
                invalidChoiceMsg(i);
            }
        }
    }

    private void postFlopAction(int playerHands) {
        flopMsg();
        for (int i = 0; i < playerHands; i++) {
            if(!pHs.get(i).isFinished()){
                choiceMsg(i);
                int choice = getChoice();
                if (choice == 0) {
                    checkMsg(i);
                } else if (choice == 2) {
                    pHs.get(i).setFinalBet(2 * (unit));
                    twoXMsg(i);
                } else {
                    invalidChoiceMsg(i);
                }
            }
        }
    }

    private void riverAction(int playerHands) {
        turnAndRiverMsg();
        for (int i = 0; i < playerHands; i++) {
            if(!pHs.get(i).isFinished()){
                choiceMsg(i);
                int choice = getChoice();
                if (choice == 0) {
                    foldMsg(i);
                } else if (choice == 1) {
                    callMsg(i);
                    pHs.get(i).setFinalBet(unit);
                } else {
                    invalidChoiceMsg(i);
                }
            }
        }
    }

    private void postHandLogic(int playerHands){
        //TODO: make it determine the hand (pair, flush, etc)
        determineHand(playerHands);
        dealerHand();
        moneyMsg();
    }

    private void determineHand(int playerHands){
        for (int i = 0; i < playerHands; i++) {
            UTHHand final7 = new UTHHand(pHs.get(i), runOut);
            getFinal5(final7);
        }
    }

    private void getFinal5(UTHHand h) {
        if(sFlushCheck(h)) return;
//        quadsCheck(h);
//        fullHouseCheck(h);
//        flushCheck(h);
//        straightCheck(h);
//        tripsCheck(h);
//        pairsCheck(h);
    }

    //TODO: need to make where if duplicate numbers handled properly
    private boolean sFlushCheck(UTHHand h) {
        int counter = 1; int index = 0;
        int val = h.get(0).getValue();
        char suit = h.get(0).getSuit();
        for (int i = 1; i < h.size(); i++) {
            if (h.get(i).getSuit() == suit && val == h.get(i).getValue() - 1) {
                counter++;
            } else if (val == h.get(i).getValue() - 1) {
                if (val == h.get(i + 1).getValue() - 1) {
                    index++;
                }
            } else {
                counter = 1;
                val = h.get(i).getValue();
                suit = h.get(i).getSuit();
                index++;
                if(index > 3) {
                    return false;
                }
            }
        }
        return true;
    }

    protected int getChoice() {
        return input.nextInt();
    }

    protected void handMsg(int i) {
        System.out.println("Player " + i + " hand: " + pHs.get(i).get(0) + ", " + pHs.get(i).get(1));
    }

    protected void flopMsg(){
        System.out.println("Flop is: " );
        for (int i = 0; i < 3; i++) {
            System.out.print(runOut.get(i) + ", ");
        }
        System.out.println();
        System.out.println("0 for check, 2 for 2x bet");
    }

    protected void choiceMsg(int i) {
        System.out.print("Hand " + i + " choice: ");
    }

    protected void preFlopDecisionMsg() {
        System.out.println("0 for check, 3 for 3x, 4 for 4x");
    }

    protected void checkMsg(int i) {
        System.out.println("Hand " + i + " checks ");
    }

    protected void callMsg(int i) {
        System.out.println("Hand " + i + " calls after river");
    }

    protected void twoXMsg(int i) {
        System.out.println("Hand " + i + " bet 2x after flop");
    }

    protected void threeXMsg(int i) {
        System.out.println("Hand " + i + " bet 3x before flop");
    }

    protected void fourXMsg(int i) {
        System.out.println("Hand " + i + " bet 4x before flop");
    }

    protected void foldMsg(int i) {
        System.out.println("Hand " + i + " folds after river");
    }

    protected void invalidChoiceMsg(int i) {
        System.out.println("Invalid choice on hand " + i + ", please try again");
    }

    protected void turnAndRiverMsg() {
        System.out.println("Turn and river: " + runOut.get(3) + ", " + runOut.get(4));
        System.out.println("Full board: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(runOut.get(i) + ", ");
        }
        System.out.println();
        System.out.println("0 for fold, 1 for call");
    }

    protected void dealerHand() {
        System.out.println("Dealer's hand: ");
        for (int i = 0; i < 2; i++) {
            System.out.print(dH.get(i) + ", ");
        }
        System.out.println();
    }

    protected void moneyMsg() {
        System.out.println("bankroll: " + bankroll);
    }
}
