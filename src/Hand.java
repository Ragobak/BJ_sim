import java.util.ArrayList;


public class Hand extends Card {
    protected final ArrayList<Card> cards;
    int total;

    public Hand(int num, Deck deck) {
        cards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            this.addCard(deck.pickCard());
        }
    }

    public int getTotal() {
        return total;
    }

    public void changeTotal(int dT) {
        total += dT;
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

    public void addRandomCard(Deck deck){
        this.addCard(deck.pickCard());
    }

    public void changeCard(int index, Card newCard){
        changeTotal(newCard.getValue() - cards.get(index).getValue());
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
