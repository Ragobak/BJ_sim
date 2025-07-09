import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck{

    protected List<Card> cards;
    protected int left;

    //creates a scrambled unsuited deck with 4 of each card, and 16 tens
    public Deck(){
        cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) cards.add(new Card(i+2));
            if(i == 8){
                for (int j = 0; j < 12; j++) cards.add(new Card(10));
            }
        }
        Collections.shuffle(cards);
        left = 52;
    }

    public List<Card> getCards(){
        return cards;
    }

    public Card pickCard(){
        left--;
        return cards.removeFirst();
    }

    public int getLeftInDeck(){
        return left;
    }

    public String toString(){
        StringBuilder r = new StringBuilder();
        for (Card card : cards) {
            r.append(card.getValue()).append(", ");
        }
        return r + "\n";
    }
}