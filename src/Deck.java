import java.util.concurrent.ThreadLocalRandom;

public class Deck{

    private final Card[] cards = new Card[10];

    public Deck(){
        for (int i = 0; i < 10; i++) {
            cards[i] = new Card( i+2, 4);
            if(i == 8){
                cards[i].setAmtLeft(16);
            }
        }
    }

    public Card[] getCards(){
        return cards;
    }

    public Card getCard(int index){
        return cards[index];
    }

    public Card pickCard(){
        int totalCards = getLeftInDeck();
        int cardIndex = ThreadLocalRandom.current().nextInt(totalCards);
        int cumulativeSum = 0;

        for (Card card : cards) {
            cumulativeSum += card.getAmtLeft();
            if (cardIndex < cumulativeSum) {
                card.setAmtLeft(card.getAmtLeft() - 1);
                return card;
            }
        }

        return null; // Shouldn't reach here if totalCards is correct
    }

    public int getLeftInDeck(){
        int total = 0;
        for(Card i : cards){
            total += i.getAmtLeft();
        }
        return total;
    }

    public String toString(){
        StringBuilder r = new StringBuilder();
        for (Card card : cards) {
            r.append("There are ").append(card.getAmtLeft()).append(" ").append(card.getValue()).append("'s remaining \n");
        }
        return r.toString();
    }
}