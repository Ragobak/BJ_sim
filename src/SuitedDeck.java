import java.util.ArrayList;

public class SuitedDeck extends Deck{

    public SuitedDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 14; j++) {
                SuitedCard toAdd;
                if (i == 0) toAdd = new SuitedCard(j, "h");
                else if (i == 1) toAdd = new SuitedCard(j, "d");
                else if (i == 2) toAdd = new SuitedCard(j, "s");
                else toAdd = new SuitedCard(j, "c");
                cards.add(toAdd);
            }
        }
    }


    //TODO: finish these methods
    @Override
    public Card pickCard() {
        return null;
    }

    @Override
    public String toString(){
        return null;
    }

}
