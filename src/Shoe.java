

public class Shoe extends Deck{
    private final int decks;
    
    public Shoe(int decks){
        super();
        for (int i = 0; i < 10; i++){
            getCard(i).setAmtLeft(getCard(i).getAmtLeft() * decks);
        }
        this.decks = decks;
    }

    public int getDecks(){
        return decks;
    }

    public void resetShoe(){
        for (int i = 0; i < 10; i++){
            getCard(i).setAmtLeft(4 * decks);
            if (i == 8) {
                getCard(i).setAmtLeft(4 * getCard(i).getAmtLeft());
            }
        }
    }
}