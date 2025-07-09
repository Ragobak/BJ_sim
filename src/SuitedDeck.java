import java.util.ArrayList;
import java.util.Collections;

public class SuitedDeck extends Deck{

    //standard 52 card deck
    public SuitedDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                SuitedCard toAdd;
                if (i == 0) toAdd = new SuitedCard(j, "h");
                else if (i == 1) toAdd = new SuitedCard(j, "d");
                else if (i == 2) toAdd = new SuitedCard(j, "s");
                else toAdd = new SuitedCard(j, "c");
                cards.add(toAdd);
            }
        }
        Collections.shuffle(cards);
        left = 52;
    }

    @Override
    public String toString(){
        StringBuilder r = new StringBuilder();
        for (Card card : cards) {
            r.append(card.getValue()).append(card.getSuit()).append(", ");
        }
        return r + "\n";
    }

}
