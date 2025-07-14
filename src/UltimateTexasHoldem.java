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

    public void playHand(int playerHands) {
        setUpHands(playerHands);
        preFlopAction(playerHands);
        postFlopAction(playerHands);
        riverAction(playerHands);
    }

    private void setUpHands(int playerHands) {
        //deals hands and prints them
        for (int i = 0; i < playerHands; i++) {
            pHs.add(new UTHHand(deck, unit));
            handMsg(i);
        }
        dH = new UTHHand(deck, unit);
        runOut = new UTHHand(deck, unit, 3);
    }

    private void preFlopAction(int playerHands) {
        preFlopDecisionMsg();
        for (int i = 0; i < playerHands; i++) {
            int choice = getChoice();
            if (choice == 0) {
                checkMsg(i);
                continue;
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
        for (int i = 0; i < playerHands; i++) {
            int choice = getChoice();
            if (choice == 0) {
                checkMsg(i);
                continue;
            } else if (choice == 2) {
                pHs.get(i).setFinalBet(2 * (unit));
                twoXMsg(i);
            } else {
                invalidChoiceMsg(i);
            }
        }
    }

    private void riverAction(int playerHands) {
        for (int i = 0; i < playerHands; i++) {
            int choice = getChoice();
            if (choice == 0) {
                foldMsg(i);
                continue;
            }
            if (choice == 1) {
                callMsg(i);
                pHs.get(i).setFinalBet(unit);
            }
        }
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
        System.out.println("Hand " + i + " bets 2x after flop");
    }

    protected void threeXMsg(int i) {
        System.out.println("Hand " + i + "bet 3x before flop");
    }

    protected void fourXMsg(int i) {
        System.out.println("Hand " + i + "bet 4x before flop");
    }

    protected void foldMsg(int i) {
        System.out.println("Hand " + i + " folds after river");
    }

    protected void invalidChoiceMsg(int i) {
        System.out.println("Invalid choice on hand " + i + ", please try again");
    }
}
