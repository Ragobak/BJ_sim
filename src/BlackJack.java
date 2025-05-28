import java.util.ArrayList;
import java.util.Scanner;

/**
 * CURRENT GAME RULES:
 * Blackjack pays 3 to 2
 * dealer hits on soft 17
 * can split 3 times (4 hands)
 * can double after split
 * can only get one card after split aces, cannot re split aces
 * cannot surrender
 * 75% penetration
 */

public class BlackJack {
    public static double BJ_PAYOUT = 1.5;
    public static boolean HIT_ON_SOFT_17 = true;
    public static int MAX_SPLIT_COUNT = 3;
    public static boolean CAN_DOUBLE_AFTER_SPLIT = true;
    public static boolean CAN_RE_SPLIT_ACES = false;
    public static boolean CAN_LATE_SURRENDER = false; //maybe add
    public static double PENETRATION = 0.75;


    protected final Shoe shoe;
    protected double bankroll;
    protected int unit;
    protected BJHand dH;
    protected final ArrayList<BJHand> pHs;
    private static final Scanner input = new Scanner(System.in);

    //initializes blackjack object with shoe, bankroll and unit size
    public BlackJack(Shoe shoe, int bankroll, int unit) {
        pHs = new ArrayList<>();
        this.shoe = shoe;
        this.bankroll = bankroll;
        this.unit = unit;
    }

    //plays the shoe
    public void play(int playerHands) {
        while (shoe.getLeftInDeck() > ((1 - PENETRATION) * 52 * shoe.getDecks())) {
            playHand(playerHands);
            clearHands();
        }
    }

    //plays each hand in the shoe
    public void playHand(int playerHands) {
        setUpHand(playerHands);
        if(checkBlackJack()){
            playPlayerHands();
            playDealerHand();
        }
        postPlayedLogic();
    }

    //resets the shoe to full
    public void resetShoe(){
        shoe.resetShoe();
    }

    //sets up the individual hands, dealer and players
    protected void setUpHand(int numHands) {
        dH = new BJHand(2, shoe, unit);

        for (int i = 0; i < numHands; i++) {
            pHs.add(new BJHand(2, shoe, unit));
        }

        //prints the cards
        for (int i = 0; i < numHands; i++) {
            handMsg(i);
        }
        dUpCardMsg();
    }

    //checks for BJ in each hand, players and dealer, false if dealer BJ, true else
    private boolean checkBlackJack() {
        for (int i = 0; i < pHs.size(); i++) {
            //if hand is BJ, finishes it
            if (pHs.get(i).getTotal() == 21) {
                pHs.get(i).setBJ();
                //if beats dealer, payout and print msg
                if (dH.getTotal() < 21) {
                    bankroll += unit * BJ_PAYOUT;
                    pBJMsg(i);
                } else if (dH.getTotal() == 21) {
                    //if push print that and move on to next if
                    pAndDBJMsg(i);
                }
            }
        }
        //if dealer BJ finish dealer hand
        if (dH.getTotal() == 21) {
            dH.setBJ();
            for (int i = 0; i < pHs.size(); i++) {
                //if player hand is not BJ, finish each player hand and print msg
                if (!pHs.get(i).isBJ()) {
                    bankroll -= unit;
                    pHs.get(i).setFinished();
                    dHandMsg();
                    dBJMsg(i);
                    //skip to post hand logic
                    return false;
                }
            }
        }
        //continue play
        return true;
    }

    //player takes their actions
    private void playPlayerHands() {
        for (int i = 0; i < pHs.size(); i++) {
            //if hand was split, adds second card after first hand played
            if (pHs.get(i).size() == 1) {
                hit(i);
                if (pHs.get(i).getValue(0) == 11) {
                    if(pHs.get(i).getValue(1) != 11) {
                        pHs.get(i).setFinished();
                    } else {
                        pHs.get(i).lockChoice();
                    }
                    if(!handleTimesSplit(i)) {
                        pHs.get(i).setFinished();
                        aceHandle(pHs.get(i));
                    }
                }
                sHandFirstCardMsg(i);
            }
            //while hand is live
            while (!pHs.get(i).isFinished()) {
                decisionMsg(i);
                //choose action
                int choice = this.getChoice(i);
                //handle rare case that player tries to hit or double on two aces after already split
                if(pHs.get(i).choiceLocked() && (choice == 0 || choice == 2)) {
                    twoAcesAfterSplitMsg();
                    continue;
                }
                //hit
                if (choice == 0) {
                    hit(i);
                    hitMsg(i);
                    //double down
                } else if (choice == 2) {
                    if (pHs.get(i).size() == 2) {
                        if(isSplit(i) && !CAN_DOUBLE_AFTER_SPLIT) {
                            cantDASMsg(i);
                            continue;
                        }
                        doubleDown(i);
                        doubleMsg(i);
                    } else {
                        doubleErrorMsg(i);
                    }
                    //stay, set finished, end loop onto next hand
                } else if (choice == 1) {
                    stay(i);
                    stayMsg(i);
                    //split
                } else if (choice == 3) {
                    //make sure can split
                    if (pHs.get(i).getValue(0) == pHs.get(i).getValue(1)) {
                        if (pHs.get(i).size() != 2) {
                            splitErrorMsg1(i);
                        } else {
                            split(i);
                        }
                    } else {
                        splitErrorMsg2(i);
                    }
                } else if (choice == 4 && CAN_LATE_SURRENDER) {
                    pHs.get(i).setBusted();
                    bankroll -= unit * 0.5;
                    surrenderMsg(i);
                }
                //if player has two aces (after split again or non split choice)
                if(pHs.get(i).getValue(0) == 11 && pHs.get(i).getValue(1) == 11
                    && pHs.get(i).size() == 2){
                    //handle if stayed on 2 aces
                    if(pHs.get(i).isFinished() || !handleTimesSplit(i)){
                        aceHandle(pHs.get(i));
                        pHs.get(i).setFinished();
                    }
                    twoAcesAfterSplitMsg();
                    pHs.get(i).lockChoice();
                }

                //if player hand busts, set finished
                else if (pHs.get(i).getTotal() > 21) {
                    //if one ace, then sets to 1 and keeps going
                    aceHandle(pHs.get(i));
                    if (pHs.get(i).getTotal() > 21 && !aceHandle(pHs.get(i))) {
                        pHs.get(i).setBusted();
                        bankroll -= pHs.get(i).getFinalBet();
                        pBustMsg(i);
                    }
                }
            }
        }
    }

    //once all player hands played, dealer plays
    private void playDealerHand() {
        boolean runDealer = false;
        //if every hand is BJ or is busted, dealer will not play and end hand
        for (BJHand pH : pHs) {
            if (!(pH.isBJ() || pH.isBusted())) {
                runDealer = true;
                break;
            }
        }
        //print the dealer full hand
        dHandMsg();
        //if determined above, skip playing dealer (already skipped if BJ in checkBlackJack
        if (!runDealer) return;
        //if starts soft 17 (ace and 6) will hit again
        if ((dH.getTotal() == 17 && dH.hasAce() && HIT_ON_SOFT_17)){
            dH.addRandomCard(shoe);
            dHitMsg();
        }
        //if starts with two aces or hits after soft 17 and busts, will reset ace to 1
        if (dH.hasAce() && dH.getTotal() > 21) {
            aceHandle(dH);
        }
        //hit until 17, if it goes over will check ace to keep hitting, busts are taken care of in postPlayedLogic
        while (dH.getTotal() < 17) {
            dH.addRandomCard(shoe);
            dHitMsg();
            //will hit again on soft 17
            if (dH.getTotal() == 17 && dH.hasAce() && HIT_ON_SOFT_17) {
                dH.addRandomCard(shoe);
                dHitMsg();
            }
            //if busted, reset ace to 1
            if (dH.getTotal() > 21 || (dH.getTotal() == 17 && HIT_ON_SOFT_17)) {
                aceHandle(dH);
            }
            //will hit on soft 17 after reset if ace put it to 17
            if (dH.getTotal() == 17 && dH.hasAce() && HIT_ON_SOFT_17) {
                dH.addRandomCard(shoe);
                dHitMsg();
            }

        }
    }

    //handles after all cards are out whether win loss or push
    private void postPlayedLogic() {
        if (dH.isBJ()) return;
        for (int i = 0; i < pHs.size(); i++) {
            if (!pHs.get(i).isBJ() && !pHs.get(i).isBusted()) {
                //if dealer is busted player win
                if (dH.getTotal() > 21) {
                    bankroll += pHs.get(i).getFinalBet();
                    dBustMsg(i);
                } else {
                    //if dealer wins, loss
                    if (dH.getTotal() > pHs.get(i).getTotal()) {
                        bankroll -= pHs.get(i).getFinalBet();
                        pHandLossMsg(i);
                        //if dealer loses, win
                    } else if (dH.getTotal() < pHs.get(i).getTotal()) {
                        bankroll += pHs.get(i).getFinalBet();
                        pHandWinMsg(i);
                        //push otherwise, no bankroll change
                    } else {
                        pHandPushMsg(i);
                    }
                }
            }
        }
    }

    //true if ace was found and turned into a 1, false if no ace
    protected boolean aceHandle(Hand hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getValue(i) == 11) {
                hand.changeCard(i, new Card(1, 1));
                aceMsg(hand);
                return true;
            }
        }
        return false;
    }


    //adds a random card to a hand
    private void hit(int index) {
        pHs.get(index).addRandomCard(shoe);
    }

    //sets a BJ hand as finished
    private void stay(int index) {
        pHs.get(index).setFinished();
    }

    //adds card to hand, doubles bet, finishes hand
    protected void doubleDown(int index) {
        pHs.get(index).addRandomCard(shoe);
        pHs.get(index).setFinalBet(unit * 2);
        stay(index);
    }

    //adds new hand right after current hand with starting card
    protected void split(int index) {
        //make sure not split too many times, if so cancel and print msg
        if(!handleTimesSplit(index)) return;
        splitMsg(index);
        pHs.get(index).removeCard(1);
        //creates empty hand in pHs and adds the card that was removed
        pHs.add(index + 1, new BJHand(0, shoe, unit));
        pHs.get(index + 1).addCard(pHs.get(index).get(0));
        //handles split count limit
        calcTimesSplit(index);
        //if ace, only adds one card to each and sets each as finished
        if(pHs.get(index).getValue(0)==11){
            hitOnSplitAces(index);
            return;
        }
        //adds second card to first hand
        hit(index);
        sHandFirstCardMsg(index);
    }

    //hits once on each ace, if another ace it terminates so that it can be split again
    private void hitOnSplitAces(int index) {
        splitAcesMsg(index);
        hit(index);
        sHandFirstCardMsg(index);
        //will resplit action if another ace (if enabled), same below
        if(pHs.get(index).getValue(1)==11 && CAN_RE_SPLIT_ACES){
            return;
        }
        pHs.get(index).setFinished();
        if(pHs.get(index).getValue(1)==11){
            aceHandle(pHs.get(index));
        }
        hit(index + 1);
        sHandFirstCardMsg(index + 1);
        if(pHs.get(index + 1).getValue(1)==11 && CAN_RE_SPLIT_ACES){
            return;
        }
        pHs.get(index + 1).setFinished();
        if(pHs.get(index + 1).getValue(1)==11){
            aceHandle(pHs.get(index + 1));
        }
    }

    //true if can split again, false if cannot
    protected boolean handleTimesSplit(int i) {
        if(pHs.get(i).getTimesSplit()>=MAX_SPLIT_COUNT){
            maxSplitsMsg(i);
            return false;
        }
        if(pHs.get(i).getSplitFrom() == null){
            return true;
        } else if (pHs.get(i).getSplitFrom()!=null){
            if(pHs.get(i).getSplitFrom().getTimesSplit()>=MAX_SPLIT_COUNT){
                maxSplitsMsg(i);
                return false;
            }
        }
        return true;
    }

    private void calcTimesSplit(int i) {
        //if first split, set second hands parent to first, increment timesSplit on first
        if(pHs.get(i).getSplitFrom()==null){
            pHs.get(i+1).setSplitFrom(pHs.get(i));
            pHs.get(i).addTimesSplit();
        }
        //if not first split, set new hand splitFrom to initial, and increment initial
        else if(pHs.get(i).getSplitFrom()!=null){
            pHs.get(i+1).setSplitFrom(pHs.get(i).getSplitFrom());
            pHs.get(i).getSplitFrom().addTimesSplit();
        }
    }

    //returns true the hand has been split
    protected boolean isSplit(int i) {
        if(pHs.get(i).getTimesSplit()>0){
            return true;
        }
        return pHs.get(i).getSplitFrom() == null;
    }

    protected void clearHands() {
        while (!pHs.isEmpty()) {
            pHs.removeFirst();
        }
    }

    protected int getChoice(int i){
        return input.nextInt();
    }







    //print msgs for game

    protected void handMsg(int i) {
        System.out.println("\nplayer " + i + " hand: " + pHs.get(i).getTotal());
        System.out.println("card 1: " + pHs.get(i).getValue(0) +
                " card 2: " + pHs.get(i).getValue(1));
    }

    protected void dUpCardMsg() {
        System.out.println("\ndealer up card: " + dH.getValue(0));
    }

    protected void pBJMsg(int i) {
        System.out.println("Hand " + i + " blackjack! Money: " + bankroll);
    }

    protected void dBJMsg(int i) {
        System.out.println("Hand " + i + " dealer blackjack. Money: " + bankroll);
    }

    protected void pAndDBJMsg(int i) {
        System.out.println("Hand " + i + " both blackjack, push. Money: " + bankroll);
    }

    protected void decisionMsg(int i) {
        System.out.print("Hand " + i + " decision: hit: 0, stay: 1, double down: 2, split: 3");
        if(CAN_LATE_SURRENDER) {
            System.out.println(", surrender: 4");
        } else  {
            System.out.println();
        }
    }

    protected void hitMsg(int i) {
        System.out.println("you hit and got a " + pHs.get(i).getValue(pHs.get(i).size() - 1) +
                " which puts you at " + pHs.get(i).getTotal());
    }

    protected void cantDASMsg(int i) {
        System.out.println("cant double after split on hand " + i + " in this game mode");
    }

    protected void doubleMsg(int i) {
        System.out.println("you doubled down and got a " + pHs.get(i).getValue(pHs.get(i).size() - 1) +
                " which puts you at " + pHs.get(i).getTotal());
    }

    protected void doubleErrorMsg(int i) {
        System.out.println("cannot double down after getting a card");
    }

    protected void stayMsg(int i) {
        System.out.println("you stayed on " + pHs.get(i).getTotal());
    }

    protected void splitMsg(int i) {
        System.out.println("you split " + pHs.get(i).getValue(0) + "'s");
    }

    protected void splitErrorMsg1(int i) {
        System.out.println("cannot split after getting a card");
    }

    protected void splitErrorMsg2(int i) {
        System.out.println("cannot split without 2 of same card");
    }

    protected void splitAcesMsg(int i) {
        System.out.println("you can only get 1 card after splitting aces");
    }

    protected void surrenderMsg(int i) {
        System.out.println("you surrendered " + pHs.get(i).getTotal() + ". Money: " + bankroll);
    }

    protected void pBustMsg(int i) {
        System.out.println("Hand " + i + " bust! Money: " + bankroll);
    }

    protected void dHandMsg() {
        System.out.println("dealer hole card: " + dH.getValue(1) + " dealer hand total: " + dH.getTotal());
    }

    protected void dHitMsg(){
        System.out.println("dealer got a " + dH.getValue(dH.size() - 1) +
                " which puts him at " + dH.getTotal());
    }

    protected void dBustMsg(int i) {
        System.out.println("Hand " + i + " dealer bust. Money: " + bankroll);
    }

    protected void pHandLossMsg(int i) {
        System.out.println("Hand " + i + " loss. Money: " + bankroll);
    }

    protected void pHandWinMsg(int i) {
        System.out.println("Hand " + i + " win. Money: " + bankroll);
    }

    protected void pHandPushMsg(int i) {
        System.out.println("Hand " + i + " push. Money: " + bankroll);
    }

    protected void aceMsg(Hand hand) {
        System.out.println(" ace detected! New hand total: " + hand.getTotal());
    }

    protected void sHandFirstCardMsg(int i) {
        System.out.println("Hand " + i + " got a " + pHs.get(i).getValue(1) +
                " which puts you at " + pHs.get(i).getTotal());
    }

    protected void maxSplitsMsg(int i) {
        System.out.println("Hand " + i + " has reached max splits: " + MAX_SPLIT_COUNT);
    }

    protected void twoAcesAfterSplitMsg(){
        System.out.println("Can only split or stay after splitting aces and getting another ace");
    }
}

