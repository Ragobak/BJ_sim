import java.util.ArrayList;
import java.util.Collections;

public class Shoe extends Deck {
    private final int decks;
    
    public Shoe(int decks){
        this.decks = decks;
        cards = new ArrayList<>();
        resetShoe();
    }

    public int getDecks(){
        return decks;
    }

    public void resetShoe(){
        cards.clear();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4 * decks; j++) cards.add(new Card(i+2));
        }
        for (int j = 0; j < 12 * decks; j++) cards.add(new Card(10));
        left = 52 * decks;
        Collections.shuffle(cards);
    }
}