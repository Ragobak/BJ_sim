import java.util.ArrayList;
import java.util.List;


public class Hand extends Card {
    protected final List<Card> cards;
    int total;

    public Hand() {
        cards = null;
    }

    public Hand(int num, Deck deck) {
        cards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            this.addCard(deck.pickCard());
        }
    }

    public int getTotal() {
        return total;
    }

    public Card get(int card) {
        return cards.get(card);
    }

    public void removeCard(int cardIndex) {
        total -= cards.get(cardIndex).getValue();
        cards.remove(cardIndex);
    }

    public int getValue(int cardIndex) {
        return cards.get(cardIndex).getValue();
    }

    public char getSuit(int cardIndex) {
        return ' ';
    }

    public void addRandomCard(Deck deck){
        this.addCard(deck.pickCard());
    }

    public void changeCard(int index, Card newCard){
        total += (newCard.getValue() - cards.get(index).getValue());
        cards.set(index, newCard);
    }

    public void addCard(Card card) {
        cards.add(card);
        total += card.getValue();
    }

    public int size() {
        return cards.size();
    }
}
