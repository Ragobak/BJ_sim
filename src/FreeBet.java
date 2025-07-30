public class FreeBet extends BlackJack{

    public FreeBet(Shoe shoe, int bankroll, int unit){
        super(shoe, bankroll, unit);
    }

    @Override
    protected void setUpHand(int numHands) {
        dH = new BJHand(2, shoe, unit);

        for (int i = 0; i < numHands; i++) {
            pHs.add(new FreeBetHand(2, shoe, unit, false));
        }

        //prints the cards
        for (int i = 0; i < numHands; i++) {
            handMsg(i);
        }

        dUpCardMsg();
    }

    //TODO: work on rest of overrides for this gamemode
}
