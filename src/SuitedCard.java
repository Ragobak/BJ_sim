public class SuitedCard extends Card{
    private final char suit;

    public SuitedCard(int value, char suit){
        super(value);
        this.suit = suit;
    }

    @Override
    public char getSuit(){
        return suit;
    }

    public String toString(){
        return "" + value + suit;
    }

}
